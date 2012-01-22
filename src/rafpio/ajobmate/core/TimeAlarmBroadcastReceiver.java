package rafpio.ajobmate.core;

import rafpio.ajobmate.R;
import rafpio.ajobmate.activities.TaskDetailActivity;
import rafpio.ajobmate.db.DBTaskHandler;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class TimeAlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        String taskName = arg1.getStringExtra("taskName");
        long taskStartTime = arg1.getLongExtra("taskStartTime", 0);
        long taskId = arg1.getLongExtra("taskId", 0);
        
        if (TextUtils.isEmpty(taskName)) {
            return;
        }
        NotificationManager nm = (NotificationManager) arg0
                .getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence from = "AJobmate";
        StringBuilder message = new StringBuilder("Task ");
        message.append(taskName);
        message.append(" pending ");
        if(taskStartTime > 0){
            message.append("at: ");
            message.append(String.valueOf(taskStartTime));
        }

        Intent startIntent = new Intent(arg0.getApplicationContext(), TaskDetailActivity.class);
        startIntent.putExtra(DBTaskHandler.KEY_ROWID, taskId);
        
        PendingIntent contentIntent = PendingIntent.getActivity(arg0, 0,
                startIntent, 0);
        Notification notif = new Notification(R.drawable.icon,
                "Task notification from AJobMate...",
                System.currentTimeMillis());
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.setLatestEventInfo(arg0, from, message, contentIntent);
        nm.notify(1, notif);
        EventHandler.getInstance().resetTaskNotification(taskId);
    }

}
