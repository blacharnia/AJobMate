package rafpio.ajobmate.activities;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import rafpio.ajobmate.R;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.core.DialogManager;
import rafpio.ajobmate.core.EventHandler;
import rafpio.ajobmate.core.EventHandler.OpResult;
import rafpio.ajobmate.db.DBOfferHandler;
import rafpio.ajobmate.db.DBTaskHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.db.TableHandler;
import rafpio.ajobmate.model.Offer;
import rafpio.ajobmate.model.Task;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TaskAddEditActivity extends Activity implements Observer {
    private EditText descriptionEdit;

    private TextView startTimeTV;
    private TextView endTimeTV;
    private TextView alarmTimeTV;

    private Button setStartTimeButton;
    private Button setEndTimeButton;
    private Button notificationTimeButton;
    private Spinner offerSpinner;
    private Task task;

    private long mRowId;
    private long mOfferId;
    private List<Object> offers;

    public static final int ALARM_TIME_REQUEST = 2;
    public static final int START_TIME_REQUEST = 3;
    public static final int END_TIME_REQUEST = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.task_edit);

        Button confirmButton = (Button) findViewById(R.id.confirm);
        Button cancelButton = (Button) findViewById(R.id.cancel);

        descriptionEdit = (EditText) findViewById(R.id.description);

        startTimeTV = (TextView) findViewById(R.id.startTimeTV);
        endTimeTV = (TextView) findViewById(R.id.endTimeTV);
        alarmTimeTV = (TextView) findViewById(R.id.alarmTimeTV);

        setStartTimeButton = (Button) findViewById(R.id.startTimeBtn);
        setEndTimeButton = (Button) findViewById(R.id.endTimeBtn);
        notificationTimeButton = (Button) findViewById(R.id.alarmTimeBtn);
        offerSpinner = (Spinner) findViewById(R.id.offer);

        // button event listeners
        confirmButton.setOnClickListener(confirmButtonClickListener);
        cancelButton.setOnClickListener(cancelButtonClickListener);
        setStartTimeButton.setOnClickListener(setStartTimeButtonClickListener);
        setEndTimeButton.setOnClickListener(setEndTimeButtonClickListener);

        notificationTimeButton
                .setOnClickListener(notificationTimeButtonClickListener);
    }

    private OnClickListener confirmButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            if (task.isStartTimeSet() && task.isEndTimeSet()
                    && task.getStartTime() > task.getEndTime()) {
                showDialog(DialogManager.BAD_START_END_TIME_DIALOG);
            } else if (descriptionEdit.getText().toString().trim().equals("")) {
                showDialog(DialogManager.ADDING_EMPTY_TASK_DIALOG);
            } else {
                setupTaskObject();
                if (mRowId == 0) {
                    EventHandler.getInstance().addTask(task);
                } else {
                    EventHandler.getInstance().updateTask(task);
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        populateOffers();
        if (task == null) {
            init();
        }
        EventHandler.getInstance().addObserver(this);
    }

    private OnClickListener cancelButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            setResult(android.app.Activity.RESULT_CANCELED);
            finish();
        }
    };

    private OnClickListener setStartTimeButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(TaskAddEditActivity.this,
                    TimePickerActivity.class);
            intent.putExtra(DBTaskHandler.KEY_START_TIME, task.getStartTime());
            intent.putExtra(DBTaskHandler.KEY_END_TIME, task.getEndTime());
            intent.putExtra("request_code", START_TIME_REQUEST);
            startActivityForResult(intent, START_TIME_REQUEST);
        }
    };

    private OnClickListener setEndTimeButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(TaskAddEditActivity.this,
                    TimePickerActivity.class);
            intent.putExtra(DBTaskHandler.KEY_START_TIME, task.getStartTime());
            intent.putExtra(DBTaskHandler.KEY_END_TIME, task.getEndTime());
            intent.putExtra("request_code", END_TIME_REQUEST);
            startActivityForResult(intent, END_TIME_REQUEST);
        }
    };

    private OnClickListener notificationTimeButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(TaskAddEditActivity.this,
                    TimePickerActivity.class);
            intent.putExtra(DBTaskHandler.KEY_START_TIME, task.getStartTime());
            intent.putExtra(DBTaskHandler.KEY_END_TIME, task.getEndTime());
            intent.putExtra(DBTaskHandler.KEY_NOTIFICATION_TIME,
                    task.getNotificationTime());
            intent.putExtra("request_code", ALARM_TIME_REQUEST);
            startActivityForResult(intent, ALARM_TIME_REQUEST);
        }
    };

    private void init() {
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mRowId = extras.getLong(DBOfferHandler.KEY_ROWID);
            mOfferId = extras.getLong(DBTaskHandler.KEY_OFFER_ID);
        }

        if (mRowId > 0) {
            task = JOffersDbAdapter.getInstance().getTask(mRowId);
            populateFields();
        } else {
            task = new Task();
        }
        if (mOfferId > 0) {
            task.setOfferId(mOfferId);
            offerSpinner.setSelection(1 + Common.getOfferIndexById(offers,
                    mOfferId));
        }

    };

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

        task.setArchive(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                boolean timeSet = extras.getBoolean("time_set");
                switch (requestCode) {
                case START_TIME_REQUEST:
                    if (timeSet) {
                        task.enableFlag(Task.FLAG_START_TIME_SET);
                        long startTime = extras.getLong("start_time");
                        task.setStartTime(startTime);
                        startTimeTV.setText(Common.getTimeAsString(startTime));
                    } else {
                        task.disableFlag(Task.FLAG_START_TIME_SET);
                        startTimeTV.setText("No start time provided");
                    }
                    break;
                case END_TIME_REQUEST:
                    if (timeSet) {
                        task.enableFlag(Task.FLAG_END_TIME_SET);
                        long endTime = extras.getLong("end_time");
                        task.setEndTime(endTime);
                        endTimeTV.setText(Common.getTimeAsString(endTime));
                    } else {
                        task.disableFlag(Task.FLAG_END_TIME_SET);
                        endTimeTV.setText("No end time provided");
                    }
                    break;
                case ALARM_TIME_REQUEST:
                    if (timeSet) {
                        task.enableFlag(Task.FLAG_ALARM_TIME_SET);
                        long notificationTime = extras
                                .getLong("notification_time");
                        task.setNotificationTime(notificationTime);
                        alarmTimeTV.setText(Common
                                .getTimeAsString(notificationTime));
                    } else {
                        task.disableFlag(Task.FLAG_ALARM_TIME_SET);
                        alarmTimeTV.setText("No notification time provided");
                    }
                    break;
                }
            }
        }
    }

    private void populateOffers() {
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        offers = dbHelper.getRecentOffersAsList();
        List<String> positions = dbHelper.getRecentOffersPositions();

        positions.add(0, getString(R.string.none));
        String[] offerStrings = (String[]) positions
                .toArray(new String[positions.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, offerStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        offerSpinner.setAdapter(adapter);
    }

    private void populateFields() {
        if (-1 != mRowId) {
            task = JOffersDbAdapter.getInstance().getTask(mRowId);

            if (null != task) {
                descriptionEdit.setText(task.getDescription());

                if (task.isStartTimeSet()) {
                    startTimeTV.setText(Common.getTimeAsString(task
                            .getStartTime()));
                } else {
                    endTimeTV.setText("No start time provided");
                }

                if (task.isEndTimeSet()) {
                    endTimeTV
                            .setText(Common.getTimeAsString(task.getEndTime()));
                } else {
                    endTimeTV.setText("No end time provided");
                }

                if (task.isAlarmTimeSet()) {
                    alarmTimeTV.setText(Common.getTimeAsString(task
                            .getNotificationTime()));
                } else {
                    alarmTimeTV.setText("No notification time provided");
                }

                offerSpinner.setSelection(1 + Common.getOfferIndexById(offers,
                        task.getOfferId()));
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DialogManager.ADDING_EMPTY_TASK_DIALOG:
        case DialogManager.BAD_START_END_TIME_DIALOG:
        case DialogManager.BAD_TIME_DIALOG:
            return DialogManager.getInstance().getDialog(this, id, null);
        default:
            break;
        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventHandler.getInstance().deleteObserver(this);
    }

    // TODO: handle saveinstance state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TableHandler.KEY_ROWID, mRowId);
    }

    public void update(Observable observable, Object data) {
        OpResult opResult = (OpResult) data;

        switch (opResult.status) {
        case EventHandler.TASK_UPDATED:
        case EventHandler.TASK_ADDED:
            setResult(RESULT_OK);
            finish();
            break;
        case EventHandler.TASK_NOT_ADDED:
        case EventHandler.TASK_NOT_UPDATED:
            setResult(RESULT_CANCELED);
            finish();
            break;
        }
    }

}
