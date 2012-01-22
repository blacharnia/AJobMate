package rafpio.ajobmate.test;

import rafpio.ajobmate.activities.MyOfferDetailActivity;
import rafpio.ajobmate.db.TableHandler;
import rafpio.ajobmate.model.Offer;
import rafpio.ajobmate.model.Task;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class MyOfferDetailTestWithTasks extends
        ActivityInstrumentationTestCase2<MyOfferDetailActivity> {

    private MyOfferDetailActivity mActivity;
    private Solo solo;
    private TextView position;

    private TextView employer;
    private Button phoneNumber;
    private Button email;
    private Button gpsLocation;
    private TextView description;
    private TextView noTasks;
    private Offer offer;
    private Task task;
    private ListView tasksListView;

    private Instrumentation instrumentation;
    private Context context;

    public MyOfferDetailTestWithTasks() {
        super("rafpio.ajobmate", MyOfferDetailActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        instrumentation = getInstrumentation();
        context = instrumentation.getTargetContext();
        offer = TestUtils.createTestOffer(context);
        task = TestUtils.createTestTask(context, offer.getId());

        Intent intent = new Intent(context, MyOfferDetailActivity.class);
        intent.putExtra(TableHandler.KEY_ROWID, offer.getId());
        setActivityIntent(intent);
        
        mActivity = this.getActivity();
        solo = new Solo(instrumentation, mActivity);

        cacheControls();
    }

    private void cacheControls() {

        position = (TextView) mActivity
                .findViewById(rafpio.ajobmate.R.id.position);
        employer = (TextView) mActivity
                .findViewById(rafpio.ajobmate.R.id.employer);
        phoneNumber = (Button) mActivity
                .findViewById(rafpio.ajobmate.R.id.phone_nr);
        email = (Button) mActivity.findViewById(rafpio.ajobmate.R.id.email);
        gpsLocation = (Button) mActivity
                .findViewById(rafpio.ajobmate.R.id.gps_location);
        description = (TextView) mActivity
                .findViewById(rafpio.ajobmate.R.id.description);
        noTasks = (TextView) mActivity.findViewById(rafpio.ajobmate.R.id.empty);
        tasksListView = (ListView) mActivity.findViewById(rafpio.ajobmate.R.id.tasksForOffer);
    }

    public void testLayout() {
        
        assertTrue(phoneNumber.isEnabled());
        assertTrue(email.isEnabled());
        assertTrue(gpsLocation.isEnabled());
        assertTrue(phoneNumber.isEnabled());
        
        assertEquals(offer.getPosition(), position.getText());
        assertEquals(offer.getEmployer(), employer.getText());
        assertEquals(offer.getPhoneNr(), phoneNumber.getText());
        assertEquals(offer.getEmail(), email.getText());
        assertEquals(offer.getLatitude() + "\n" + offer.getLongitude(), gpsLocation.getText());
        assertEquals(offer.getDescription(), description.getText());
        
        assertEquals(View.GONE, noTasks.getVisibility());
        assertEquals(View.VISIBLE, tasksListView.getVisibility());
        assertEquals(1, tasksListView.getCount());
    }

    @Override
    public void tearDown() throws Exception {
        try {
            solo.finalize();
        } catch (Throwable e) {

            e.printStackTrace();
        }
        //TestUtils.deleteAllOffers(context);
        mActivity.finish();
        super.tearDown();

    }

}
