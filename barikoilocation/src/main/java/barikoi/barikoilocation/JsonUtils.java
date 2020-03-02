package barikoi.barikoilocation;

import android.util.Log;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import barikoi.barikoilocation.PlaceModels.NearbyPlace;
import barikoi.barikoilocation.PlaceModels.SearchAutoCompletePlace;
import barikoi.barikoilocation.PlaceModels.GeoCodePlace;
import barikoi.barikoilocation.PlaceModels.ReverseGeoPlace;

/**
 * This class is used to handle all the JSON Data received from server API
 */

public final class JsonUtils {

    /**
     * This checks the json object for place attributes. If the object has the attribute then, this function sets it
     * otherwise sets the attribute as empty string
     * @param placearray is a JSONArray from server
     * @return a arraylist of places
     */
    public static ArrayList<NearbyPlace> getNearbyPlace(JSONArray placearray){
        ArrayList<NearbyPlace> newplaces=new ArrayList<NearbyPlace>();
        try {
            for (int i = 0; i < placearray.length(); i++) {
                JSONObject jsonObject = placearray.getJSONObject(i);

                String id = jsonObject.has("id")? jsonObject.getString("id"):"";
                double distance_within_meters = jsonObject.has("distance_within_meters")?jsonObject.getDouble("distance_within_meters"):0.0;
                String lon = jsonObject.has("longitude")? jsonObject.getString("longitude"):"";
                String lat = jsonObject.has("latitude")?jsonObject.getString("latitude"):"";
                String address =jsonObject.has("Address")? jsonObject.getString("Address"):"";
                String city=jsonObject.has("city")? jsonObject.getString("city"):"";
                String code = jsonObject.has("uCode")?jsonObject.getString("uCode"):"";
                String area=jsonObject.has("area")? jsonObject.getString("area"):"";
                String pType=jsonObject.has("pType")? jsonObject.getString("pType"):"";
                String subType=jsonObject.has("subType")? jsonObject.getString("subType"):"";
                String phoneNumber=jsonObject.has("contact_person_phone")? jsonObject.getString("contact_person_phone"):"";

                NearbyPlace newplace = new NearbyPlace(id,distance_within_meters,lon,lat,address,city, area,pType,subType,code,phoneNumber);
                newplaces.add(newplace);
            }
        }catch (Exception e){
            Log.d("JsonUtils",e.getLocalizedMessage());
        }
        return newplaces;
    }

    /**
     * This checks the json object for place attributes. If the object has the attribute then, this function sets it
     * otherwise sets the attribute as empty string
     * @param placearray is a JSONArray from server
     * @return a arraylist of places
     */
    public static ArrayList<SearchAutoCompletePlace> getSearchAutoCompletePlaces(JSONArray placearray){
        ArrayList<SearchAutoCompletePlace> newplaces=new ArrayList<SearchAutoCompletePlace>();
        try {
            for (int i = 0; i < placearray.length(); i++) {
                JSONObject report = placearray.getJSONObject(i);
                String id = report.has("id")? report.getString("id"):"";
                String address =report.has("address")? report.getString("address"):"";
                String code = report.has("uCode")?report.getString("uCode"):"";
                String area=report.has("area")? report.getString("area"):"";

                SearchAutoCompletePlace newplace = new SearchAutoCompletePlace(id,address, code, area);
                newplaces.add(newplace);
            }
        }catch (Exception e){
            Log.d("JsonUtils",e.getLocalizedMessage());
        }
        return newplaces;
    }

    /**
     * Gets a place from the server
     * @param jsonObject takes a json object and structures the data and return a place
     * @return a structured place
     */
    public static GeoCodePlace getGeoCodePlace(JSONObject jsonObject){
        try{
            String lon = jsonObject.has("longitude")? jsonObject.getString("longitude"):"";
            String lat = jsonObject.has("latitude")?jsonObject.getString("latitude"):"";
            String address =jsonObject.has("address")? jsonObject.getString("address"):jsonObject.has("Address")?jsonObject.getString("Address"):"";
            String code = jsonObject.has("uCode")?jsonObject.getString("uCode"):"";
            String area=jsonObject.has("area")? jsonObject.getString("area"):"";
            String city=jsonObject.has("city")? jsonObject.getString("city"):"";
            String postal=jsonObject.has("postcode")? jsonObject.getString("postcode"):"";
            String pType=jsonObject.has("pType")? jsonObject.getString("pType"):"";
            String subType=jsonObject.has("subType")? jsonObject.getString("subType"):"";
            String route=jsonObject.has("route_description")? jsonObject.getString("route_description"):"";

            JSONArray images=jsonObject.has("images")?  jsonObject.getJSONArray("images"): new JSONArray();

            GeoCodePlace newplace = new GeoCodePlace(address, lon, lat, code, city, area, postal, pType, subType);

            if (images.length() > 0) {
                JSONObject image = images.getJSONObject(0);
                newplace.setImglink(image.getString("imageLink"));
            }
            if(route.length()>0 && !route.equals("null")){
                newplace.setRoute(route);
            }
            Log.d("JsonUtils",""+newplace.getAddress());
            return newplace;

        }catch (JSONException e){
            Log.d("JsonUtils",e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Gets a place from the server
     * @param jsonObject takes a json object and structures the data and return a place
     * @return a structured place
     */
    public static ReverseGeoPlace getReverseGeoPlace(JSONObject jsonObject){
        try{
            double distance_within_meters = jsonObject.has("distance_within_meters")?jsonObject.getDouble("distance_within_meters"):0.0;
            String id = jsonObject.has("id")?jsonObject.getString("id"):"";
            String address =jsonObject.has("address")? jsonObject.getString("address"):"";
            String area=jsonObject.has("area")? jsonObject.getString("area"):"";
            String city=jsonObject.has("city")? jsonObject.getString("city"):"";

            ReverseGeoPlace newplace = new ReverseGeoPlace(id,address, area, city, distance_within_meters);

            Log.d("JsonUtils",""+newplace.getAddress());
            return newplace;

        }catch (JSONException e){
            Log.d("JsonUtils",e.getLocalizedMessage());
            return null;
        }
    }
    /**
     * Handles error response from the server
     * @param error is a VolleyError occurs  mostly in network responses
     *  return a string about the error
     */
    public static String  handleResponse(VolleyError error){
        if(error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError){
           return "Could not connect";
        }
        else if(error instanceof AuthFailureError){
            return "Authentication Problem";
        }
        else if(error instanceof ServerError || error instanceof ParseError){
           return "Problem or change in server, please wait and try again";

        }
        else{
           return "Unknown Error Occured";
        }
    }

    /**
     * This method is used to track the errors if theres an JSON exception.
     * @param TAG is class tag
     * @param response is the JSON response
     * @return the error exception message
     */
    public static String logError(String TAG, String response){
        String error="";
        JSONObject data;
        try {
            data = new JSONObject(response);
            if(data.has("message")){
                error=data.getString("message");
                Log.d(TAG,error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return error;
    }
    public static void logResponse(VolleyError error){
        String body = "";
        //get status code here
        //get response body and parse with appropriate encoding
        if(error.networkResponse!=null) {
            try {
                body = new String(error.networkResponse.data,"UTF-8");
                Log.d("error response ",body);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
