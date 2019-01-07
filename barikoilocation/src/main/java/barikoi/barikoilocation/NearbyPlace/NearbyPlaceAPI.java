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
    Context context;
    Double distance;
    int limit;
    Double latitude;
    Double longitude;
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
                            JSONArray placearray = new JSONArray(response);

                            if (placearray.length() == 0) {
                                nearbyPlaceListener.OnFailure("No places Found");
                            } else {
                                ArrayList<Place> searchPlaces = JsonUtils.getPlaces(placearray);
                                nearbyPlaceListener.OnPlaceListReceived(searchPlaces);
                            }

                        } catch (JSONException e) {
                            try{
                                JSONObject data = new JSONObject(response);
                            }
                            catch (JSONException ex){
                                //Toast.makeText(this,"problem formatting data", Toast.LENGTH_SHORT).show();
                                nearbyPlaceListener.OnFailure(ex.toString());
                                ex.printStackTrace();
                            }
                        }
                    },
                    error ->{
                        nearbyPlaceListener.OnFailure(JsonUtils.handleResponse(error));
                    }){
            };
            request.setTag("search");
            queue.add(request);
        }
        else{
            nearbyPlaceListener.OnFailure("Latitude and Longitude Invalid");
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
    public static final class Builder{
        Context context;
        double distance;
        int limit;
        Double latitude;
        Double longitude;

        private Builder(Context context){this.context=context;}

        public Builder setLatLng(Double latitude, Double longitude){
            this.latitude=latitude;
            this.longitude=longitude;
            return this;
        }
        public Builder setDistance(double distance){
            this.distance=distance;
            return this;
        }
        public Builder setLimit(int limit){
            this.limit=limit;
            return this;
        }
        public NearbyPlaceAPI build(){
            NearbyPlaceAPI nearbyPlaceAPI=new NearbyPlaceAPI(this.context,this.distance,this.limit,this.latitude,this.longitude);
            return nearbyPlaceAPI;
        }
    }
}
