package rafpio.ajobmate.activities;

import java.util.Calendar;

import rafpio.ajobmate.R;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.db.DBTaskHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class DateTimePickerActivity extends Activity {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button confirmButton;
    private Button cancelButton;
    private long startTime;
    private long endTime;
    private long notificationTime;
    private int requestCode;
    private TextView startTimeTV;
    private TextView endTimeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_layout);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        confirmButton = (Button) findViewById(R.id.confirm);
        cancelButton = (Button) findViewById(R.id.cancel);

        startTimeTV = (TextView) findViewById(R.id.start_time);
        endTimeTV = (TextView) findViewById(R.id.end_time);

        confirmButton.setOnClickListener(confirmClickListener);
        cancelButton.setOnClickListener(cancelClickListener);

        init();
    }

    private OnClickListener cancelClickListener = new OnClickListener() {

        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    private OnClickListener confirmClickListener = new OnClickListener() {

        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(datePicker.getYear(), datePicker.getMonth(),
                    datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute(), 0);
            long time = calendar.getTimeInMillis();
            switch (requestCode) {
            case TaskAddEditActivity.START_TIME_REQUEST:
                startTime = time;
                break;
            case TaskAddEditActivity.END_TIME_REQUEST:
                endTime = time;
                break;
            case TaskAddEditActivity.NOTIFICATION_TIME_REQUEST:
                notificationTime = time;
                break;
            }

            Intent intent = new Intent();
            intent.putExtra("start_time", startTime);
            intent.putExtra("end_time", endTime);
            intent.putExtra("notification_time", notificationTime);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    private void init() {
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            startTime = extras.getLong(DBTaskHandler.KEY_START_TIME);
            endTime = extras.getLong(DBTaskHandler.KEY_END_TIME);
            notificationTime = extras
                    .getLong(DBTaskHandler.KEY_NOTIFICATION_TIME);
            long time = 0;

            requestCode = extras.getInt("request_code");
            switch (requestCode) {
            case TaskAddEditActivity.START_TIME_REQUEST:
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                time = startTime;
                break;
            case TaskAddEditActivity.END_TIME_REQUEST:
                if (endTime == 0) {
                    endTime = System.currentTimeMillis();
                }
                time = endTime;
                break;
            case TaskAddEditActivity.NOTIFICATION_TIME_REQUEST:
                if (notificationTime == 0) {
                    notificationTime = System.currentTimeMillis();
                }
                time = notificationTime;
                break;
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

        } else {
            setResult(RESULT_CANCELED);
            finish();
        }

    };

}
