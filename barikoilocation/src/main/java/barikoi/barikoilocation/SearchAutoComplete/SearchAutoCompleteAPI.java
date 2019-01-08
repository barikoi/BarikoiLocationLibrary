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
    public SearchAutoCompleteAPI(Context context,String nameOrCode){
        this.context=context;
        this.nameOrCode=nameOrCode;
    }
    public static Builder builder(Context context){
        return new Builder(context);
    }
    /**
     *  requests the server to get info about the given place name
     */
    public void generatelist(SearchAutoCompleteListener searchAutoCompleteListener) {
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
                                searchAutoCompleteListener.OnFailure("Place Not Found!");
                            } else {
                                ArrayList<Place> searchPlaces = JsonUtils.getPlaces(placearray);
                                searchAutoCompleteListener.OnPlaceListReceived(searchPlaces);
                            }

                        } catch (JSONException e) {
                            try{
                                JSONObject data = new JSONObject(response);
                            }
                            catch (JSONException ex){
                                Log.d(TAG,ex.toString());
                                searchAutoCompleteListener.OnFailure(ex.toString());
                            }
                        }
                    },
                    error ->{
                        Log.d(TAG,JsonUtils.handleResponse(error));
                        searchAutoCompleteListener.OnFailure(JsonUtils.handleResponse(error));
                    }){
            };
            request.setTag("search");
            queue.add(request);
        }
    }

    public static final class Builder{
        Context context;
        String nameOrCode;

        private Builder(Context context){ this.context=context;}

        public Builder nameOrCode(String nameOrCode){
            this.nameOrCode=nameOrCode;
            return this;
        }
        public SearchAutoCompleteAPI build(){
            SearchAutoCompleteAPI searchAutoCompleteAPI=new SearchAutoCompleteAPI(this.context,this.nameOrCode);
            return searchAutoCompleteAPI;
        }
    }
}
