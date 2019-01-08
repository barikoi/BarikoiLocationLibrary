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
import barikoi.barikoilocation.Place;
import barikoi.barikoilocation.RequestQueueSingleton;

/**
 * This Class is created to handle all GeoCode related network calls
 */
public class GeoCodeAPI {
    private static final String TAG="GeoCodeApi";
    Context context;
    String nameOrCode;

    /**
     * This constructor sets the context of application and a PlaceGeoCodeAPI listener
     */
    private GeoCodeAPI(Context context,String nameOrCode){
        this.context=context;
        this.nameOrCode=nameOrCode;
    }
    public static Builder builder(Context context){
        return new Builder(context);
    }
    /**
     * This function makes network call with the api to get the place details
     *  requests the server to get info about the current position
     */
    public void generatelist(PlaceGeoCodeListener placeGeoCodeListener) {
        RequestQueue queue= RequestQueueSingleton.getInstance(this.context).getRequestQueue();
        queue.cancelAll("search");
        if (this.nameOrCode.length() > 0) {
            StringRequest request = new StringRequest(Request.Method.GET,
                    Api.geoCodeString +this.nameOrCode,
                    (String response) -> {
                        try {
                            JSONObject data = new JSONArray(response).getJSONObject(0);
                                Place place = JsonUtils.getPlace(data);
                                placeGeoCodeListener.geoCodePlace(place);
                        } catch (JSONException e) {
                            try{
                                JSONObject data = new JSONObject(response);
                            }
                            catch (JSONException ex){
                                Log.d(TAG,ex.toString());
                                placeGeoCodeListener.onFailure(ex.toString());
                                ex.printStackTrace();
                            }
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
    }

    public static final class Builder{
        Context context;
        String nameOrCode;

        private Builder(Context context){ this.context=context;}

        public Builder nameOrCode(String nameOrCode){
            this.nameOrCode=nameOrCode;
            return this;
        }
        public GeoCodeAPI build(){
            GeoCodeAPI geoCodeAPI=new GeoCodeAPI(this.context,this.nameOrCode);
            return geoCodeAPI;
        }
    }
}
