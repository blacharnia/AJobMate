package rafpio.ajobmate.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class MyNotification {

    public static void sendNotification(Context context, Class activityClass) {

	NotificationManager nm = (NotificationManager) context
		.getSystemService(Context.NOTIFICATION_SERVICE);
	CharSequence from = "Messenger";
	StringBuilder message = new StringBuilder("Notification message:");

	Intent startIntent = new Intent(context.getApplicationContext(),
		activityClass);

	// intent.putExtra(DBTaskHandler.KEY_ROWID,
	// arg1.getLongExtra(TableHandler.KEY_ROWID, -1));

	startIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
		startIntent, 0);
	Notification notif = new Notification(android.R.drawable.btn_plus,
		"Notification ticker text", System.currentTimeMillis());
	notif.flags |= Notification.FLAG_AUTO_CANCEL;
	notif.setLatestEventInfo(context, from, message, contentIntent);
	nm.notify(1, notif);

    }
}
