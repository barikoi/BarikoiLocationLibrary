package barikoi.barikoilocation.GeoCode;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import barikoi.barikoilocation.Api;
import barikoi.barikoilocation.JsonUtils;
import barikoi.barikoilocation.PlaceModels.GeoCodePlaceModel;
import barikoi.barikoilocation.RequestQueueSingleton;

/**
 * This Class is created to handle all GeoCode related network calls
 */
public class GeoCodeAPI {
    private static final String TAG="GeoCodeApi";
    private Context context;
    private String idOrCode;

    /**
     * This constructor sets the context of application and a PlaceGeoCodeAPI listener
     */
    private GeoCodeAPI(Context context,String idOrCode){
        this.context=context;
        this.idOrCode = idOrCode;
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
     * This function makes network call with the api to get the place details
     *  requests the server to get info about the current position
     */
    public void generateList(PlaceGeoCodeListener placeGeoCodeListener) {
        RequestQueue queue= RequestQueueSingleton.getInstance(this.context).getRequestQueue();
        queue.cancelAll("search");
        if (this.idOrCode.length() > 0) {
            StringRequest request = new StringRequest(Request.Method.GET,
                    Api.geoCodeString +this.idOrCode,
                    (String response) -> {
                        try {
                            JSONObject data = new JSONArray(response).getJSONObject(0);
                                GeoCodePlaceModel place = JsonUtils.getGeoCodePlace(data);
                                placeGeoCodeListener.onGeoCodePlace(place);
                        } catch (JSONException e) {
                            placeGeoCodeListener.onFailure(JsonUtils.logError(TAG,response));
                        }
                    },
                    error ->{
                        Log.d(TAG,JsonUtils.handleResponse(error));
                        placeGeoCodeListener.onFailure(JsonUtils.handleResponse(error));
                    }){
            };
            request.setTag("search");
            queue.add(request);
        }
        else{
            Log.d(TAG,"Geo Code is Null");
            placeGeoCodeListener.onFailure("Geo Code is Null");
        }
    }
    /**
     * This builder is used to create a new request to the GeoCode API
     * At a bare minimum, your request
     * must include an application context, a Name or Code of your desired place. All other fields can be left alone
     * inorder to use the default behaviour of the API.
     */
    public static final class Builder{
        private Context context;
        private String idOrCode="";

        /**
         * Private constructor for initializing the raw GeoCode.Builder
         */
        private Builder(Context context){ this.context=context;}

        /**
         * The name or code the user want to search with
         * @param idOrCode is the input string of place name or code for searching place
         * @return
         */
        public Builder idOrCode(String idOrCode){
            this.idOrCode =idOrCode;
            return this;
        }

        /**
         * This uses the provided parameters set using the {@link Builder} and adds the required
         * settings for GeoCode to work correctly.
         *
         * @return a new instance of GeoCode
         */
        public GeoCodeAPI build(){
            GeoCodeAPI geoCodeAPI=new GeoCodeAPI(this.context,this.idOrCode);
            return geoCodeAPI;
        }
    }
}
