package barikoi.barikoilocation.SearchAutoComplete;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import barikoi.barikoilocation.GeoCode.GeoCodeAPI;
import barikoi.barikoilocation.GeoCode.PlaceGeoCodeListener;
import barikoi.barikoilocation.PlaceModels.GeoCodePlace;
import barikoi.barikoilocation.PlaceModels.SearchAutoCompletePlace;
import barikoi.barikoilocation.R;
import barikoi.barikoilocation.RequestQueueSingleton;

/**
 * This activity provides a search widget and RecyclerViewEmptySupport
 * User can search for a place, and the result list will be shown in the RecyclerViewEmptySupport
 */
public class SearchAutoCompleteActivity extends AppCompatActivity {

    private ArrayList<SearchAutoCompletePlace> items;
    private RequestQueue queue;
    private RecyclerViewEmptySupport listView;
    private PlaceSearchAdapter placeAdapter;
    private EditText editTextSearchAutoComplete;
    private ProgressBar progressBar;
    private final static String JSONErrorMessage="not found";
    private final static String TAG="SearchACActivity";
    private static final int AUTOCOMPLETE_DELAY = 300;
    private static final int MESSAGE_TEXT_CHANGED = 0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_auto_complete);
        init();
        progressBar=findViewById(R.id.progressBarSearchPlace);
        progressBar.setVisibility(View.GONE);
        editTextSearchAutoComplete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP && progressBar.getVisibility()!=View.VISIBLE) {
                    if(editTextSearchAutoComplete.getCompoundDrawables()[DRAWABLE_RIGHT]!=null)
                        if(event.getRawX() >= (editTextSearchAutoComplete.getRight() - editTextSearchAutoComplete.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            editTextSearchAutoComplete.setText("");
                            items.clear();
                            placeAdapter.notifyDataSetChanged();
                            return true;
                        }
                }
                return false;
            }
        });
        editTextSearchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listView.emptyshow(false);
                editTextSearchAutoComplete.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                progressBar.setVisibility(View.VISIBLE);
                if(charSequence.length()>=2){
                    queue.cancelAll("search");
                    mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
                    final Message msg = Message.obtain(mHandler, MESSAGE_TEXT_CHANGED, charSequence.toString());
                    mHandler.sendMessageDelayed(msg, AUTOCOMPLETE_DELAY);

                }
                else {
                    progressBar.setVisibility(View.GONE);
                    queue.cancelAll("search");
                    items.clear();
                    placeAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * Initializes the views needed in this activity
     */
    private void init(){
        queue= RequestQueueSingleton.getInstance(getApplicationContext()).getRequestQueue();
        editTextSearchAutoComplete =findViewById(R.id.barikoiEditText);
        items=new ArrayList<SearchAutoCompletePlace>();
        listView= findViewById(R.id.searchedplacelist);
        View emptyList=findViewById(R.id.LinearLayoutListEmpty);
        View noNetList=findViewById(R.id.LinearLayoutNoNetContainer);
        listView.setNonetview(noNetList);
        listView.setEmptyView(emptyList);
        placeAdapter=new PlaceSearchAdapter(items, new PlaceSearchAdapter.OnPlaceItemSelectListener() {
            @Override
            public void onPlaceSelected(SearchAutoCompletePlace mItem, int position) {
                GeoCodeAPI.builder(getApplicationContext())
                        .idOrCode(mItem.getId())
                        .build()
                        .generateList(new PlaceGeoCodeListener() {
                            @Override
                            public void onGeoCodePlace(GeoCodePlace place) {
                                Intent returnIntent = getIntent();
                                returnIntent.putExtra("place_selected",place);
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                            }
                            @Override
                            public void onFailure(String message) {
                                Log.d(TAG,message);
                            }
                        });

            }

        });
        listView.setAdapter(placeAdapter);
        editTextSearchAutoComplete.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_TEXT_CHANGED) {
                String enteredText = (String)msg
                        .obj;
                items.clear();
                placeAdapter.notifyDataSetChanged();
                queue.cancelAll("search");
                SearchAutoCompleteAPI.builder(getApplicationContext())
                        .nameOrCode(enteredText)
                        .build()
                        .generateList(new SearchAutoCompleteListener() {
                            @Override
                            public void onPlaceListReceived(ArrayList<SearchAutoCompletePlace> places) {
                                if(places.size()>0){
                                    progressBar.setVisibility(View.GONE);
                                    items.addAll(places);
                                    placeAdapter.notifyDataSetChanged();
                                }
                                else listView.emptyshow(true);
                            }
                            @Override
                            public void onFailure(String message) {
                                if(message.equals(JSONErrorMessage)){
                                    progressBar.setVisibility(View.GONE);
                                    listView.emptyshow(true);
                                }
                                else{
                                    progressBar.setVisibility(View.GONE);
                                    listView.nonetshow(true);
                                }
                            }
                        });
            }
        }
    };
}
