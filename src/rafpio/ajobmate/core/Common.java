package rafpio.ajobmate.core;

import java.util.List;

import rafpio.ajobmate.db.DBTaskHandler;
import rafpio.ajobmate.location.GingerbreadLastLocationFinder;
import rafpio.ajobmate.location.ILastLocationFinder;
import rafpio.ajobmate.location.LegacyLastLocationFinder;
import rafpio.ajobmate.model.Alarm;
import rafpio.ajobmate.model.Offer;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;

public class Common {

    private static final String PLAIN_TEXT = "plain/text";
    public static final int LOADING_DIALOG = 0;
    public static final int ACTIVITY_EDIT = 1;
    private static final String DATE_FORMAT = "dd-MMM-yyyy kk:mm";

    private static final boolean SUPPORTS_GINGERBREAD = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD;

    public static String getTimeAsString(long timeInMilis) {
	if (timeInMilis == 0) {
	    return "Date unknown";
	} else {
	    return (String) DateFormat.format(DATE_FORMAT, timeInMilis);
	}
    }

    public static void sendEmail(String email, Context context) {
	Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
	String aEmailList[] = { email };
	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
	emailIntent.setType(PLAIN_TEXT);
	context.startActivity(emailIntent);
    }

    public static void startCalling(String phoneNumber, Activity parentActivity) {
	parentActivity.startActivity(new Intent(Intent.ACTION_CALL, Uri
		.parse("tel:" + phoneNumber)));
    }

    public static int getOfferIndexById(List<?> list, long id) {
	int ret = -1;
	int offersCnt = list.size();

	for (int i = 0; i < offersCnt; i++) {
	    long offerId = ((Offer) list.get(i)).getId();
	    if (id == offerId) {
		ret = i;
	    }
	}
	return ret;
    }

    public static ILastLocationFinder getLastLocationFinder(Context context) {
	return SUPPORTS_GINGERBREAD ? new GingerbreadLastLocationFinder(context)
		: new LegacyLastLocationFinder(context);
    }

    public static String getPackageName() {
	return "rafpio.ajobmate";
    }

    public static void setDeviceAlarm(Alarm alarm, Context context) {
	PendingIntent pendingIntent = getAlarmPendingIntent(alarm, context);
	AlarmManager alarmManager = (AlarmManager) context
		.getSystemService(Context.ALARM_SERVICE);
	alarmManager.cancel(pendingIntent);
	alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getAlarmTime(),
		pendingIntent);

    }

    public static PendingIntent getAlarmPendingIntent(Alarm alarm,
	    Context context) {
	Intent intent = new Intent(context, TimeAlarmBroadcastReceiver.class);
	intent.putExtra(DBTaskHandler.KEY_ROWID, alarm.getTaskId());

	PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
		(int) alarm.getTaskId(), intent, PendingIntent.FLAG_ONE_SHOT
			| PendingIntent.FLAG_UPDATE_CURRENT);
	return pendingIntent;
    }
}
