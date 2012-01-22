package rafpio.ajobmate.test;

import rafpio.ajobmate.activities.MyOfferAddEditActivity;
import rafpio.ajobmate.activities.MyOfferDetailActivity;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.db.TableHandler;
import rafpio.ajobmate.model.Offer;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class MyOfferEditTest extends
        ActivityInstrumentationTestCase2<MyOfferAddEditActivity> {

    private MyOfferAddEditActivity mActivity;
    private Solo solo;
    Context context;
    private TextView hintText;
    private Button confirmButton;
    private Button cancelButton;
    private EditText position;

    private EditText employer;
    private EditText phoneNumber;
    private EditText email;
    private EditText location;
    private EditText latitude;
    private EditText longitude;
    private EditText description;

    private Instrumentation instrumentation;
    private Offer offer;

    public MyOfferEditTest() {
        super("rafpio.ajobmate", MyOfferAddEditActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        instrumentation = getInstrumentation();
        context = instrumentation.getTargetContext();

        offer = TestUtils.createTestOffer(context);

        Intent intent = new Intent(context, MyOfferDetailActivity.class);
        intent.putExtra(TableHandler.KEY_ROWID, offer.getId());
        setActivityIntent(intent);

        mActivity = this.getActivity();
        solo = new Solo(instrumentation, mActivity);

        cacheControls();
    }

    private void cacheControls() {
        hintText = (TextView) mActivity.findViewById(rafpio.ajobmate.R.id.hint);
        confirmButton = (Button) mActivity
                .findViewById(rafpio.ajobmate.R.id.confirm);
        cancelButton = (Button) mActivity
                .findViewById(rafpio.ajobmate.R.id.cancel);

        position = (EditText) mActivity
                .findViewById(rafpio.ajobmate.R.id.position);
        employer = (EditText) mActivity
                .findViewById(rafpio.ajobmate.R.id.employer);
        phoneNumber = (EditText) mActivity
                .findViewById(rafpio.ajobmate.R.id.phone_nr);
        email = (EditText) mActivity.findViewById(rafpio.ajobmate.R.id.email);
        location = (EditText) mActivity
                .findViewById(rafpio.ajobmate.R.id.location);

        latitude = (EditText) mActivity
                .findViewById(rafpio.ajobmate.R.id.latitude);
        longitude = (EditText) mActivity
                .findViewById(rafpio.ajobmate.R.id.longitude);
        description = (EditText) mActivity
                .findViewById(rafpio.ajobmate.R.id.description);
    }

    public void testLayout() throws InterruptedException {
        assertNotNull(hintText);
        assertNotNull(confirmButton);
        assertNotNull(cancelButton);
        assertEquals(offer.getPosition(), position.getText().toString());
        assertEquals(offer.getEmployer(), employer.getText().toString());
        assertEquals(offer.getEmail(), email.getText().toString());
        assertEquals(offer.getPhoneNr(), phoneNumber.getText().toString());
        assertEquals(offer.getLocation(), location.getText().toString());
        assertEquals(offer.getLatitude(),
                Double.valueOf(latitude.getText().toString()));
        assertEquals(offer.getLongitude(),
                Double.valueOf(longitude.getText().toString()));
        assertEquals(offer.getDescription(), description.getText().toString());
    }

    public void testEditOffer() throws InterruptedException {
        solo.clearEditText(position);
        solo.clearEditText(employer);
        solo.clearEditText(phoneNumber);
        solo.clearEditText(email);
        solo.clearEditText(location);
        solo.clearEditText(latitude);
        solo.clearEditText(longitude);
        solo.clearEditText(description);

        solo.enterText(position, "position2");
        solo.enterText(employer, "employer2");
        solo.enterText(phoneNumber, "0701");
        solo.enterText(email, "email2@example.com");
        solo.enterText(location, "Warsaw2");
        solo.enterText(latitude, "53");
        solo.enterText(longitude, "22");
        solo.enterText(description, "description2");

        solo.clickOnButton("Confirm");
        Thread.sleep(2000);

        JOffersDbAdapter.getInstance().init(instrumentation.getTargetContext());
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.open();
        Offer newOfer = dbHelper.getOffer(offer.getId());
        assertNotNull(newOfer);
        assertEquals("position2", newOfer.getPosition());
        assertEquals("employer2", newOfer.getEmployer());
        assertEquals("0701", newOfer.getPhoneNr());
        assertEquals("email2@example.com", newOfer.getEmail());
        assertEquals("Warsaw2", newOfer.getLocation());
        assertEquals(53.0, newOfer.getLatitude());
        assertEquals(22.0, newOfer.getLongitude());
        assertEquals("description2", newOfer.getDescription());

        dbHelper.close();
    }

    @Override
    public void tearDown() throws Exception {
        try {
            solo.finalize();
        } catch (Throwable e) {

            e.printStackTrace();
        }
        mActivity.finish();
        super.tearDown();

    }

}
