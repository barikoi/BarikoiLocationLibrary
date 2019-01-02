package barikoi.barikoilocation.NearbyPlace;

import android.content.Context;

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
import barikoi.barikoilocation.RequestQueueSingleton;


public class NearbyPlaceAPI {
    private RequestQueue queue;
    private NearbyPlaceListener nearbyPlaceListener;

    public NearbyPlaceAPI(Context context, NearbyPlaceListener nearbyPlaceListener){
        queue= RequestQueueSingleton.getInstance(context).getRequestQueue();
        this.nearbyPlaceListener=nearbyPlaceListener;
    }
    public void generateNearbyPlaceList(Double distance, int limit,Double latitude,Double longitude){
        queue.cancelAll("search");
        if(isValidLatLng(latitude,longitude)){
            StringRequest request = new StringRequest(Request.Method.GET,
                    Api.NearbyPlacesString+distance+"/"+limit+"/?latitude="+latitude+"&longitude="+longitude,
                    (String response) -> {
                        try {
                            JSONArray placearray = new JSONArray(response);

                            if (placearray.length() == 0) {
                                this.nearbyPlaceListener.OnFailure("No places Found");
                            } else {
                                ArrayList<Place> searchPlaces = JsonUtils.getPlaces(placearray);
                                this.nearbyPlaceListener.OnPlaceListReceived(searchPlaces);
                            }

                        } catch (JSONException e) {
                            try{
                                JSONObject data = new JSONObject(response);
                            }
                            catch (JSONException ex){
                                //Toast.makeText(this,"problem formatting data", Toast.LENGTH_SHORT).show();
                                this.nearbyPlaceListener.OnFailure(ex.toString());
                                ex.printStackTrace();
                            }
                        }
                    },
                    error ->{
                        this.nearbyPlaceListener.OnFailure(JsonUtils.handleResponse(error));
                    }){
            };
            request.setTag("search");
            queue.add(request);
        }
        else{
            this.nearbyPlaceListener.OnFailure("Latitude and Longitude Invalid");
        }
    }
    public boolean isValidLatLng(double lat, double lng){
        if(lat < -90 || lat > 90)
        {
            return false;
        }
        else if(lng < -180 || lng > 180)
        {
            return false;
        }
        return true;
    }

}
