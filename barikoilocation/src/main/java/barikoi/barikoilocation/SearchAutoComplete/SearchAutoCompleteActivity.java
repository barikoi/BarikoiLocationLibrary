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
    //private RecyclerView recentSearchlistView;

    //private ProgressBar loading;
    private String token;

    public static final int REQUEST_SEARCH_CODE=69;
    private SearchAdapter placeAdapter;
    //private SearchAdapter recentPlaceAdapter;
    TextView tvRecentSearch;
    EditText editText;
    ProgressBar progressBar;
    private TextView addplace;

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
                if(charSequence.length()>=2){
                    //findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    //progressBar.setVisibility(View.VISIBLE);

                    items.clear();
                    placeAdapter.notifyDataSetChanged();
                    queue.cancelAll("search");

                    generatelist(charSequence.toString());
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
        //loading= findViewById(R.id.loading);
        listView= findViewById(R.id.searchedplacelist);
        View emptylist=findViewById(R.id.LinearLayoutListEmpty);
        View nonetList=findViewById(R.id.LinearLayoutNoNetContainer);
        listView.setNonetview(nonetList);
        listView.setEmptyView(emptylist);
        // recentSearchlistView= findViewById(R.id.recentsearchedplacelist);
        placeAdapter=new SearchAdapter(items, new SearchAdapter.OnPlaceItemSelectListener() {
            @Override
            public void onPlaceItemSelected(Place mItem, int position) {

            }
        });
        //recentPlaceAdapter=new SearchAdapter(items, this );
        listView.setAdapter(placeAdapter);
        //recentSearchlistView.setAdapter(recentPlaceAdapter);


        editText.requestFocus();
    }
    /**
     * @param nameOrCode is the place searching for in the app
     *  requests the server to get info about the current position
     */
    public void generatelist(final String nameOrCode) {
        progressBar.setVisibility(View.VISIBLE);
        editText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        queue.cancelAll("search");
        items.clear();
        if (nameOrCode.length() > 0) {
            StringRequest request = new StringRequest(Request.Method.GET,
                    Api.AutoCompleteString+"?q="+nameOrCode,
                    (String response) -> {
                        progressBar.setVisibility(View.GONE);
                        editText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.clearitems,0);
                        try {
                            JSONObject data = new JSONObject(response);
                            JSONArray placearray = data.getJSONArray("places");

                            Bundle b = new Bundle();
                            b.putString("query", nameOrCode);
                            b.putInt("result_numbers", placearray.length());
                            if (placearray.length() == 0) {
                                listView.emptyshow(true);
                                //Toast.makeText(this,"google", Toast.LENGTH_SHORT).show();
                            } else {
                                ArrayList<Place> newplaces = JsonUtils.getPlaces(placearray);
                                items.addAll(newplaces);
                                placeAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            try{
                                JSONObject data = new JSONObject(response);
                                listView.emptyshow(true);
                                //Toast.makeText(SearchPlaceActivity.this,data.getJSONObject("places").getString("Message"), Toast.LENGTH_SHORT).show();
                            }
                            catch (JSONException ex){
                                Toast.makeText(SearchAutoCompleteActivity.this,"problem formatting data", Toast.LENGTH_SHORT).show();
                                ex.printStackTrace();
                            }

                        }
                    },
                    error -> {
                        //loading.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        editText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.clearitems,0);
                        JsonUtils.logResponse(error);
                        listView.nonetshow(true);
                        //JsonUtils.handleResponse(error, SearchPlaceActivity.this);
                        Log.d("search params",nameOrCode);
                        /*Toast toast= Toast.makeText(getApplicationContext(),
                                "Not Found", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 250);
                        toast.show();*/

                    }) {

            };
            request.setTag("search");
            //loading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            queue.add(request);
        }
    }

}
