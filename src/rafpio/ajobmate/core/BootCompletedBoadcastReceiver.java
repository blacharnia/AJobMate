package rafpio.ajobmate.core;

import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.model.Alarm;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class BootCompletedBoadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.open(arg0);

        Cursor alarms = dbHelper.getAllAlarms();
        if (alarms != null) {
            if (alarms.moveToFirst())
                do {
                    Alarm alarm = dbHelper.getAlarmFromCursor(alarms);
                    Common.setDeviceAlarm(alarm, arg0.getApplicationContext());
                } while (alarms.moveToNext());
            alarms.close();
        }

    }

}
