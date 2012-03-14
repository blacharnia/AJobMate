package rafpio.ajobmate.core;

import rafpio.ajobmate.activities.TaskDetailActivity;
import rafpio.ajobmate.db.DBTaskHandler;
import rafpio.ajobmate.db.TableHandler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;
import android.util.Log;


public class TimeAlarmBroadcastReceiver extends BroadcastReceiver {

    public static WakeLock lock;

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        //Log.d("RP", "onReceive");
        Intent intent = new Intent(arg0, TaskDetailActivity.class);
        intent.putExtra(DBTaskHandler.KEY_ROWID, 
                arg1.getLongExtra(TableHandler.KEY_ROWID, -1));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        arg0.startActivity(intent);
    }
    
//FIXME: move notification and wakelock code to the common package
//    private static WakeLock sCpuWakeLock;
//
//    static void acquireCpuWakeLock(Context context) {
//        if (sCpuWakeLock != null) {
//            return;
//        }
//
//        PowerManager pm =
//                (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//
//        sCpuWakeLock = pm.newWakeLock(
//                PowerManager.FULL_WAKE_LOCK |
//                PowerManager.ACQUIRE_CAUSES_WAKEUP |
//                PowerManager.ON_AFTER_RELEASE, "rafpio.jobmate");
//        
//        sCpuWakeLock.acquire();
//        
//    }
//
//    static void releaseCpuLock() {
//        if (sCpuWakeLock != null) {
//            sCpuWakeLock.release();
//            sCpuWakeLock = null;
//        }
//    }

    
  //FIXME: send entire task object to the receiver
    //String taskName = arg1.getStringExtra("taskName");
    //long taskStartTime = arg1.getLongExtra("taskStartTime", 0);
    //long taskId = arg1.getLongExtra("taskId", 0);

//    if (TextUtils.isEmpty(taskName)) {
//        return;
//    }
//    intent.putExtra(DBTaskHandler.KEY_DESCRIPTION, task.getDescription());
//    intent.putExtra(DBTaskHandler.KEY_START_TIME, task.getStartTime());
//    intent.putExtra(DBTaskHandler.KEY_END_TIME, task.getEndTime());
//    intent.putExtra(DBTaskHandler.KEY_ROWID, task.getId());
    
//    NotificationManager nm = (NotificationManager) arg0
//            .getSystemService(Context.NOTIFICATION_SERVICE);
//    CharSequence from = "AJobmate";
//    StringBuilder message = new StringBuilder("Task ");
//    message.append(" pending ");
//    if (taskStartTime > 0) {
//        message.append("at: ");
//        message.append(String.valueOf(taskStartTime));
//    }

//    Intent startIntent = new Intent(arg0.getApplicationContext(),
//            TaskAddEditActivity.class);
//
//    startIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//    startIntent.putExtra(DBTaskHandler.KEY_ROWID, taskId);

//    PendingIntent contentIntent = PendingIntent.getActivity(arg0, 0,
//            startIntent, 0);
//    Notification notif = new Notification(R.drawable.icon,
//            "Task notification from AJobMate...",
//            System.currentTimeMillis());
//    notif.flags |= Notification.FLAG_AUTO_CANCEL;
//    notif.setLatestEventInfo(arg0, from, message, contentIntent);
  //nm.notify(1, notif);
    //JOffersDbAdapter.getInstance().open(arg0.getApplicationContext());
    //EventHandler.getInstance().resetTaskNotification(taskId);
    
}
