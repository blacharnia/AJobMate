package rafpio.ajobmate.activities;

import java.util.List;

import rafpio.ajobmate.R;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.core.DialogManager;
import rafpio.ajobmate.core.JobmateApplication;
import rafpio.ajobmate.core.TimeAlarmBroadcastReceiver;
import rafpio.ajobmate.db.DBOfferHandler;
import rafpio.ajobmate.db.DBTaskHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.db.TableHandler;
import rafpio.ajobmate.model.Offer;
import rafpio.ajobmate.model.Task;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TaskAddEditActivity extends Activity {
    private EditText descriptionEdit;
    private Button startTimeButton;
    private Button endTimeButton;
    private Button notificationTimeButton;
    private Spinner offerSpinner;
    private Task task;

    private long mRowId;
    private long mOfferId;
    private List<Object> offers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.task_edit);

        Button confirmButton = (Button) findViewById(R.id.confirm);
        Button cancelButton = (Button) findViewById(R.id.cancel);

        descriptionEdit = (EditText) findViewById(R.id.description);
        startTimeButton = (Button) findViewById(R.id.startTime);
        endTimeButton = (Button) findViewById(R.id.endTime);
        notificationTimeButton = (Button) findViewById(R.id.notificationTime);
        offerSpinner = (Spinner) findViewById(R.id.offer);   

        // button event listeners
        confirmButton.setOnClickListener(confirmButtonClickListener);
        cancelButton.setOnClickListener(cancelButtonClickListener);
        startTimeButton.setOnClickListener(startTimeButtonClickListener);
        endTimeButton.setOnClickListener(endTimeButtonClickListener);
        notificationTimeButton
                .setOnClickListener(notificationTimeButtonClickListener);
    }

    private OnClickListener confirmButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            if (descriptionEdit.getText().toString().trim().equals("")) {
                showDialog(Common.ADDING_EMPTY_TASK_DIALOG);
            } else {
                setupTaskObject();
                if (mRowId == 0) {
                    createTask();
                } else {
                    updateTask();
                }
                setResult(RESULT_OK);
                finish();
            }
        }
    };

    @Override
	protected void onStart() {
    	populateOffers();
    	if(task == null){
    		init();
    	}
		super.onStart();
	}

	private OnClickListener cancelButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            setResult(android.app.Activity.RESULT_CANCELED);
            finish();
        }
    };

    private OnClickListener startTimeButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            startActivityForResult(new Intent(TaskAddEditActivity.this,
                    DateTimePickerActivity.class), 0);
        }
    };

    private OnClickListener endTimeButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            startActivityForResult(new Intent(TaskAddEditActivity.this,
                    DateTimePickerActivity.class), 1);
        }
    };

    private OnClickListener notificationTimeButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            startActivityForResult(new Intent(TaskAddEditActivity.this,
                    DateTimePickerActivity.class), 2);
        }
    };

    private void init() {
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mRowId = extras.getLong(DBOfferHandler.KEY_ROWID);
            mOfferId = extras.getLong(DBTaskHandler.KEY_OFFER_ID);
        }

        if (mRowId > 0) {
            JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
            dbHelper.open();
            task = dbHelper.getTask(mRowId);
            dbHelper.close();
            populateFields();
        } else {
            task = new Task();
            startTimeButton.setText("Press to set the start time");
            endTimeButton.setText("Press to set the end time");
            notificationTimeButton.setText("Press to set the notification time");
        }
        if (mOfferId > 0) {
            task.setOfferId(mOfferId);
            offerSpinner.setSelection(1 + getOfferIndexById(mOfferId));
        }

    };

    protected void updateTask() {
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.open();
        dbHelper.updateTask(task);
        dbHelper.close();
    }

    protected void setupTaskObject() {
        task.setDescription((descriptionEdit.getText().toString()));

        int pos = offerSpinner.getSelectedItemPosition();
        if (pos == 0) {// no offer selected
            task.setOfferId(-1);
        } else {
            Offer offer = (Offer) offers.get(pos - 1);
            if (offer != null) {
                task.setOfferId(offer.getId());
            }
        }

        task.setCategory("");
        task.setArchive(false);
        // start end time should have been defined already
    }

    private void createTask() {
        JOffersDbAdapter dBase = JOffersDbAdapter.getInstance();
        dBase.open();
        dBase.createTask(task);
        dBase.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                long time = extras.getLong(getString(R.string.time));
                if (requestCode == 0) {
                    task.setStartTime(time);
                    startTimeButton.setText(Common.getTimeAsString(time));
                    if(mRowId > 0){
                    	updateTask();
                    }
                } else if (requestCode == 1) {
                    task.setEndTime(time);
                    endTimeButton.setText(Common.getTimeAsString(time));
                    if(mRowId > 0){
                    	updateTask();
                    }
                } else if (requestCode == 2) {
                    task.setNotificationTime(time);
                    notificationTimeButton.setText(Common.getTimeAsString(time));
                    if(mRowId > 0){
                    	updateTask();
                    }
                    setAlarm(time);
                }
            }
        }
    }

    private void setAlarm(long alarmTime) {
        Intent intent = new Intent(getApplicationContext(),
                TimeAlarmBroadcastReceiver.class);
        intent.putExtra("taskName", task.getDescription());
        intent.putExtra("taskstartTime", task.getStartTime());
        intent.putExtra("taskId", task.getId());
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 
                0, intent, PendingIntent.FLAG_ONE_SHOT);
        ((AlarmManager) getSystemService(ALARM_SERVICE)).set(
                AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

        JobmateApplication.getInstance().setPendingIntent(pendingIntent);
    }

    private void populateOffers() {
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.open();
        offers = dbHelper.getRecentOffersAsList();
        List<String> positions = dbHelper.getRecentOffersPositions();
        dbHelper.close();

        positions.add(0, getString(R.string.none));
        String[] offerStrings = (String[]) positions
                .toArray(new String[positions.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, offerStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        offerSpinner.setAdapter(adapter);
    }

    private int getOfferIndexById(long id) {
        int ret = -1;
        int offersCnt = offers.size();

        for (int i = 0; i < offersCnt; i++) {
            long offerId = ((Offer) offers.get(i)).getId();
            if (id == offerId) {
                ret = i;
            }
        }
        return ret;
    }

    private void populateFields() {
        if (-1 != mRowId) {
            JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
            dbHelper.open();
            task = dbHelper.getTask(mRowId);
            dbHelper.close();

            if (null != task) {
                descriptionEdit.setText(task.getDescription());

                long startTime = task.getStartTime();
                if (startTime > 0) {
                    startTimeButton.setText(Common.getTimeAsString(startTime));
                } else {
                    startTimeButton.setText("No start time provided");
                }

                long endTime = task.getEndTime();
                if (endTime > 0) {
                    endTimeButton.setText(Common.getTimeAsString(endTime));
                } else {
                    endTimeButton.setText("No end time provided");
                }

                long notificationTime = task.getNotificationTime();
                if (notificationTime > 0) {
                    notificationTimeButton.setText(Common
                            .getTimeAsString(notificationTime));
                } else {
                    notificationTimeButton
                            .setText("No notification time provided");
                }

                offerSpinner.setSelection(1 + getOfferIndexById(task
                        .getOfferId()));
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case Common.ADDING_EMPTY_TASK_DIALOG:
            return DialogManager.getDialog(this, id, null);
        default:
            break;
        }
        return super.onCreateDialog(id);
    }

    // TODO: handle saveinstance state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TableHandler.KEY_ROWID, mRowId);
    }

}
