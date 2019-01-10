package barikoi.barikoilocation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import barikoi.barikoilocation.BarikoiException.BarikoiAPIConfigurationException;

/**
 * This class is to set the context and access token to hit the api server
 */
public class BarikoiAPI {
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
        validateBarikoiAPI();
        return INSTANCE.accessToken;
    }
    /**
     * Runtime validation of BarikoiAPI Token.
     */
    private static void validateBarikoiAPI() {
        if (INSTANCE == null) {
            throw new BarikoiAPIConfigurationException();
        }
    }
}
