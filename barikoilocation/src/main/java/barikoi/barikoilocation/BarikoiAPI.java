package barikoi.barikoilocation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import barikoi.barikoilocation.BarikoiException.BarikoiAPIConfigurationException;

public class BarikoiAPI {
    private static BarikoiAPI INSTANCE;
    private Context context;
    private String accessToken;

    public static synchronized BarikoiAPI getINSTANCE(@NonNull Context context, @Nullable String accessToken){
       if(INSTANCE==null){
           Context appContext=context.getApplicationContext();
           INSTANCE=new BarikoiAPI(appContext,accessToken);
       }

        return INSTANCE;
    }

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
     * Application context
     *
     * @return the application context
     */
    @NonNull
    public static Context getApplicationContext() {
        validateBarikoiAPI();
        return INSTANCE.context;
    }
    /**
     * Runtime validation of BarikoiAPI creation.
     */
    private static void validateBarikoiAPI() {
        if (INSTANCE == null) {
            throw new BarikoiAPIConfigurationException();
        }
    }
}
