package rafpio.ajobmate.activities;

import java.util.Observable;
import java.util.Observer;

import rafpio.ajobmate.R;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.core.EventHandler;
import rafpio.ajobmate.db.DBOfferHandler;
import rafpio.ajobmate.db.DBTaskHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.db.TableHandler;
import rafpio.ajobmate.model.Task;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TaskDetailActivity extends Activity implements Observer {
    private TextView description;
    private TextView startTimeTV;
    private TextView endTimeTV;
    private TextView notificationTimeTV;
    private Button archiveButton;
    private TextView offerTV;
    private Task task;

    private long mRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.task_detail);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        Button editButton = (Button) findViewById(R.id.edit);

        description = (TextView) findViewById(R.id.description);
        startTimeTV = (TextView) findViewById(R.id.startTimeText);
        endTimeTV = (TextView) findViewById(R.id.endTimeText);
        notificationTimeTV = (TextView) findViewById(R.id.notificationTimeText);
        archiveButton = (Button) findViewById(R.id.archive);

        offerTV = (TextView) findViewById(R.id.offer);

        editButton.setOnClickListener(mEditOnClickListener);
        archiveButton.setOnClickListener(mArchiveOnClickListener);
    }

    private View.OnClickListener mEditOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            Intent intent = new Intent(TaskDetailActivity.this,
                    TaskAddEditActivity.class);
            intent.putExtra(DBOfferHandler.KEY_ROWID, mRowId);
            startActivityForResult(intent, Common.ACTIVITY_EDIT);
        }
    };

    private View.OnClickListener mArchiveOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            EventHandler.getInstance().archiveTask(task.getId());
        }
    };

    private void checkInputParams() {
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mRowId = extras.getLong(DBTaskHandler.KEY_ROWID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            setupControls();
        }

    }

    private void setupControls() {

        description.setText(task.getDescription());

        if (task.isStartTimeSet()) {
            startTimeTV.setText("Start time:"
                    + Common.getTimeAsString(task.getStartTime()));
        } else {
            startTimeTV.setText("No start time provided");
        }

        if (task.isEndTimeSet()) {
            endTimeTV.setText("End time:"
                    + Common.getTimeAsString(task.getEndTime()));
        } else {
            endTimeTV.setText("No end time provided");
        }

        if (task.isAlarmTimeSet()) {
            notificationTimeTV.setText("Notification time:"
                    + Common.getTimeAsString(task.getNotificationTime()));
        } else {
            notificationTimeTV.setText("No notification time provided");
        }

        String taskOffer = JOffersDbAdapter.getInstance().getOfferName(
                task.getOfferId());
        if (TextUtils.isEmpty(taskOffer)) {
            offerTV.setText("No related offer set");
        } else {
            offerTV.setText("Related offer:" + taskOffer);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkInputParams();

        if (mRowId > 0) {
            JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
            task = dbHelper.getTask(mRowId);
            if (task != null) {
                setupControls();
            } else {
                finish();
            }
        } else {
            finish();
        }

        EventHandler.getInstance().addObserver(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TableHandler.KEY_ROWID, mRowId);
    }

    public void update(Observable observable, Object data) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFinishing()) {
            EventHandler.getInstance().deleteObserver(this);
        }
    }

}
