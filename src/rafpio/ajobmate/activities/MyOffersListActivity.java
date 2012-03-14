package rafpio.ajobmate.activities;

import java.util.Observable;
import java.util.Observer;

import rafpio.ajobmate.R;
import rafpio.ajobmate.adapters.OfferCursorAdapter;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.core.DialogManager;
import rafpio.ajobmate.core.DialogManager.CommonDialogListener;
import rafpio.ajobmate.core.EventHandler;
import rafpio.ajobmate.db.DBTaskHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.db.TableHandler;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MyOffersListActivity extends Activity implements Observer {

    private ListView mOffersList;
    private OfferCursorAdapter mListAdapter;
    private Button addButton;
    private Button archiveAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_offers);

        EventHandler.getInstance().addObserver(this);

        mOffersList = (ListView) findViewById(R.id.offers);
        mOffersList.setEmptyView(findViewById(R.id.empty));

        addButton = (Button) findViewById(R.id.add);
        addButton.setOnClickListener(addClickListener);

        archiveAllButton = (Button) findViewById(R.id.archiveAll);
        archiveAllButton.setOnClickListener(archiveAllClickListener);

        registerForContextMenu(mOffersList);

        mOffersList.setOnItemClickListener(mItemListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showDialog(Common.LOADING_DIALOG);
        loadOffers();
        dismissDialog(Common.LOADING_DIALOG);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case Common.LOADING_DIALOG:
            ProgressDialog loadingDialog = new ProgressDialog(this);
            loadingDialog.setMessage("Loading...");
            loadingDialog.setIndeterminate(true);
            return loadingDialog;
        case DialogManager.CONFIRM_ARCHIVE_ALL_OFFERS_DIALOG:
            return DialogManager.getInstance().getDialog(this, id,
                    new OnConfirmArchiveDialogListener());
        default:
            break;
        }
        return super.onCreateDialog(id);
    }

    private void loadOffers() {
        new AsyncTask<Void, Integer, Void>() {
            Cursor cursor;

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (cursor != null) {
                    if (mListAdapter == null) {
                        mListAdapter = new OfferCursorAdapter(
                                getApplicationContext(), cursor);
                        mOffersList.setAdapter(mListAdapter);
                    } else {
                        mListAdapter.changeCursor(cursor);
                    }
                    // TODO:consider placing it somewhere else
                    archiveAllButton.setEnabled(cursor.getCount() > 0);

                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                cursor = JOffersDbAdapter.getInstance().getRecentOffers();
                return null;
            }
        }.execute();

    }

    private static class OnConfirmArchiveDialogListener implements
            CommonDialogListener {

        public void onPositiveResponse() {
            EventHandler.getInstance().archiveAllOffers();
        }

        public void onNegativeResponse() {
        }

    }

    OnClickListener addClickListener = new OnClickListener() {

        public void onClick(View v) {
            startActivityForResult(new Intent(MyOffersListActivity.this,
                    MyOfferAddEditActivity.class), 0);
        }
    };

    OnClickListener archiveAllClickListener = new OnClickListener() {

        public void onClick(View v) {
            if (mListAdapter.getCount() > 0) {
                showDialog(DialogManager.CONFIRM_ARCHIVE_ALL_OFFERS_DIALOG);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventHandler.getInstance().deleteObserver(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, 0, 0, getString(R.string.edit_offer));
        menu.add(Menu.NONE, 1, 0, getString(R.string.archive_offer));        
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        int id = item.getItemId();
        if (id == 1) {
            EventHandler.getInstance().archiveOffer(info.id);
        }
        else if (id == 0) {
            Intent intent = new Intent(MyOffersListActivity.this,
                    MyOfferAddEditActivity.class);
            intent.putExtra(DBTaskHandler.KEY_ROWID, info.id);
            startActivityForResult(intent, Common.ACTIVITY_EDIT);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: check this
        if (resultCode == RESULT_OK) {
            loadOffers();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private OnItemClickListener mItemListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
            Intent intent = new Intent(MyOffersListActivity.this,
                    MyOfferDetailActivity.class);
            intent.putExtra(TableHandler.KEY_ROWID, arg3);
            startActivityForResult(intent, Common.ACTIVITY_EDIT);
        }
    };

    public void update(Observable observable, Object data) {
        loadOffers();
    }

}
