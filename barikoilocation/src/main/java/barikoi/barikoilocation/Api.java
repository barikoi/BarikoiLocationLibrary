package barikoi.barikoilocation;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;

import static android.content.res.Resources.getSystem;

/**
 * This Class contains all the api keys needed to hit the server
 * Created by Sakib on 7/16/2017.
 */

public class Api {

    public static final String url_base="https://barikoi.xyz/v1/";

    public static final String ReverseString=url_base+"api/search/reverse/geocode/"+BarikoiAPI.getAccessToken()+"/place";
    public static final String AutoCompleteString=url_base+"api/search/autocomplete/"+BarikoiAPI.getAccessToken()+"/place";
    public static final String GeoCodeString=url_base+"api/search/geocode/"+BarikoiAPI.getAccessToken()+"/place";
    public static final String NearbyPlacesString=url_base+"api/search/nearby/"+BarikoiAPI.getAccessToken()+"/";

}
