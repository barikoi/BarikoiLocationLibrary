package barikoi.barikoilocation;

import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import barikoi.barikoilocation.BarikoiException.BarikoiAPIConfigurationException;

/**
 * This class is to set the context and access token to hit the api server
 */
public class BarikoiAPI {
    private static final String TAG="BarikoiAPI";
    private static final String error="\nUsing BarikoiAPI requires calling BarikoiAPI.getInstance(Context context, String accessToken) before "
            + "inflating or creating the view. The access token parameter is required when using a BarikoiAPI service."
            + "\nPlease see https://barikoi.com/#/business/dash to learn how to create one.";
    private static BarikoiAPI INSTANCE;
    private Context context;
    private String accessToken;

    /**
     * This method sets the application context and access token
     * @param context is the application context
     * @param accessToken is the specific access token assigned to the user
     * @return a BarikoiAPI objet
     */
    public static synchronized BarikoiAPI getINSTANCE(@NonNull Context context, @Nullable String accessToken){
       if(INSTANCE==null){
           Context appContext=context.getApplicationContext();
           INSTANCE=new BarikoiAPI(appContext,accessToken);
       }
        return INSTANCE;
    }

    /**
     * @param context is the application context
     * @param accessToken is the specific access token assigned to the user
     */
    BarikoiAPI(@NonNull Context context, @Nullable String accessToken){
        this.context=context;
        this.accessToken = accessToken;
    }

    /**
     * Access token for this application.
     *
     * @return BarikoiAPI access token
     */
    @Nullable
    public static String getAccessToken() {
        if(validateBarikoiAPI()){
            return INSTANCE.accessToken;
        }
        else{ return "";}
    }
    /**
     * Runtime validation of BarikoiAPI Token.
     */
    private static boolean validateBarikoiAPI() {
        if (INSTANCE == null) {
            Log.e(TAG,error);
            return false;
        }
        else{return true;}
    }
}
