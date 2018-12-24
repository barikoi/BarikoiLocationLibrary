package barikoi.barikoilocation.SearchAutoComplete;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import barikoi.barikoilocation.R;
import barikoi.barikoilocation.RequestQueueSingleton;

public class SearchAutoCompleteAPI {
    private RequestQueue queue;
    SearchAutoCompleteListener searchAutoCompleteListener;

    public SearchAutoCompleteAPI(Context context,SearchAutoCompleteListener searchAutoCompleteListener){
        queue= RequestQueueSingleton.getInstance(context).getRequestQueue();
        this.searchAutoCompleteListener=searchAutoCompleteListener;
    }
    /**
     * @param nameOrCode is the place searching for in the app
     *  requests the server to get info about the current position
     */
    public void generatelist(final String nameOrCode) {

        queue.cancelAll("search");
        if (nameOrCode.length() > 0) {
            StringRequest request = new StringRequest(Request.Method.GET,
                    Api.AutoCompleteString+"?q="+nameOrCode,
                    (String response) -> {
                        try {
                            JSONObject data = new JSONObject(response);
                            JSONArray placearray = data.getJSONArray("places");

                            Bundle b = new Bundle();
                            b.putString("query", nameOrCode);
                            b.putInt("result_numbers", placearray.length());
                            if (placearray.length() == 0) {
                                //Toast.makeText(this,"google", Toast.LENGTH_SHORT).show();
                                //getPlaceFromAutocomplete(nameOrCode);
                            } else {
                                ArrayList<Place> searchPlaces = JsonUtils.getPlaces(placearray);
                                this.searchAutoCompleteListener.OnPlaceListRecieved(searchPlaces);
                            }

                        } catch (JSONException e) {
                            try{
                                JSONObject data = new JSONObject(response);
                                //Toast.makeText(SearchPlaceActivity.this,data.getJSONObject("places").getString("Message"), Toast.LENGTH_SHORT).show();
                            }
                            catch (JSONException ex){
                                //Toast.makeText(this,"problem formatting data", Toast.LENGTH_SHORT).show();
                                ex.printStackTrace();
                            }

                        }
                    },
                    error -> {
                        JsonUtils.logResponse(error);
                        Log.d("search params",nameOrCode);

                    }) {
            };
            request.setTag("search");
            queue.add(request);
        }
    }
}
