package barikoi.barikoilocation;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;

import static android.content.res.Resources.getSystem;

/**
 * This Class contains all the api keys needed to hit the server
 */

public class Api {

    public static final String url_base="https://barikoi.xyz/v1/";
    public static final String reverseString =url_base+"api/search/reverse/"+BarikoiAPI.getAccessToken()+"/geocode";
    public static final String autoCompleteString =url_base+"api/search/autocomplete/"+BarikoiAPI.getAccessToken()+"/place";
    public static final String geoCodeString =url_base+"api/search/geocode/"+BarikoiAPI.getAccessToken()+"/place/";
    public static final String nearbyPlacesString =url_base+"api/search/nearby/"+BarikoiAPI.getAccessToken()+"/";
    public static final String nearbyPlacesByTypeString =url_base+"api/search/nearby/category/"+BarikoiAPI.getAccessToken()+"/";
    public static final String rupantorApiString= url_base+"api/search/"+BarikoiAPI.getAccessToken()+"/rupantor/geocode";
}
