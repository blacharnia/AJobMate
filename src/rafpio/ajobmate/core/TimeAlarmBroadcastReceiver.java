package rafpio.ajobmate.core;

import rafpio.ajobmate.activities.TaskDetailActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeAlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
	MyNotification.sendNotification(arg0, TaskDetailActivity.class);
	// Log.d("RP", "onReceive");
	// Intent intent = new Intent(arg0, TaskDetailActivity.class);
	// intent.putExtra(DBTaskHandler.KEY_ROWID,
	// arg1.getLongExtra(TableHandler.KEY_ROWID, -1));
	// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// arg0.startActivity(intent);
    }

}
