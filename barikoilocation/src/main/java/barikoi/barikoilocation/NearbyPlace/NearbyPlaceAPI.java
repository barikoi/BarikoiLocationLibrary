package barikoi.barikoilocation.NearbyPlace;

import android.content.Context;

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


public class NearbyPlaceAPI {
    private RequestQueue queue;
    private NearbyPlaceListener nearbyPlaceListener;

    /**
     * This constructor sets the context of application and a NearbyPlaceAPI listener
     * @param context is the application context
     * @param nearbyPlaceListener is Nearby Place Listener to handle the network response
     */
    public NearbyPlaceAPI(Context context, NearbyPlaceListener nearbyPlaceListener){
        queue= RequestQueueSingleton.getInstance(context).getRequestQueue();
        this.nearbyPlaceListener=nearbyPlaceListener;
    }

    /**
     * @param distance radius around tha latitude an longitude provided by the user to get the nearby places
     * @param limit total number of places you want to get
     * @param latitude of location of which you want to get the nearby places list
     * @param longitude of location of which you want to get the nearby places list
     */
    public void generateNearbyPlaceList(Double distance, int limit,Double latitude,Double longitude){
        queue.cancelAll("search");
        if(isValidLatLng(latitude,longitude)){
            StringRequest request = new StringRequest(Request.Method.GET,
                    Api.nearbyPlacesString +distance+"/"+limit+"/?latitude="+latitude+"&longitude="+longitude,
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

    /**
     * Checks if the given latitude and longitude is valid or not
     * @param lat of location of which you want to get the nearby places
     * @param lng of location of which you want to get the nearby places
     * @return true or false if the latitude and longitude is right or wrong
     */
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
