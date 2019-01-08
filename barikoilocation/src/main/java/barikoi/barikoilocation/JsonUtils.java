package barikoi.barikoilocation;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


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
    public static ArrayList<Place> getPlaces(JSONArray placearray){
        ArrayList<Place> newplaces=new ArrayList<Place>();
        try {
            for (int i = 0; i < placearray.length(); i++) {
                JSONObject report = placearray.getJSONObject(i);
                String lon = report.has("longitude")? report.getString("longitude"):"";
                String lat = report.has("latitude")?report.getString("latitude"):"";
                String address =report.has("Address")? report.getString("Address"):"";
                String code = report.has("uCode")?report.getString("uCode"):"";
                String area=report.has("area")? report.getString("area"):"";
                String city=report.has("city")? report.getString("city"):"";
                String postal=report.has("postCode")? report.getString("postCode"):"";
                String pType=report.has("pType")? report.getString("pType"):"";
                String subType=report.has("subType")? report.getString("subType"):"";
                String route=report.has("route_description")? report.getString("route_description"):"";
                String ward=report.has("ward")? report.getString("ward"):"";
                String zone=report.has("zone")? report.getString("zone"):"";
                String phoneNumber=report.has("contact_person_phone")? report.getString("contact_person_phone"):"";
                JSONArray images=report.has("images")?  report.getJSONArray("images"): new JSONArray();

                Place newplace = new Place(address, lon, lat, code, city, area, postal, pType, subType,phoneNumber);

                if(ward.length()>0 && !ward.equals("null")){
                    newplace.setWard(ward);
                }
                if(zone.length()>0 && !zone.equals("null")){
                    newplace.setZone(zone);
                }

                if (images.length() > 0) {
                    JSONObject image = images.getJSONObject(0);
                    newplace.setImglink(image.getString("imageLink"));
                }
                if(route.length()>0 && !route.equals("null")){
                    newplace.setRoute(route);
                }
                if(report.has("distance")){
                    String distancestring=report.getString("distance_in_meters");
                    newplace.setDistance(Float.parseFloat(distancestring));
                }
                if(phoneNumber.length()>4){
                    newplace.setPhoneNumber(phoneNumber);
                }
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
    public static Place getPlace(JSONObject jsonObject){
        try{
            String lon = jsonObject.has("longitude")? jsonObject.getString("longitude"):"";
            String lat = jsonObject.has("latitude")?jsonObject.getString("latitude"):"";
            String address =jsonObject.has("Address")? jsonObject.getString("Address"):"";
            String code = jsonObject.has("uCode")?jsonObject.getString("uCode"):"";
            String area=jsonObject.has("area")? jsonObject.getString("area"):"";
            String city=jsonObject.has("city")? jsonObject.getString("city"):"";
            String postal=jsonObject.has("postCode")? jsonObject.getString("postCode"):"";
            String pType=jsonObject.has("pType")? jsonObject.getString("pType"):"";
            String subType=jsonObject.has("subType")? jsonObject.getString("subType"):"";
            String route=jsonObject.has("route_description")? jsonObject.getString("route_description"):"";
            String ward=jsonObject.has("ward")? jsonObject.getString("ward"):"";
            String zone=jsonObject.has("zone")? jsonObject.getString("zone"):"";
            String phoneNumber=jsonObject.has("contact_person_phone")? jsonObject.getString("contact_person_phone"):"";
            JSONArray images=jsonObject.has("images")?  jsonObject.getJSONArray("images"): new JSONArray();

            Place newplace = new Place(address, lon, lat, code, city, area, postal, pType, subType,phoneNumber);

            if(ward.length()>0 && !ward.equals("null")){
                newplace.setWard(ward);
            }
            if(zone.length()>0 && !zone.equals("null")){
                newplace.setZone(zone);
            }

            if (images.length() > 0) {
                JSONObject image = images.getJSONObject(0);
                newplace.setImglink(image.getString("imageLink"));
            }
            if(route.length()>0 && !route.equals("null")){
                newplace.setRoute(route);
            }
            if(jsonObject.has("distance")){
                String distancestring=jsonObject.getString("distance_in_meters");
                newplace.setDistance(Float.parseFloat(distancestring));
            }
            if(phoneNumber.length()>4){
                newplace.setPhoneNumber(phoneNumber);
            }
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
