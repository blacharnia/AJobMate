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

public class TaskArchivedListActivity extends Activity implements Observer {
    private ListView mTasksList;
    private TaskCursorAdapter mListAdapter;
    private Spinner offerSpinner;
    private List<Object> offers;
    private long mOfferId;
    private Button unarchiveAllButton;
    private Button deleteAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_tasks_archived);

        offerSpinner = (Spinner) findViewById(R.id.offers);

        mTasksList = (ListView) findViewById(R.id.tasks);
        mTasksList.setEmptyView(findViewById(R.id.empty));
        mTasksList.setOnItemClickListener(mItemListener);
        registerForContextMenu(mTasksList);

        unarchiveAllButton = (Button) findViewById(R.id.unarchiveAll);
        unarchiveAllButton.setOnClickListener(unarchiveAllClickListener);

        deleteAllButton = (Button) findViewById(R.id.deleteAll);
        deleteAllButton.setOnClickListener(deleteAllClickListener);
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
                offerSpinner.setSelection(1 + getOfferIndexById(mOfferId));
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
            cursor = JOffersDbAdapter.getInstance().getArchivedTasks();
        } else {
            cursor = JOffersDbAdapter.getInstance().getArchivedTasksForOffer(
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
            unarchiveAllButton.setEnabled(cnt > 0);
            deleteAllButton.setEnabled(cnt > 0);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, 0, 0, R.string.unarchive_task);
        menu.add(Menu.NONE, 1, 0, R.string.delete_task);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int id = item.getItemId();
        if (id == 0) {
            EventHandler.getInstance().unarchiveTask(info.id);
        } else if (id == 1) {
            EventHandler.getInstance().deleteTask(info.id);
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
        case DialogManager.CONFIRM_UNARCHIVE_ALL_TASKS_DIALOG:
            return DialogManager.getInstance().getDialog(this, id,
                    new OnConfirmUnarchiveDialogListener());
        case DialogManager.CONFIRM_DELETE_ALL_ARCHIVED_TASKS_DIALOG:
            return DialogManager.getInstance().getDialog(this, id,
                    new OnConfirmDeleteDialogListener());
        default:
            break;
        }
        return super.onCreateDialog(id);
    }

    private static class OnConfirmUnarchiveDialogListener implements
            CommonDialogListener {

        public void onPositiveResponse() {
            EventHandler.getInstance().unarchiveAllTasks();
        }

        public void onNegativeResponse() {
        }

    }

    private static class OnConfirmDeleteDialogListener implements
            CommonDialogListener {

        public void onPositiveResponse() {
            EventHandler.getInstance().deleteAllArchivedTasks();
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

    private OnClickListener unarchiveAllClickListener = new OnClickListener() {

        public void onClick(View v) {
            if (mListAdapter.getCount() > 0) {
                showDialog(DialogManager.CONFIRM_UNARCHIVE_ALL_TASKS_DIALOG);
            }
        }
    };

    OnClickListener deleteAllClickListener = new OnClickListener() {

        public void onClick(View v) {
            if (mListAdapter.getCount() > 0) {
                showDialog(DialogManager.CONFIRM_DELETE_ALL_ARCHIVED_TASKS_DIALOG);
            }
        }
    };

    private OnItemClickListener mItemListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
            Intent intent = new Intent(TaskArchivedListActivity.this,
                    TaskAddEditActivity.class);
            intent.putExtra(TableHandler.KEY_ROWID, arg3);
            startActivityForResult(intent, Common.ACTIVITY_EDIT);
        }
    };

    public void update(Observable arg0, Object arg1) {
        loadTasks();
    }

    private void populateOffers() {
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        offers = dbHelper.getRecentOffersAsList();
        List<String> positions = JOffersDbAdapter.getInstance()
                .getRecentOffersPositions();

        positions.add(0, getString(R.string.all_offers));
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
