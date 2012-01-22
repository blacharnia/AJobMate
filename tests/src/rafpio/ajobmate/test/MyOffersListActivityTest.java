package rafpio.ajobmate.test;

import rafpio.ajobmate.activities.MyOfferDetailActivity;
import rafpio.ajobmate.activities.MyOffersListActivity;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.model.Offer;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class MyOffersListActivityTest extends
        ActivityInstrumentationTestCase2<MyOffersListActivity> {

    private MyOffersListActivity mActivity;
    private ListView mListView;
    private TextView emptyText;
    private Button addButton;

    private Solo solo;
    private Button deleteAllButton;
    private TextView hintText;

    public MyOffersListActivityTest() {
        super("rafpio.ajobmate", MyOffersListActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setupOffers();

        mActivity = this.getActivity();
        solo = new Solo(getInstrumentation(), mActivity);

        mListView = (ListView) mActivity
                .findViewById(rafpio.ajobmate.R.id.offers);

        emptyText = (TextView) mActivity
                .findViewById(rafpio.ajobmate.R.id.empty);

        hintText = (TextView) mActivity.findViewById(rafpio.ajobmate.R.id.hint);

        addButton = (Button) mActivity.findViewById(rafpio.ajobmate.R.id.add);
        deleteAllButton = (Button) mActivity
                .findViewById(rafpio.ajobmate.R.id.deleteAll);
    }

    private void setupOffers() {
        JOffersDbAdapter.getInstance().init(
                getInstrumentation().getTargetContext());
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.open();
        dbHelper.archiveAllOffers();
        dbHelper.createOffer(new Offer(0, "position1", "employer1", "0700",
                "email@example.com", "Warsaw", "testDescription", 52d, 21d));
        dbHelper.close();
    }

    public void testLayout() {
        assertNotNull(mListView);
        assertEquals(View.VISIBLE, mListView.getVisibility());
        assertEquals(View.GONE, emptyText.getVisibility());
        assertNotNull(emptyText);
        assertNotNull(hintText);
        assertNotNull(addButton);
        assertNotNull(deleteAllButton);
        assertTrue(deleteAllButton.isEnabled());

        ListAdapter adapter = mListView.getAdapter();
        assertNotNull(adapter);
        assertEquals(1, adapter.getCount());
    }

    public void testClickOnListItem() {
        assertNotNull(solo.clickInList(0));
        assertTrue(solo.waitForActivity(MyOfferDetailActivity.class
                .getSimpleName()));
    }

    public void testDeleteAllButton() {
        Resources res = mActivity.getResources();
        String buttonText = res.getString(rafpio.ajobmate.R.string.delete_all);
        solo.clickOnButton(buttonText);

        String msg = res
                .getString(rafpio.ajobmate.R.string.confirm_delete_all_offers);
        assertTrue(solo.searchText(msg));
    }

    public void testLongClickOnListItem() {
        assertNotNull(solo.clickLongInList(0));
        assertTrue(solo.searchText("Delete"));
    }

    public void testLongClickOnListItemAndConfirm() throws InterruptedException {
        assertNotNull(solo.clickLongInList(0));
        assertTrue(solo.searchText("Delete"));
        solo.clickOnText("Delete offer");

        // not very nice...
        Thread.sleep(3000);

        ListAdapter adapter = mListView.getAdapter();
        assertNotNull(adapter);
        assertEquals(0, adapter.getCount());
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
