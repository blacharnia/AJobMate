package rafpio.ajobmate.activities;

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
import rafpio.ajobmate.model.Offer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyOfferDetailActivity extends Activity implements Observer {
    private TextView mPositionText;
    private TextView mEmployerText;
    private Button mEmailButton;
    private Button mPhoneNrButton;
    private TextView mLocation;
    private Button mGPSLocationButton;
    private TextView mDescriptionText;

    private TextView mTasksMessage;
    private Button mShowTasks;
    private Button mAddToContacts;

    private long mRowId;
    private Offer offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_offer_detail);

        EventHandler.getInstance().addObserver(this);

        mPositionText = (TextView) findViewById(R.id.position);
        mEmployerText = (TextView) findViewById(R.id.employer);
        mLocation = (TextView) findViewById(R.id.location);
        mGPSLocationButton = (Button) findViewById(R.id.gps_location);
        mEmailButton = (Button) findViewById(R.id.email);
        mPhoneNrButton = (Button) findViewById(R.id.phone_nr);
        mShowTasks = (Button) findViewById(R.id.show_tasks);
        mShowTasks.setOnClickListener(mShowTasksTasksOnClickListener);
        mTasksMessage = (TextView) findViewById(R.id.tasks_message);
        mDescriptionText = (TextView) findViewById(R.id.description);
        mPhoneNrButton.setOnClickListener(mPhoneNrOnClickListener);
        mEmailButton.setOnClickListener(mEmailOnClickListener);
        mGPSLocationButton.setOnClickListener(mLocationOnClickListener);
        mAddToContacts = (Button) findViewById(R.id.addToContactsBtn);
        mAddToContacts.setOnClickListener(mAddToContactsClickListener);

        ((Button) findViewById(R.id.editBtn))
                .setOnClickListener(mEditOnClickListener);
        ((Button) findViewById(R.id.addTaskBtn))
                .setOnClickListener(mAddTaskOnClickListener);
    }

    private void checkInputParams() {
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mRowId = extras.getLong(DBOfferHandler.KEY_ROWID);
        }
    }

    private View.OnClickListener mEditOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            Intent intent = new Intent(MyOfferDetailActivity.this,
                    MyOfferAddEditActivity.class);
            intent.putExtra(DBOfferHandler.KEY_ROWID, mRowId);
            startActivityForResult(intent, Common.ACTIVITY_EDIT);
        }
    };

    @Override
    protected void onDestroy() {
        EventHandler.getInstance().deleteObserver(this);
        super.onDestroy();
    }

    private View.OnClickListener mAddTaskOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            Intent intent = new Intent(MyOfferDetailActivity.this,
                    TaskAddEditActivity.class);
            intent.putExtra(DBTaskHandler.KEY_OFFER_ID, offer.getId());
            startActivityForResult(intent, Common.ACTIVITY_EDIT);
        }
    };

    /*
     * private void test() { Intent intent = new Intent(Intent.ACTION_INSERT);
     * intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
     * 
     * intent.putExtra(ContactsContract.Intents.Insert.NAME,
     * "some Contact Name");
     * intent.putExtra(ContactsContract.Intents.Insert.PHONE, "12121");
     * intent.putExtra(ContactsContract.Intents.Insert.EMAIL,
     * "naoknaoknaok@gmail.com");
     * 
     * startActivity(intent); }
     */
    
    private View.OnClickListener mAddToContactsClickListener = new OnClickListener() {

        public void onClick(View v) {

            if ("".equals(offer.getPhoneNr())) {
                showDialog(DialogManager.PHONE_NUMBER_MISSING_DIALOG);
            } else {
                Intent intent = new Intent(Intent.ACTION_INSERT);
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
    };

    private View.OnClickListener mPhoneNrOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            if (!TextUtils.isEmpty(offer.getPhoneNr())) {
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                        + offer.getPhoneNr())));
            }
        }
    };

    private View.OnClickListener mEmailOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            String email = offer.getEmail();

            if (!TextUtils.isEmpty(email)) {
                Common.sendEmail(email, MyOfferDetailActivity.this);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        checkInputParams();

        if (mRowId == 0) {
            finish();
        }

        populateFields();
        setupTasksPanel();
    }

    private View.OnClickListener mLocationOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            Intent intent = new Intent(MyOfferDetailActivity.this,
                    LocationMapActivity.class);
            intent.putExtra("longitude", offer.getLongitude());
            intent.putExtra("latitude", offer.getLatitude());
            intent.putExtra("title", offer.getEmployer());
            intent.putExtra("message", offer.getPosition());

            startActivity(intent);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            populateFields();
        }
    }

    private void populateFields() {
        if (mRowId > 0) {
            offer = JOffersDbAdapter.getInstance().getOffer(mRowId);

            if (null != offer) {
                mPositionText.setText(offer.getPosition());
                mEmployerText.setText(offer.getEmployer());

                String location = offer.getLocation();
                if (TextUtils.isEmpty(location)) {
                    mLocation.setText("No location provided");
                } else {
                    mLocation.setText("location: " + location);
                }

                double latitude = offer.getLatitude();
                double longitude = offer.getLongitude();
                if (latitude == 0 || longitude == 0) {
                    mGPSLocationButton.setEnabled(false);
                    mGPSLocationButton.setText("No GPS location provided");
                } else {
                    mGPSLocationButton.setText("GPS: "
                            + String.valueOf(latitude) + "\n"
                            + String.valueOf(longitude));
                    mGPSLocationButton.setEnabled(true);
                }

                String email = offer.getEmail();
                if (TextUtils.isEmpty(email)) {
                    mEmailButton.setText("No email provided");
                    mEmailButton.setEnabled(false);
                } else {
                    mEmailButton.setText("email: " + email);
                    mEmailButton.setEnabled(true);
                }

                String phoneNumber = offer.getPhoneNr();
                if (TextUtils.isEmpty(phoneNumber)) {
                    mPhoneNrButton.setText("No phone number provided");
                    mPhoneNrButton.setEnabled(false);
                } else {
                    mPhoneNrButton.setText("tel.: " + phoneNumber);
                    mPhoneNrButton.setEnabled(true);
                }

                String description = Html.fromHtml(offer.getDescription())
                        .toString();

                if (TextUtils.isEmpty(description)) {
                    mDescriptionText.setText("No description provided");
                } else {
                    mDescriptionText.setText(description);
                }
            }
        }
    }

    private void setupTasksPanel() {
        int cnt = 0;
        Cursor tasks = JOffersDbAdapter.getInstance().getRecentTasksForOffer(
                mRowId);
        if (tasks != null) {
            cnt = tasks.getCount();
        }
        mTasksMessage.setText(String.valueOf(cnt + " tasks for this offer"));
        mShowTasks.setEnabled(cnt > 0);
    }

    private View.OnClickListener mShowTasksTasksOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            Intent intent = new Intent(MyOfferDetailActivity.this,
                    TaskListActivity.class);
            intent.putExtra(DBTaskHandler.KEY_OFFER_ID, offer.getId());
            startActivityForResult(intent, Common.ACTIVITY_EDIT);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DialogManager.CONTACT_EXISTS_DIALOG:
        case DialogManager.CONTACT_ADDED_DIALOG:
        case DialogManager.PHONE_NUMBER_MISSING_DIALOG:
            return DialogManager.getInstance().getDialog(this, id, null);
        }
        return super.onCreateDialog(id);
    }

    public void update(Observable observable, Object data) {
        OpResult opResult = (OpResult) data;

        switch (opResult.status) {
        case EventHandler.CONTACT_ADDED:
            showDialog(DialogManager.CONTACT_ADDED_DIALOG);
            break;
        case EventHandler.CONTACT_EXISTS:
            showDialog(DialogManager.CONTACT_EXISTS_DIALOG);
            break;
        case EventHandler.MULTIPLE_CONTACTS_EXIST:
            showDialog(DialogManager.CONTACT_EXISTS_DIALOG);
            break;
        }
    }

}
