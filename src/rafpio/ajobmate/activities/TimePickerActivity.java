package rafpio.ajobmate.activities;

import java.util.Calendar;

import rafpio.ajobmate.R;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.core.DialogManager;
import rafpio.ajobmate.db.DBTaskHandler;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerActivity extends Activity {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button confirmButton;
    private Button cancelButton;
    private long startTime;
    private long endTime;
    private long notificationTime;
    private TextView startTimeTV;
    private TextView endTimeTV;
    private Button setUnSetButton;
    private boolean setTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_time_layout);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        confirmButton = (Button) findViewById(R.id.confirm);
        cancelButton = (Button) findViewById(R.id.cancel);
        setUnSetButton = (Button) findViewById(R.id.set_unset_btn);

        startTimeTV = (TextView) findViewById(R.id.start_time);
        endTimeTV = (TextView) findViewById(R.id.end_time);

        confirmButton.setOnClickListener(confirmClickListener);
        cancelButton.setOnClickListener(cancelClickListener);
        setUnSetButton.setOnClickListener(setUnsetAlarmClickListener);

        init();
    }

    private OnClickListener cancelClickListener = new OnClickListener() {

        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    private OnClickListener setUnsetAlarmClickListener = new OnClickListener() {

        public void onClick(View v) {
            setTime = !setTime;
            datePicker.setEnabled(setTime);
            timePicker.setEnabled(setTime);
            String label = setTime ? getString(R.string.do_not_set)
                    : getString(R.string.set);
            setUnSetButton.setText(label);
        }
    };

    private OnClickListener confirmClickListener = new OnClickListener() {

        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(datePicker.getYear(), datePicker.getMonth(),
                    datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute(), 0);
            long time = calendar.getTimeInMillis();

            if (time <= System.currentTimeMillis()) {
                showDialog(DialogManager.BAD_TIME_DIALOG);
                return;
            }

            Intent intent = new Intent();
            intent.putExtra("time_set", setTime);
            // Log.d("RP", "onClick" + setTime);

            if (setTime) {
                String key = null;
                switch (requestCode) {
                case TaskAddEditActivity.START_TIME_REQUEST:
                    key = "start_time";
                    startTime = time;
                    break;
                case TaskAddEditActivity.END_TIME_REQUEST:
                    key = "end_time";
                    endTime = time;
                    break;
                case TaskAddEditActivity.ALARM_TIME_REQUEST:
                    key = "notification_time";
                    notificationTime = time;
                    break;
                default:
                    break;
                }
                if (key != null) {
                    intent.putExtra(key, time);
                }
            }

            setResult(RESULT_OK, intent);
            finish();
        }
    };
    private int requestCode;

    @Override
    protected Dialog onCreateDialog(int id) {
        return DialogManager.getInstance().getDialog(this,
                DialogManager.BAD_TIME_DIALOG, null);
    }

    private void init() {
        setTime = true;
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            requestCode = extras.getInt("request_code");
            startTime = extras.getLong(DBTaskHandler.KEY_START_TIME);
            endTime = extras.getLong(DBTaskHandler.KEY_END_TIME);
            notificationTime = extras
                    .getLong(DBTaskHandler.KEY_NOTIFICATION_TIME);

            long time = 0;

            switch (requestCode) {
            case TaskAddEditActivity.START_TIME_REQUEST:
                time = startTime;
                break;
            case TaskAddEditActivity.END_TIME_REQUEST:
                time = endTime;
                break;
            case TaskAddEditActivity.ALARM_TIME_REQUEST:
                time = notificationTime;
                break;
            default:
                break;
            }

            setUnSetButton.setVisibility(View.VISIBLE);
            if (time == 0) {
                time = System.currentTimeMillis();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
            timePicker.setCurrentHour(calendar.get(Calendar.HOUR));
            datePicker.updateDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            startTimeTV.setText("Start time: "
                    + Common.getTimeAsString(startTime));
            endTimeTV.setText("End time: " + Common.getTimeAsString(endTime));

        }

    };

}
