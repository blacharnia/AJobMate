package rafpio.ajobmate.activities;

import java.util.Calendar;

import rafpio.ajobmate.R;
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
    private TextView captionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_layout);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        confirmButton = (Button) findViewById(R.id.confirm);
        cancelButton = (Button) findViewById(R.id.cancel);
        captionTV = (TextView) findViewById(R.id.caption);

        confirmButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute(), 0);

                Intent intent = new Intent();
                // TODO: externalize "time"
                setResult(RESULT_OK,
                        intent.putExtra("time", calendar.getTimeInMillis()));
                finish();
            }
        });

        cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        init();
    }

    private void init() {
        //
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            long time = extras.getLong("time");
            if (time != 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
                timePicker.setCurrentHour(calendar.get(Calendar.HOUR));
                datePicker.updateDate(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            }

            String caption = extras.getString("caption");
            if (caption != null) {
                captionTV.setText("Select the " + caption);
            }

        } else {
            setResult(RESULT_CANCELED);
            finish();
        }

    };

}
