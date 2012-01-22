package rafpio.ajobmate.test;

import rafpio.ajobmate.activities.MyOfferAddEditActivity;
import rafpio.ajobmate.activities.MyOffersListActivity;
import rafpio.ajobmate.db.JOffersDbAdapter;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class MyOffersListActivityNoOffersTest extends
        ActivityInstrumentationTestCase2<MyOffersListActivity> {

    private MyOffersListActivity mActivity;
    private ListView mListView;
    private TextView emptyText;
    private Button addButton;
    private Button deleteAllButton;

    private Solo solo;
    private TextView hintText;

    public MyOffersListActivityNoOffersTest() {
        super("rafpio.ajobmate", MyOffersListActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        deleteAllOffers();

        mActivity = this.getActivity();
        solo = new Solo(getInstrumentation(), mActivity);

        mListView = (ListView) mActivity
                .findViewById(rafpio.ajobmate.R.id.offers);

        emptyText = (TextView) mActivity
                .findViewById(rafpio.ajobmate.R.id.hint);

        emptyText = (TextView) mActivity
                .findViewById(rafpio.ajobmate.R.id.empty);

        hintText = (TextView) mActivity.findViewById(rafpio.ajobmate.R.id.hint);
        addButton = (Button) mActivity.findViewById(rafpio.ajobmate.R.id.add);
        deleteAllButton = (Button) mActivity
                .findViewById(rafpio.ajobmate.R.id.deleteAll);
    }

    private void deleteAllOffers() {
        JOffersDbAdapter.getInstance().init(
                getInstrumentation().getTargetContext());
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.open();
        dbHelper.archiveAllOffers();
        dbHelper.close();
    }

    public void testLayout() {
        assertNotNull(hintText);
        assertNotNull(mListView);
        assertNotNull(emptyText);
        assertNotNull(addButton);
        assertNotNull(deleteAllButton);
        assertTrue(!deleteAllButton.isEnabled());

        assertEquals(View.GONE, mListView.getVisibility());
        assertEquals(View.VISIBLE, emptyText.getVisibility());
    }

    public void testAddOfferButton() {
        solo.clickOnButton("Add new..");
        assertTrue(solo.waitForActivity(MyOfferAddEditActivity.class
                .getSimpleName()));
    }

    @Override
    public void tearDown() throws Exception {
        try {
            solo.finalize();
        } catch (Throwable e) {

            e.printStackTrace();
        }
        getActivity().finish();
        super.tearDown();
    }

}
