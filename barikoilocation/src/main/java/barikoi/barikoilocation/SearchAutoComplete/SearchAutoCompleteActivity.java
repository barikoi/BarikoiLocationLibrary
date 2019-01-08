package barikoi.barikoilocation.SearchAutoComplete;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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

import barikoi.barikoilocation.Place;
import barikoi.barikoilocation.R;
import barikoi.barikoilocation.RequestQueueSingleton;

/**
 * This activity provides a search widget and RecyclerViewEmptySupport
 * User can search for a place, and the result list will be shown in the RecyclerViewEmptySupport
 */
public class SearchAutoCompleteActivity extends AppCompatActivity {

    private ArrayList<Place> items;
    private RequestQueue queue;
    private RecyclerViewEmptySupport listView;
    SearchAutoCompleteListener searchAutoCompleteListener;

    private PlaceSearchAdapter placeAdapter;
    EditText editTextSearchAutoComplete;
    ProgressBar progressBar;

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
                    items.clear();
                    placeAdapter.notifyDataSetChanged();
                    queue.cancelAll("search");
                    SearchAutoCompleteAPI.builder(getApplicationContext())
                           .nameOrCode(charSequence.toString())
                           .build()
                           .generatelist(new SearchAutoCompleteListener() {
                               @Override
                               public void OnPlaceListReceived(ArrayList<Place> places) {
                                   if (places.size() == 0) {
                                       listView.emptyshow(true);
                                   } else {
                                       progressBar.setVisibility(View.GONE);
                                       items.addAll(places);
                                       placeAdapter.notifyDataSetChanged();
                                   }
                               }
                               @Override
                               public void OnFailure(String message) {
                                   Log.d("BarikoiError",message);
                               }
                           });
                    editTextSearchAutoComplete.requestFocus();
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
        items=new ArrayList<Place>();
        listView= findViewById(R.id.searchedplacelist);
        View emptylist=findViewById(R.id.LinearLayoutListEmpty);
        View nonetList=findViewById(R.id.LinearLayoutNoNetContainer);
        listView.setNonetview(nonetList);
        listView.setEmptyView(emptylist);
        placeAdapter=new PlaceSearchAdapter(items, new PlaceSearchAdapter.OnPlaceItemSelectListener() {
            @Override
            public void onPlaceSelected(Place mItem, int position) {
                Intent returnIntent = getIntent();
                returnIntent.putExtra("place_selected",mItem);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
        listView.setAdapter(placeAdapter);
        editTextSearchAutoComplete.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }
}
