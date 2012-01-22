package rafpio.ajobmate.activities;

import rafpio.ajobmate.R;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.core.DialogManager;
import rafpio.ajobmate.db.DBOfferHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.model.Offer;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MyOfferAddEditActivity extends Activity {
    private EditText mPositionText;
    private EditText mEmployerText;
    private EditText mEmailText;
    private EditText mPhoneNrText;
    private EditText mLocationText;
    private EditText mLatitudeText;
    private EditText mLongitudeText;
    private EditText mDescriptionText;
    private long mRowId;
    private Offer offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.offer_edit);

        Button confirmButton = (Button) findViewById(R.id.confirm);
        Button cancelButton = (Button) findViewById(R.id.cancel);
        mPositionText = (EditText) findViewById(R.id.position);
        mEmployerText = (EditText) findViewById(R.id.employer);
        mLocationText = (EditText) findViewById(R.id.location);
        mLatitudeText = (EditText) findViewById(R.id.latitude);
        mLongitudeText = (EditText) findViewById(R.id.longitude);
        mDescriptionText = (EditText) findViewById(R.id.description);
        mEmailText = (EditText) findViewById(R.id.email);
        mPhoneNrText = (EditText) findViewById(R.id.phone_nr);

        init();

        // button event listeners
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mPositionText.getText().toString().trim().equals("")) {
                    showDialog(Common.ADDING_EMPTY_OFFER_DIALOG);
                } else {
                    setupOfferObject();
                    if (mRowId == 0) {
                        createOffer();
                    } else {
                        updateOffer();
                    }
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(android.app.Activity.RESULT_CANCELED);
                finish();
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case Common.ADDING_EMPTY_OFFER_DIALOG:
            return DialogManager.getDialog(this, id, null);
        default:
            break;
        }
        return super.onCreateDialog(id);
    }

    private void init() {
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mRowId = extras.getLong(DBOfferHandler.KEY_ROWID);
        }

        if (mRowId > 0) {
            JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
            dbHelper.open();
            offer = dbHelper.getOffer(mRowId);
            dbHelper.close();
            populateFields();
        } else {
            offer = new Offer();
        }

    };

    private void setupOfferObject() {
        offer.setPosition(mPositionText.getText().toString());
        offer.setEmployer(mEmployerText.getText().toString());
        offer.setLocation(mLocationText.getText().toString());

        try {
            offer.setLatitude(Double.parseDouble(mLatitudeText.getText()
                    .toString()));
        } catch (NumberFormatException e) {
            //
        }

        try {
            offer.setLongitude(Double.parseDouble(mLongitudeText.getText()
                    .toString()));
        } catch (NumberFormatException e) {
            //
        }

        offer.setLocation(mLocationText.getText().toString());
        offer.setDescription(mDescriptionText.getText().toString());
        offer.setEmail(mEmailText.getText().toString());
        offer.setPhoneNr(mPhoneNrText.getText().toString());
        offer.setArchive(false);
    }

    private void createOffer() {
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.open();
        dbHelper.createOffer(offer);
        dbHelper.close();
    }

    protected void updateOffer() {
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.open();
        dbHelper.updateOffer(offer);
        dbHelper.close();
    }

    private void populateFields() {
        if (null != offer) {
            mPositionText.setText(offer.getPosition());
            mEmployerText.setText(offer.getEmployer());
            mLocationText.setText(offer.getLocation());
            mDescriptionText.setText(Html.fromHtml(offer.getDescription())
                    .toString());
            mEmailText.setText(offer.getEmail());

            double latitude = offer.getLatitude();
            double longitude = offer.getLongitude();
            if (latitude != 0) {
                mLatitudeText.setText(String.valueOf(latitude));
            }
            if (longitude != 0) {
                mLongitudeText.setText(String.valueOf(longitude));
            }

            mEmailText.setText(offer.getEmail());
            mPhoneNrText.setText(offer.getPhoneNr());
        }
    }

    // TODO: implement save instance state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(DBOfferHandler.KEY_ROWID, mRowId);
    }
}
