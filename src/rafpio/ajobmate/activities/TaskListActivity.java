package rafpio.ajobmate.activities;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import rafpio.ajobmate.R;
import rafpio.ajobmate.adapters.TaskCursorAdapter;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.core.DialogManager;
import rafpio.ajobmate.core.DialogManager.CommonDialogListener;
import rafpio.ajobmate.core.EventHandler;
import rafpio.ajobmate.db.DBOfferHandler;
import rafpio.ajobmate.db.DBTaskHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.db.TableHandler;
import rafpio.ajobmate.model.Offer;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class TaskListActivity extends Activity implements Observer {
    private ListView mTasksList;
    private TaskCursorAdapter mListAdapter;
    private Spinner offerSpinner;
    private List<Object> offers;
    private long mOfferId;
    private Button addButton;
    private Button archiveAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_tasks);

        offerSpinner = (Spinner) findViewById(R.id.offers);

        mTasksList = (ListView) findViewById(R.id.tasks);
        mTasksList.setEmptyView(findViewById(R.id.empty));
        mTasksList.setOnItemClickListener(mItemListener);
        registerForContextMenu(mTasksList);

        addButton = (Button) findViewById(R.id.add);
        addButton.setOnClickListener(addClickListener);

        archiveAllButton = (Button) findViewById(R.id.archiveAll);
        archiveAllButton.setOnClickListener(archiveAllClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
        offerSpinner.setOnItemSelectedListener(mSpinnerSelectedListener);
        EventHandler.getInstance().addObserver(this);
        showDialog(Common.LOADING_DIALOG);
        loadTasks();
        dismissDialog(Common.LOADING_DIALOG);
    }

    private void init() {
        populateOffers();
        Bundle extras = getIntent().getExtras();

        if (null != extras) {
            mOfferId = extras.getLong(DBTaskHandler.KEY_OFFER_ID);
            if (mOfferId > 0) {
                offerSpinner.setSelection(1 + Common.getOfferIndexById(offers,
                        mOfferId));
            }
        }
    }

    @Override
    protected void onStop() {
        EventHandler.getInstance().deleteObserver(this);
        super.onStop();
    }

    private void loadTasks() {
        Cursor cursor;

        if (mOfferId == 0) {
            cursor = JOffersDbAdapter.getInstance().getRecentTasks();
        } else {
            cursor = JOffersDbAdapter.getInstance().getRecentTasksForOffer(
                    mOfferId);
        }

        if (cursor != null) {
            if (mListAdapter == null) {
                mListAdapter = new TaskCursorAdapter(getApplicationContext(),
                        cursor);
                mTasksList.setAdapter(mListAdapter);
            } else {
                mListAdapter.changeCursor(cursor);
            }
            int cnt = cursor.getCount();
            archiveAllButton.setEnabled(cnt > 0);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, 0, 0, R.string.edit_task);
        menu.add(Menu.NONE, 1, 0, R.string.archive_task);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        int id = item.getItemId();
        if (id == 0) {
            Intent intent = new Intent(TaskListActivity.this,
                    TaskAddEditActivity.class);
            intent.putExtra(DBOfferHandler.KEY_ROWID, info.id);
            startActivityForResult(intent, Common.ACTIVITY_EDIT);
        } else if (id == 1) {
            EventHandler.getInstance().archiveTask(info.id);
        }
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case Common.LOADING_DIALOG:
            ProgressDialog loadingDialog = new ProgressDialog(this);
            loadingDialog.setMessage(getString(R.string.loading));
            loadingDialog.setIndeterminate(true);
            return loadingDialog;
        case DialogManager.CONFIRM_ARCHIVE_ALL_TASKS_DIALOG:
            return DialogManager.getInstance().getDialog(this, id,
                    new OnConfirmArchiveDialogListener());
        default:
            break;
        }
        return super.onCreateDialog(id);
    }

    private static class OnConfirmArchiveDialogListener implements
            CommonDialogListener {

        public void onPositiveResponse() {
            EventHandler.getInstance().archiveAllTasks();
        }

        public void onNegativeResponse() {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            loadTasks();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private OnClickListener addClickListener = new OnClickListener() {

        public void onClick(View v) {
            startActivityForResult(new Intent(TaskListActivity.this,
                    TaskAddEditActivity.class), 0);
        }
    };

    private OnClickListener archiveAllClickListener = new OnClickListener() {

        public void onClick(View v) {
            if (mListAdapter.getCount() > 0) {
                showDialog(DialogManager.CONFIRM_ARCHIVE_ALL_TASKS_DIALOG);
            }
        }
    };

    private OnItemClickListener mItemListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
            Intent intent = new Intent(TaskListActivity.this,
                    TaskDetailActivity.class);
            intent.putExtra(TableHandler.KEY_ROWID, arg3);
            startActivityForResult(intent, Common.ACTIVITY_EDIT);
        }
    };

    public void update(Observable arg0, Object arg1) {
        loadTasks();
    }

    private void populateOffers() {
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();

        offers = JOffersDbAdapter.getInstance().getRecentOffersAsList();
        List<String> positions = dbHelper.getRecentOffersPositions();

        positions.add(0, getString(R.string.all_offers));
        String[] offerStrings = (String[]) positions
                .toArray(new String[positions.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, offerStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        offerSpinner.setAdapter(adapter);
    }

    private OnItemSelectedListener mSpinnerSelectedListener = new OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
            int pos = offerSpinner.getSelectedItemPosition();
            if (pos > 0) {
                Offer offer = (Offer) offers.get(pos - 1);
                if (offer != null) {
                    mOfferId = offer.getId();
                }
            } else {
                mOfferId = 0;
            }
            loadTasks();

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

}
