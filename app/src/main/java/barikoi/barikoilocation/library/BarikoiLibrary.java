package barikoi.barikoilocation.library;

import android.app.Application;

import barikoi.barikoilocation.BarikoiAPI;

public class BarikoiLibrary extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BarikoiAPI.getINSTANCE(this,"BARIKOI_API_KEY_HERE");
    }
}
