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
import rafpio.ajobmate.model.Offer;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
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

    private static final int EDIT_OFFER = 0;
    private static final int ARCHIVE_OFFER = 1;
    private static final int CALL_EMPLOYER = 2;
    private static final int SEND_EMAIL = 3;
    private static final int SHOW_TASKS = 4;
    private static final int ADD_TASK = 5;
    private static final int ADD_TO_CONTACTS = 6;

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
            loadingDialog.setMessage(getString(R.string.loading));
            loadingDialog.setIndeterminate(true);
            return loadingDialog;
        case DialogManager.CONFIRM_ARCHIVE_ALL_OFFERS_DIALOG:
            return DialogManager.getInstance().getDialog(this, id,
                    new OnConfirmArchiveDialogListener());
        case DialogManager.PHONE_NUMBER_MISSING_DIALOG:
            return DialogManager.getInstance().getDialog(this, id, null);
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
        String[] options = getResources().getStringArray(
                R.array.offer_context_options);
        for (int i = 0; i < options.length; i++) {
            menu.add(Menu.NONE, i, 0, options[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        Offer offer;
        Intent intent;

        int id = item.getItemId();
        switch (id) {
        case EDIT_OFFER:
            intent = new Intent(MyOffersListActivity.this,
                    MyOfferAddEditActivity.class);
            intent.putExtra(DBTaskHandler.KEY_ROWID, info.id);
            startActivityForResult(intent, Common.ACTIVITY_EDIT);
            break;
        case ARCHIVE_OFFER:
            EventHandler.getInstance().archiveOffer(info.id);
            break;
        case CALL_EMPLOYER:
            offer = JOffersDbAdapter.getInstance().getOffer(info.id);
            if (offer != null) {
                String phoneNumber = offer.getPhoneNr();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    Common.startCalling(phoneNumber, MyOffersListActivity.this);
                }
            }
            break;
        case SEND_EMAIL:
            offer = JOffersDbAdapter.getInstance().getOffer(info.id);
            if (offer != null) {
                String email = offer.getEmail();

                if (!TextUtils.isEmpty(email)) {
                    Common.sendEmail(email, MyOffersListActivity.this);
                }
            }
            break;
        case SHOW_TASKS:
            offer = JOffersDbAdapter.getInstance().getOffer(info.id);
            if (offer != null) {
                intent = new Intent(MyOffersListActivity.this,
                        TaskListActivity.class);
                intent.putExtra(DBTaskHandler.KEY_OFFER_ID, offer.getId());
                startActivityForResult(intent, Common.ACTIVITY_EDIT);
            }
            break;
        case ADD_TASK:
            offer = JOffersDbAdapter.getInstance().getOffer(info.id);
            if (offer != null) {
                intent = new Intent(MyOffersListActivity.this,
                        TaskAddEditActivity.class);
                intent.putExtra(DBTaskHandler.KEY_OFFER_ID, offer.getId());
                startActivityForResult(intent, Common.ACTIVITY_EDIT);
            }
            break;
        case ADD_TO_CONTACTS:
            offer = JOffersDbAdapter.getInstance().getOffer(info.id);
            if (offer != null) {
                if (TextUtils.isEmpty(offer.getPhoneNr())) {
                    showDialog(DialogManager.PHONE_NUMBER_MISSING_DIALOG);
                } else {
                    intent = new Intent(Intent.ACTION_INSERT);
                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                    intent.putExtra(ContactsContract.Intents.Insert.NAME,
                            offer.getEmployer());
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE,
                            offer.getPhoneNr());
                    intent.putExtra(ContactsContract.Intents.Insert.EMAIL,
                            offer.getEmail());

                    startActivity(intent);
                }
            }
            break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
