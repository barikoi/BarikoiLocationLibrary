package barikoi.barikoilocation.SearchAutoComplete;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import barikoi.barikoilocation.Api;
import barikoi.barikoilocation.JsonUtils;
import barikoi.barikoilocation.Place;
import barikoi.barikoilocation.R;
import barikoi.barikoilocation.RequestQueueSingleton;

public class SearchAutoCompleteActivity extends AppCompatActivity {

    private ArrayList<Place> items;
    private RequestQueue queue;
    private RecyclerViewEmptySupport listView;
    SearchAutoCompleteListener searchAutoCompleteListener;

    public static final int REQUEST_SEARCH_CODE=69;
    private SearchAdapter placeAdapter;
    TextView tvRecentSearch;
    EditText editText;
    ProgressBar progressBar;
    private TextView addplace;
    SearchAutoCompleteAPI searchAutoCompleteAPI;
    //PlaceTask placeTask;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_auto_complete);
        init();
        // handledearch(getIntent());
        progressBar=findViewById(R.id.progressBarSearchPlace);
        progressBar.setVisibility(View.GONE);
        searchAutoCompleteListener=new SearchAutoCompleteListener() {};

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP && progressBar.getVisibility()!=View.VISIBLE) {
                    if(editText.getCompoundDrawables()[DRAWABLE_RIGHT]!=null)
                        if(event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            editText.setText("");
                            items.clear();
                            placeAdapter.notifyDataSetChanged();
                            return true;
                        }
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listView.emptyshow(false);
                editText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                progressBar.setVisibility(View.VISIBLE);
                if(charSequence.length()>=2){
                    //findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    //progressBar.setVisibility(View.VISIBLE);
                    items.clear();
                    placeAdapter.notifyDataSetChanged();
                    queue.cancelAll("search");
                    searchAutoCompleteAPI.generatelist(charSequence.toString());
                    editText.requestFocus();
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
        editText=findViewById(R.id.barikoiEditText);
        items=new ArrayList<Place>();
        listView= findViewById(R.id.searchedplacelist);
        View emptylist=findViewById(R.id.LinearLayoutListEmpty);
        View nonetList=findViewById(R.id.LinearLayoutNoNetContainer);
        listView.setNonetview(nonetList);
        listView.setEmptyView(emptylist);
        placeAdapter=new SearchAdapter(items, new SearchAdapter.OnPlaceItemSelectListener() {
            @Override
            public void onPlaceSelected(Place mItem, int position) {
                //Toast.makeText(SearchAutoCompleteActivity.this, mItem.getAddress(), Toast.LENGTH_SHORT).show();\
                /*searchAutoCompleteListener.OnPlaceSelected(mItem);
                getSelectedPlaceListener.getSelectedPlaceListener(mItem);*/
                //getCallingActivity();
              /*  BarikoiSearchAutocomplete barikoiSearchAutocomplete=new BarikoiSearchAutocomplete(getApplicationContext());
                barikoiSearchAutocomplete.getPlace(mItem);*/
                Intent returnIntent = getIntent();
                returnIntent.putExtra("place_selected",mItem);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
        listView.setAdapter(placeAdapter);
        editText.requestFocus();
        searchAutoCompleteAPI=new SearchAutoCompleteAPI(this, new SearchAutoCompleteListener() {
            @Override
            public void OnPlaceListReceived(ArrayList<Place> places) {
                if (places.size() == 0) {
                    listView.emptyshow(true);
                    //Toast.makeText(this,"google", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    items.addAll(places);
                    placeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void OnFailure(String message){
                Log.d("BarikoiError",message);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("error",message);
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });

    }
}
