package rafpio.ajobmate.core;

import android.app.Application;

public class JobmateApplication extends Application {

    public static JobmateApplication getInstance() {
        return appInstance;
    }

    private static JobmateApplication appInstance;

    @Override
    public void onCreate() {
        appInstance = this;
        super.onCreate();
        
        EventHandler.getInstance().init(getApplicationContext());
    }

}
