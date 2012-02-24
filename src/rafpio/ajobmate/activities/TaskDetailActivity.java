package rafpio.ajobmate.activities;

import rafpio.ajobmate.R;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.core.EventHandler;
import rafpio.ajobmate.db.DBOfferHandler;
import rafpio.ajobmate.db.DBTaskHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.db.TableHandler;
import rafpio.ajobmate.model.Offer;
import rafpio.ajobmate.model.Task;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TaskDetailActivity extends Activity {
    private TextView description;
    private TextView startTimeTV;
    private TextView endTimeTV;
    private TextView notificationTimeTV;
    private Button archiveButton;
    private TextView offer;
    private Task task;

    private long mRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.task_detail);

        Button editButton = (Button) findViewById(R.id.edit);

        description = (TextView) findViewById(R.id.description);
        startTimeTV = (TextView) findViewById(R.id.startTimeText);
        endTimeTV = (TextView) findViewById(R.id.endTimeText);
        notificationTimeTV = (TextView) findViewById(R.id.notificationTimeText);
        archiveButton = (Button) findViewById(R.id.archive);

        offer = (TextView) findViewById(R.id.offer);

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
            finish();
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

        long startTime = task.getStartTime();
        if (startTime > 0) {
            startTimeTV.setText("Start time:"
                    + Common.getTimeAsString(startTime));
        } else {
            startTimeTV.setText("No start time provided.");
        }

        long endTime = task.getEndTime();
        if (endTime > 0) {
            endTimeTV.setText("End time:" + Common.getTimeAsString(endTime));
        } else {
            endTimeTV.setText("No end time provided.");
        }

        long notificationTime = task.getNotificationTime();
        if (notificationTime > 0) {
            notificationTimeTV.setText("Notification time:"
                    + Common.getTimeAsString(notificationTime));
        } else {
            notificationTimeTV.setText("No notification time provided.");
        }

        String taskOffer = getOfferName(task.getOfferId());
        if (TextUtils.isEmpty(taskOffer)) {
            offer.setText("No related offer set.");
        } else {
            offer.setText("Related offer:" + taskOffer);
        }

        long currentTime = System.currentTimeMillis();

        if (currentTime > startTime) {
            archiveButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        checkInputParams();

        if (mRowId > 0) {
            task = JOffersDbAdapter.getInstance().getTask(mRowId);
            if (task != null) {
                setupControls();
            } else {
                finish();
            }
        } else {
            finish();
        }

        super.onStart();
    }

    // TODO: make it common
    private String getOfferName(long offerId) {
        String offerName = null;
        Offer offer = JOffersDbAdapter.getInstance().getOffer(offerId);
        if (offer != null) {
            offerName = offer.getPosition();
        } else {
            offerName = getString(R.string.none);
        }
        return offerName;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TableHandler.KEY_ROWID, mRowId);
    }

}
