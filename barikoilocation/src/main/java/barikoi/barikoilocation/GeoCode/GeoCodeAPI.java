package barikoi.barikoilocation.GeoCode;


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
import barikoi.barikoilocation.SearchAutoComplete.SearchAutoCompleteListener;

public class GeoCodeAPI {
    private RequestQueue queue;
    PlaceGeoCodeListener placeGeoCodeListener;
    public GeoCodeAPI(Context context, PlaceGeoCodeListener placeGeoCodeListener){
        queue= RequestQueueSingleton.getInstance(context).getRequestQueue();
        this.placeGeoCodeListener=placeGeoCodeListener;
    }

    /**
     * @param nameOrCode is the place searching for in the app
     *  requests the server to get info about the current position
     */
    public void generatelist(final String nameOrCode) {
        queue.cancelAll("search");
        if (nameOrCode.length() > 0) {
            StringRequest request = new StringRequest(Request.Method.GET,
                    Api.GeoCodeString+nameOrCode,
                    (String response) -> {
                        try {
                            JSONObject data = new JSONObject(response);
                            JSONArray placearray = data.getJSONArray("places");

                            if (placearray.length() == 0) {
                                this.placeGeoCodeListener.OnFailure("Place Not Found!");
                            } else if(placearray.length() == 1){
                                ArrayList<Place> searchPlaces = JsonUtils.getPlaces(placearray);
                                this.placeGeoCodeListener.GeoCodePlace(searchPlaces.get(0));
                            }

                        } catch (JSONException e) {
                            try{
                                JSONObject data = new JSONObject(response);
                            }
                            catch (JSONException ex){
                                this.placeGeoCodeListener.OnFailure(ex.toString());
                                ex.printStackTrace();
                            }
                        }
                    },
                    error ->{
                        this.placeGeoCodeListener.OnFailure(JsonUtils.handleResponse(error));
                    }){
            };
            request.setTag("search");
            queue.add(request);
        }
    }
}
