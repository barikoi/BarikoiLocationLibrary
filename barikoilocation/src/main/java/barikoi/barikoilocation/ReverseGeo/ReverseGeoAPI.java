package barikoi.barikoilocation.ReverseGeo;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONException;
import org.json.JSONObject;

import barikoi.barikoilocation.Api;
import barikoi.barikoilocation.JsonUtils;
import barikoi.barikoilocation.PlaceModels.ReverseGeoPlace;
import barikoi.barikoilocation.RequestQueueSingleton;

/**
 * Handles the Location Requests and Response from server of Reverse Geo Api
 */

public class ReverseGeoAPI {
    private static final String TAG="ReverseGeoApi";
    private Context context;
    private Double latitude;
    private Double longitude;

    /**
     * Build a new object with the proper navigation parameters already setup.
     *
     * @return a {@link Builder} object for creating this object
     * @since 0.5.0
     */

    private ReverseGeoAPI(Context context, Double latitude, Double longitude){
        this.context=context;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    /**
     * This method builds the Builder of this class
     * @param context is the application context
     * @return a new instance of Builder class
     */
    public static Builder builder(Context context) {
        return  new Builder(context);
    }
    /**
     * Gets the place details of a given latitude and longitude
     */
    public void getAddress(ReverseGeoAPIListener reverseGeoAPIListener){
        RequestQueue queue = RequestQueueSingleton.getInstance(this.context.getApplicationContext()).getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET,
                Api.reverseString +"?latitude="+this.latitude+"&longitude="+this.longitude,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(context instanceof Activity){
                            Activity container=(Activity)context;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if(container.isDestroyed() || container.isFinishing()) return;
                            }
                        }
                        try {
                            JSONObject place= new JSONObject(response).getJSONArray("Place").getJSONObject(0);
                            ReverseGeoPlace p=JsonUtils.getReverseGeoPlace(place);
                            if(p!=null && reverseGeoAPIListener !=null ) reverseGeoAPIListener.reversedAddress(p);
                            else {
                                Log.d(TAG,"ReverseGeo Listener is null");
                                reverseGeoAPIListener.onFailure("ReverseGeo Listener is null");
                            }
                        } catch (JSONException e) {
                            reverseGeoAPIListener.onFailure(JsonUtils.logError(TAG,response));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,JsonUtils.handleResponse(error));
                        reverseGeoAPIListener.onFailure(JsonUtils.handleResponse(error));
                    }
                }
        );
        queue.add(request);

    }


    /**
     * This builder is used to create a new request to the ReverseGeo API
     * At a bare minimum, your request
     * must include an application context, a latitude and longitude of the point you are seeking address. All other fields can be left alone
     * inorder to use the default behaviour of the API.
     */
   public static final class Builder{
       Context context;
       Double latitude=0.0;
       Double longitude=0.0;

       /**
        * Private constructor for initializing the raw ReverseGeo.Builder
        */
       private Builder(Context context){this.context=context;}


        /**
         *  This class is to set the latitude and longitude you want to use search for
         * @param latitude is the latitude of a point
         * @param longitude is the longitude of a point
         * @return a builder class
         */
       public Builder setLatLng(Double latitude, Double longitude){
           this.latitude=latitude;
           this.longitude=longitude;
           return this;
       }
        /**
         * This uses the provided parameters set using the {@link Builder} and adds the required
         * settings for ReverseGeo to work correctly.
         *
         * @return a new instance of ReverseGeo
         */
       public ReverseGeoAPI build(){
           ReverseGeoAPI reverseGeoAPI=new ReverseGeoAPI(this.context,this.latitude,this.longitude);
           return reverseGeoAPI;
       }
   }
}
