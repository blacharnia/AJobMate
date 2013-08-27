package rafpio.ajobmate.core;

import rafpio.ajobmate.db.JOffersDbAdapter;
import android.app.Application;
import android.content.Context;

//FIXME: rather store time in milis
public class JobmateApplication extends Application {

    public static JobmateApplication getInstance() {
	return appInstance;
    }

    private static JobmateApplication appInstance;

    @Override
    public void onCreate() {
	super.onCreate();

	appInstance = this;
	Context appContext = getApplicationContext();
	EventHandler.getInstance().init(appContext);
	JOffersDbAdapter.getInstance().open(appContext);
    }

}
