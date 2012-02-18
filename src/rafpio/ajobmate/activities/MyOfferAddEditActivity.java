package rafpio.ajobmate.activities;

import rafpio.ajobmate.R;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.core.DialogManager;
import rafpio.ajobmate.core.ILastLocationFinder;
import rafpio.ajobmate.db.DBOfferHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.model.Offer;
import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


//FIXME: disable adding offer when one with the same employer and position exists
public class MyOfferAddEditActivity extends Activity {
    private Button getLocationBtn;
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
    private ILastLocationFinder lastLocationFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastLocationFinder = Common.getLastLocationFinder(this);
        lastLocationFinder
                .setChangedLocationListener(oneShotLastLocationUpdateListener);

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

        getLocationBtn = (Button) findViewById(R.id.gps_location);

        getLocationBtn.setOnClickListener(mGetLocationClickListener);

        init();

        // button event listeners
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (isEnoughDataProvided()) {
                    setupOfferObject();
                    if (mRowId == 0) {
                        createOffer();
                    } else {
                        updateOffer();
                    }
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showDialog(Common.ADDING_EMPTY_OFFER_DIALOG);                    
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

    private boolean isEnoughDataProvided() {
        return !TextUtils.isEmpty(mPositionText.getText().toString().trim())
                && !TextUtils
                        .isEmpty(mEmployerText.getText().toString().trim());
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
            JOffersDbAdapter.getInstance().getOffer(mRowId);
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
        JOffersDbAdapter.getInstance().createOffer(offer);
    }

    protected void updateOffer() {
        JOffersDbAdapter.getInstance().updateOffer(offer);
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

    private OnClickListener mGetLocationClickListener = new OnClickListener() {

        public void onClick(View v) {
            Location location = lastLocationFinder.getLastBestLocation(0, 0);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                if (latitude != 0) {
                    mLatitudeText.setText(String.valueOf(latitude));
                }
                if (longitude != 0) {
                    mLongitudeText.setText(String.valueOf(longitude));
                }
            }
        }
    };

    // TODO: implement save instance state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(DBOfferHandler.KEY_ROWID, mRowId);
        super.onSaveInstanceState(outState);

    }

    protected LocationListener oneShotLastLocationUpdateListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                if (latitude != 0) {
                    mLatitudeText.setText(String.valueOf(latitude));
                }
                if (longitude != 0) {
                    mLongitudeText.setText(String.valueOf(longitude));
                }
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }
    };
}
