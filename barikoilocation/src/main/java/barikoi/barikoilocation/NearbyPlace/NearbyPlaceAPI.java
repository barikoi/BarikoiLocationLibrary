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
import barikoi.barikoilocation.SearchAutoComplete.SearchAutoCompleteListener;

public class NearbyPlaceAPI {
    private RequestQueue queue;

    public NearbyPlaceAPI(Context context){
        queue= RequestQueueSingleton.getInstance(context).getRequestQueue();
    }
    public void generateNearbyPlaceList(Double distance, int limit,Double latitude,Double longitude){
        queue.cancelAll("search");
        if (nameOrCode.length() > 0) {
            StringRequest request = new StringRequest(Request.Method.GET,
                    Api.NearbyPlacesString+distance+"/"+limit+"/",
                    (String response) -> {
                        try {
                            JSONObject data = new JSONObject(response);
                            JSONArray placearray = data.getJSONArray("places");

                            if (placearray.length() == 0) {

                            } else {
                                ArrayList<Place> searchPlaces = JsonUtils.getPlaces(placearray);

                            }

                        } catch (JSONException e) {
                            try{
                                JSONObject data = new JSONObject(response);
                            }
                            catch (JSONException ex){
                                //Toast.makeText(this,"problem formatting data", Toast.LENGTH_SHORT).show();
                                ex.printStackTrace();
                            }
                        }
                    },
                    error ->{
                    }){
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("longitude",String.valueOf(longitude));
                    params.put("latitude",String.valueOf(latitude));
                    return params;
                }
            };

            request.setTag("search");
            queue.add(request);
        }
    }

}
