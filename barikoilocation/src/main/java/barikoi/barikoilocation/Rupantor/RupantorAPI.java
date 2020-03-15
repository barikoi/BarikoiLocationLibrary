package barikoi.barikoilocation.Rupantor;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import barikoi.barikoilocation.Api;
import barikoi.barikoilocation.JsonUtils;
import barikoi.barikoilocation.PlaceModels.GeoCodePlace;
import barikoi.barikoilocation.RequestQueueSingleton;

public class RupantorAPI {
    private static final String TAG="RupantorAPI";
    private Context context;
    private String rawAddress;

    /**
     * This constructor sets the context of application and a PlaceGeoCodeAPI listener
     */
    private RupantorAPI(Context context, String rawAddress){
        this.context=context;
        this.rawAddress = rawAddress;
    }

    /**
     * This method builds the Builder of this class
     * @param context is the application context
     * @return a new instance of Builder class
     */
    public static Builder builder(Context context){
        return new Builder(context);
    }
    /**
     * This function makes network call with the api to get the place details
     *  requests the server to get info about the current position
     */
    public void getRupantorPlace(RupantorPlaceListener rupantorPlaceListener) {
        RequestQueue queue= RequestQueueSingleton.getInstance(this.context).getRequestQueue();
        queue.cancelAll("search");
        if (this.rawAddress.length() > 0) {
            StringRequest request = new StringRequest(Request.Method.POST,
                    Api.rupantorApiString,
                    (String response) -> {
                        try {
                            JSONObject data = new JSONObject(response);
                            if(data.has("status")) {
                                if (data.getInt("status") == 200) {
                                    if(data.get("geocoded_address").equals("null")){
                                        rupantorPlaceListener.onFailure("Not found");
                                    }else {
                                        GeoCodePlace place = JsonUtils.getGeoCodePlace(data.getJSONObject("geocoded_address"));
                                        String fixedAddress = data.getString("fixed_address");
                                        String score = data.getString("confidence_score_percentage");
                                        boolean iscompleteAddress = data.getString("address_status").equals("complete");
                                        rupantorPlaceListener.onRupantorPlaceReceived(place, fixedAddress, iscompleteAddress);
                                        rupantorPlaceListener.onRupantorPlaceReceivedWithScore(place,fixedAddress, iscompleteAddress, score);
                                    }
                                }else{
                                    rupantorPlaceListener.onFailure(data.getString("message"));
                                }
                            }else{
                                rupantorPlaceListener.onFailure(data.getString("message"));
                            }

                        } catch (JSONException e) {
                            rupantorPlaceListener.onFailure(JsonUtils.logError(TAG,response));
                        }
                    },
                    error ->{
                        Log.d(TAG,JsonUtils.handleResponse(error));
                        rupantorPlaceListener.onFailure(JsonUtils.handleResponse(error));
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> params=new HashMap<>();
                    params.put("q",rawAddress);
                    return params;
                }
            };
            request.setTag("search");
            queue.add(request);
        }
        else{
            Log.d(TAG,"input address not given");
            rupantorPlaceListener.onFailure("input address not given");
        }
    }
    /**
     * This builder is used to create a new request to the GeoCode API
     * At a bare minimum, your request
     * must include an application context, a Name or Code of your desired place. All other fields can be left alone
     * inorder to use the default behaviour of the API.
     */
    public static final class Builder{
        private Context context;
        private String rawAddress="";

        /**
         * Private constructor for initializing the raw GeoCode.Builder
         */
        private Builder(Context context){ this.context=context;}

        /**
         * The name or code the user want to search with
         * @param idOrCode is the input string of place name or code for searching place
         * @return
         */
        public Builder rawAddress(String idOrCode){
            this.rawAddress =idOrCode;
            return this;
        }

        /**
         * This uses the provided parameters set using the {@link Builder} and adds the required
         * settings for GeoCode to work correctly.
         *
         * @return a new instance of GeoCode
         */
        public RupantorAPI build(){
            RupantorAPI rupantorAPI=new RupantorAPI(this.context,this.rawAddress);
            return rupantorAPI;
        }
    }
}
