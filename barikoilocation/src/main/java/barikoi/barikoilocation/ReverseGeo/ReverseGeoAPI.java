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
 * Handles the Location Requests and Response from server
 * Created by Sakib on 6/25/2017.
 */

public class ReverseGeoAPI {
    private ReverseGeoAPIListener reverseGeoAPIListener;
    Context context;
    private static final String TAG="ReverseGeoAPI";
    private boolean isactive;


    public ReverseGeoAPI(Context context, ReverseGeoAPIListener reverseGeoAPIListener){
        this.context=context;
        this.reverseGeoAPIListener = reverseGeoAPIListener;
        isactive=false;

    }
    public void getAddress(final double lat, final double lon){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        //final String token = prefs.getString(Api.TOKEN, "");
        RequestQueue queue = RequestQueueSingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET,
                Api.ReverseString+"?latitude="+lat+"&longitude="+lon,
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
                            /*float distance=Float.valueOf(p.getDistance());
                            if(distance>50){
                                 //getaddressG(lat,lon);
                            }
                            else*/ if(p!=null && reverseGeoAPIListener !=null ) reverseGeoAPIListener.reversedAddress(p);

                        } catch (JSONException e) {
                            reverseGeoAPIListener.onFailure(e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //loading.setVisibility(View.GONE);
                        Log.d("ErrorReverse",""+error.getMessage());
                        reverseGeoAPIListener.onFailure(JsonUtils.handleResponse(error));
                    }
                }
        );
        queue.add(request);
    }
    public void cancelRevGeo(){
        RequestQueue queue = RequestQueueSingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        queue.cancelAll("addressreq");
    }
}
