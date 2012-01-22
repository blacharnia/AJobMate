package rafpio.ajobmate.test;

import rafpio.ajobmate.activities.AJobMateMainActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class AJobMateMainActivityTest extends
        ActivityInstrumentationTestCase2<AJobMateMainActivity> {

    private AJobMateMainActivity mActivity;
    private ListView mListView;
    private Solo solo;

    public AJobMateMainActivityTest() {
        super("rafpio.ajobmate", AJobMateMainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        mActivity = this.getActivity();
        mListView = (ListView) mActivity
                .findViewById(rafpio.ajobmate.R.id.options_list);
    }

    public void testListViewUI() {
        assertNotNull(mListView);

        ListAdapter adapter = mListView.getAdapter();
        assertNotNull(adapter);

        assertEquals(rafpio.ajobmate.core.DataContainer.OPTIONS.length,
                adapter.getCount());

        TextView tv1 = (TextView) mListView.getAdapter().getView(0, null, null);
        assertEquals(rafpio.ajobmate.core.DataContainer.OPTIONS[0],
                tv1.getText());

        TextView tv2 = (TextView) mListView.getAdapter().getView(1, null, null);
        assertEquals(rafpio.ajobmate.core.DataContainer.OPTIONS[1],
                tv2.getText());

        TextView tv3 = (TextView) mListView.getAdapter().getView(2, null, null);
        assertEquals(rafpio.ajobmate.core.DataContainer.OPTIONS[2],
                tv3.getText());

    }

    public void testStartingOptions1() {
        assertNotNull(solo.clickInList(1));
        assertTrue(solo.waitForActivity("MyOffersListActivity"));
    }
    
    public void testStartingOption2(){
        assertNotNull(solo.clickInList(2));
        assertTrue(solo.waitForActivity("RSSOffersListActivity"));
    }
    
    public void testStartingOption3(){
        assertNotNull(solo.clickInList(3));
        assertTrue(solo.waitForActivity("TaskListActivity"));
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
