package rafpio.ajobmate.test;

import rafpio.ajobmate.activities.MyOfferAddEditActivity;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.model.Offer;
import android.app.Instrumentation;
import android.content.Context;
import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class MyOfferAddTest extends
        ActivityInstrumentationTestCase2<MyOfferAddEditActivity> {

    private MyOfferAddEditActivity mActivity;
    private Solo solo;
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
    private Context context;

    public MyOfferAddTest() {
        super("rafpio.ajobmate", MyOfferAddEditActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        instrumentation = getInstrumentation();
        mActivity = this.getActivity();
        solo = new Solo(instrumentation, mActivity);
        context = instrumentation.getTargetContext();

        TestUtils.deleteAllOffers(context);

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

    public void testLayout() {
        assertNotNull(hintText);
        assertNotNull(confirmButton);
        assertNotNull(cancelButton);
    }

    public void testAddEmptyOffer() {
        solo.clickOnButton("Confirm");
        String msg = mActivity.getResources().getString(
                rafpio.ajobmate.R.string.enter_at_least_position_of_the_offer);
        solo.searchText(msg);
    }

    public void testAddOffer() throws InterruptedException {
        solo.enterText(position, "position1");
        solo.enterText(employer, "employer1");
        solo.enterText(phoneNumber, "0700");
        solo.enterText(email, "email@example.com");
        solo.enterText(location, "Warsaw");
        solo.enterText(latitude, "52.0");
        solo.enterText(longitude, "21.0");
        solo.enterText(description, "description1");

        solo.clickOnButton("Confirm");
        Thread.sleep(2000);

        JOffersDbAdapter.getInstance().init(instrumentation.getTargetContext());
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.open();
        Cursor cursor = dbHelper.getAllOffers();
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        Offer offer = TestUtils.getItemFromCursor(cursor);
        assertEquals("position1", offer.getPosition());
        assertEquals("employer1", offer.getEmployer());
        assertEquals("0700", offer.getPhoneNr());
        assertEquals("email@example.com", offer.getEmail());
        assertEquals("Warsaw", offer.getLocation());
        assertEquals(52.0, offer.getLatitude());
        assertEquals(21.0, offer.getLongitude());
        assertEquals("description1", offer.getDescription());

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
