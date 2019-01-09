package barikoi.barikoilocation.NearbyPlace;

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


public class NearbyPlaceAPI {
    private static final String TAG="NearbyPlaceApi";
    private Context context;
    private Double distance;
    private int limit;
    private Double latitude;
    private Double longitude;

    /**
     * This constructor sets the context of application and a NearbyPlaceAPI listener
     * @param context is the application context
     */
    private NearbyPlaceAPI(Context context,Double distance, int limit,Double latitude,Double longitude){
        this.context=context;
        this.distance=distance;
        this.limit=limit;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public void generateNearbyPlaceList(NearbyPlaceListener nearbyPlaceListener){
        RequestQueue queue= RequestQueueSingleton.getInstance(this.context).getRequestQueue();
        queue.cancelAll("search");
        if(isValidLatLng(this.latitude,this.longitude)){
            StringRequest request = new StringRequest(Request.Method.GET,
                    Api.nearbyPlacesString+this.distance+"/"+this.limit+"/?latitude="+this.latitude+"&longitude="+this.longitude,
                    (String response) -> {
                        try {
                            JSONObject data=new JSONObject(response);
                            JSONArray placearray = data.getJSONArray("Place");

                            if (placearray.length() == 0) {
                                Log.d(TAG,"No places Found");
                                nearbyPlaceListener.onFailure("No places Found");
                            } else {
                                ArrayList<Place> searchPlaces = JsonUtils.getPlaces(placearray);
                                nearbyPlaceListener.onPlaceListReceived(searchPlaces);
                            }

                        } catch (JSONException e) {
                            nearbyPlaceListener.onFailure(JsonUtils.logError(TAG,response));
                        }
                    },
                    error ->{
                        Log.d(TAG,JsonUtils.handleResponse(error));
                        nearbyPlaceListener.onFailure(JsonUtils.handleResponse(error));
                    }){
            };
            request.setTag("search");
            queue.add(request);
        }
        else{
            Log.d(TAG,"Latitude and Longitude Invalid");
            nearbyPlaceListener.onFailure("Latitude and Longitude Invalid");
        }
    }
    public static Builder builder(Context context){
        return new Builder(context);
    }
    /**
     * Checks if the given latitude and longitude is valid or not
     * @param lat of location of which you want to get the nearby places
     * @param lng of location of which you want to get the nearby places
     * @return true or false if the latitude and longitude is right or wrong
     */
    private boolean isValidLatLng(double lat, double lng){
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
    /**
     * This builder is used to create a new request to the Search Nearby API
     * At a bare minimum, your request
     * must include an application context, a latitude and longitude of the point you are seeking address
     * a distance radius in kilometers in which you want to bound your results
     * a limit for number of places you want
     * All other fields can be left alone
     * inorder to use the default behaviour of the API.
     */
    public static final class Builder{
        Context context;
        double distance=.5;
        int limit=10;
        Double latitude=0.0;
        Double longitude=0.0;

        /**
         * Private constructor for initializing the raw NearbyPlace.Builder
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
         *  This class is to set the distance you want to limit your search to
         * @param distance
         * @return
         */
        public Builder setDistance(double distance){
            this.distance=distance;
            return this;
        }

        /**
         * This sets the limit of places you want to get
         * @param limit of the numbers of places you want to get in nearby areas
         * @return
         */
        public Builder setLimit(int limit){
            this.limit=limit;
            return this;
        }
        /**
         * This uses the provided parameters set using the {@link Builder} and adds the required
         * settings for nearby search to work correctly.
         *
         * @return a new instance of NearbyPlace
         */
        public NearbyPlaceAPI build(){
            NearbyPlaceAPI nearbyPlaceAPI=new NearbyPlaceAPI(this.context,this.distance,this.limit,this.latitude,this.longitude);
            return nearbyPlaceAPI;
        }
    }
}
