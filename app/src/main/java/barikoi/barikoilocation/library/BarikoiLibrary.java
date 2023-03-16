package barikoi.barikoilocation.library;

import android.app.Application;

import barikoi.barikoilocation.BarikoiAPI;

public class BarikoiLibrary extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BarikoiAPI.getINSTANCE(this,"MTI6SFpDRkoyN0NFOA==");
    }
}
