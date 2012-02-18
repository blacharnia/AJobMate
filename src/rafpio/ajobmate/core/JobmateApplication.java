package rafpio.ajobmate.core;

import rafpio.ajobmate.db.JOffersDbAdapter;
import android.app.Application;
import android.app.PendingIntent;

public class JobmateApplication extends Application {

    public static JobmateApplication getInstance() {
        return appInstance;
    }

    private PendingIntent pendingIntent;
    
    private static JobmateApplication appInstance;

    @Override
    public void onCreate() {
        appInstance = this;
        
        
        super.onCreate();
    }
 
    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

}
