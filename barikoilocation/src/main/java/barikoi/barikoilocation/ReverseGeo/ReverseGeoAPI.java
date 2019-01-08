package barikoi.barikoilocation.ReverseGeo;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
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
import barikoi.barikoilocation.Place;
import barikoi.barikoilocation.RequestQueueSingleton;

/**
 * Handles the Location Requests and Response from server of Reverse Geo Api
 */

public class ReverseGeoAPI {
    private static final String TAG="ReverseGeoApi";
    Context context;
    Double latitude;
    Double longitude;

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
                            Place p=JsonUtils.getPlace(place);
                            if(p!=null && reverseGeoAPIListener !=null ) reverseGeoAPIListener.reversedAddress(p);

                        } catch (JSONException e) {
                            Log.d(TAG,e.toString());
                            reverseGeoAPIListener.onFailure(e.toString());
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
    public static Builder builder(Context context) {
        return  new Builder(context);
    }

   public static final class Builder{
       Context context;
       Double latitude;
       Double longitude;

       private Builder(Context context){this.context=context;}

       public Builder SetLatLng(Double latitude, Double longitude){
           this.latitude=latitude;
           this.longitude=longitude;
           return this;
       }
       public ReverseGeoAPI build(){
           ReverseGeoAPI reverseGeoAPI=new ReverseGeoAPI(this.context,this.latitude,this.longitude);
           return reverseGeoAPI;
       }
   }
}
