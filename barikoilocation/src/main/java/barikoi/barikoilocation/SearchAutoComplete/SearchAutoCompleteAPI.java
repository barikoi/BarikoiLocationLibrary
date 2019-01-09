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
import barikoi.barikoilocation.Place;
import barikoi.barikoilocation.RequestQueueSingleton;

/**
 * This is the API class of SearchAutoComplete
 * if anyone not willing to use the SearchAutoComplete Activity, they can use this class to hit the autocomplete server and the get place list
 */
public class SearchAutoCompleteAPI {
    private static final String TAG="SearchAutoCompleteApi";
    Context context;
    String nameOrCode;
    /**
     * This constructor sets the context of application and a SearchAutoComplete listener
     * @param context is the application context

     */
    private SearchAutoCompleteAPI(Context context,String nameOrCode){
        this.context=context;
        this.nameOrCode=nameOrCode;
    }
    public static Builder builder(Context context){
        return new Builder(context);
    }
    /**
     *  requests the server to get info about the given place name
     */
    public void generateList(SearchAutoCompleteListener searchAutoCompleteListener) {
        RequestQueue queue= RequestQueueSingleton.getInstance(this.context).getRequestQueue();
        queue.cancelAll("search");
        if (this.nameOrCode.length() > 0) {
            StringRequest request = new StringRequest(Request.Method.GET,
                    Api.autoCompleteString +"?q="+this.nameOrCode,
                    (String response) -> {
                        try {
                            JSONObject data = new JSONObject(response);
                            JSONArray placearray = data.getJSONArray("places");

                            if (placearray.length() == 0) {
                                Log.d(TAG,"Place Not Found");
                                searchAutoCompleteListener.onFailure("Place Not Found!");
                            } else {
                                ArrayList<Place> searchPlaces = JsonUtils.getPlaces(placearray);
                                searchAutoCompleteListener.onPlaceListReceived(searchPlaces);
                            }
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
        /**
         * This uses the provided parameters set using the {@link Builder} and adds the required
         * settings for search Autocomplete to work correctly.
         *
         * @return a new instance of Search Autocomplete
         */
        public SearchAutoCompleteAPI build(){
            SearchAutoCompleteAPI searchAutoCompleteAPI=new SearchAutoCompleteAPI(this.context,this.nameOrCode);
            return searchAutoCompleteAPI;
        }
    }
}
