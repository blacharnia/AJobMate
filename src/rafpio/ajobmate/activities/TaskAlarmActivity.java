package rafpio.ajobmate.activities;

import rafpio.ajobmate.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class TaskAlarmActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("RP", "onCreate");
        setContentView(R.layout.task_alarm);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

}
