package barikoi.barikoilocation.SearchAutoComplete;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import barikoi.barikoilocation.Api;
import barikoi.barikoilocation.JsonUtils;
import barikoi.barikoilocation.NearbyPlace.NearbyPlaceAPI;
import barikoi.barikoilocation.PlaceModels.SearchAutoCompletePlace;
import barikoi.barikoilocation.RequestQueueSingleton;

/**
 * This is the API class of SearchAutoComplete
 * if anyone not willing to use the SearchAutoComplete Activity, they can use this class to hit the autocomplete server and the get place list
 */
public class SearchAutoCompleteAPI {
    private static final String TAG="SearchAutoCompleteApi";
    Context context;
    String nameOrCode, params;
    private Double latitude;
    private Double longitude;
    /**
     * This constructor sets the context of application and a SearchAutoComplete listener
     * @param context is the application context

     */
    private SearchAutoCompleteAPI(Context context,String nameOrCode, Double latitude,Double longitude){
        this.context=context;
        this.nameOrCode=nameOrCode;
        this.latitude=latitude;
        this.longitude=longitude;
    }
    /**
     * This method builds the Builder of this class
     * @param context is the application context
     * @return a new instance of Builder class
     */
    public static Builder builder(Context context){
        return new Builder(context);
    }

    /**
     *  requests the server to get info about the given place name
     */
    public void generateList(SearchAutoCompleteListener searchAutoCompleteListener) {
        if(this.latitude > 0 && this.longitude > 0){
            params = "?q="+nameOrCode+"&latitude="+this.latitude+"&longitude="+this.longitude;
        }else {
            params = "?q="+nameOrCode;
        }
        RequestQueue queue= RequestQueueSingleton.getInstance(this.context).getRequestQueue();
        queue.cancelAll("search");
        Log.d("SearchAC", "LATLNG: " +this.latitude+ ", " +this.longitude);
        if (this.nameOrCode.length() > 0) {
            Log.d("SearchAC", "not null Search");
            StringRequest request = new StringRequest(Request.Method.GET,
                    Api.autoCompleteString +params,
                    (String response) -> {
                        try {
                            JSONObject data = new JSONObject(response);
                            if(data.has("status")){
                                if(data.getInt("status")==200){
                                    JSONArray placearray = data.getJSONArray("places");

                                    if (placearray.length() == 0) {
                                        Log.d(TAG,"Place Not Found");
                                        searchAutoCompleteListener.onFailure(JsonUtils.logError(TAG,response));
                                    } else {
                                        ArrayList<SearchAutoCompletePlace> searchPlaces = JsonUtils.getSearchAutoCompletePlaces(placearray);
                                        searchAutoCompleteListener.onPlaceListReceived(searchPlaces);
                                    }
                                }else{
                                    searchAutoCompleteListener.onFailure(data.getString("message"));
                                }
                            }else searchAutoCompleteListener.onFailure(data.getString("message"));

                        } catch (JSONException e) {
                            searchAutoCompleteListener.onFailure(JsonUtils.logError(TAG,response));
                        }
                    },
                    error ->{
                        Log.d(TAG,JsonUtils.handleResponse(error));
                        searchAutoCompleteListener.onFailure(JsonUtils.handleResponse(error));
                    }){
            };
            request.setTag("search");
            queue.add(request);
        }
    }
    /**
     * This builder is used to create a new request to the Search AutoComplete API
     * At a bare minimum, your request
     * must include an application context, a Name or Code of your desired place. All other fields can be left alone
     * inorder to use the default behaviour of the API.
     */
    public static final class Builder{
        private Context context;
        private String nameOrCode="";
        Double latitude=0.0;
        Double longitude=0.0;

        /**
         * Private constructor for initializing the raw SearchAutoComplete.Builder
         */
        private Builder(Context context){ this.context=context;}

        /**
         * The name or code the user want to search with
         * @param nameOrCode is the input string of place name or code for searching place
         * @return
         */
        public Builder nameOrCode(String nameOrCode){
            this.nameOrCode=nameOrCode;
            return this;
        }

        public Builder setLatLng(Double latitude, Double longitude){
            this.latitude=latitude;
            this.longitude=longitude;
            return this;
        }
        /**
         * This uses the provided parameters set using the {@link Builder} and adds the required
         * settings for search Autocomplete to work correctly.
         *
         * @return a new instance of Search Autocomplete
         */
        public SearchAutoCompleteAPI build(){
            SearchAutoCompleteAPI searchAutoCompleteAPI=new SearchAutoCompleteAPI(this.context,this.nameOrCode, this.latitude,this.longitude);
            return searchAutoCompleteAPI;
        }
    }
}
