package rafpio.ajobmate.activities;

import java.util.Observable;
import java.util.Observer;

import rafpio.ajobmate.R;
import rafpio.ajobmate.adapters.RssOfferCursorAdapter;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.core.EventHandler;
import rafpio.ajobmate.db.DBOfferHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RSSOffersListActivity extends Activity implements Observer {

    private ListView mRSSOffers;
    private RssOfferCursorAdapter mRssListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rss_offers);

        mRSSOffers = (ListView) findViewById(R.id.rss_offers_list);
        mRSSOffers.setEmptyView(findViewById(R.id.empty));
        findViewById(R.id.reload).setOnClickListener(mReloadClickListener);

        EventHandler.getInstance().addObserver(this);
        mRSSOffers.setOnItemClickListener(mItemClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showDialog(Common.LOADING_DIALOG);
        if (getRssOfferCnt() == 0) {
            EventHandler.getInstance().requestRssOffers();
        } else {
            update(null, null);
        }
    }

    private OnClickListener mReloadClickListener = new OnClickListener() {

        public void onClick(View v) {
            showDialog(Common.LOADING_DIALOG);
            EventHandler.getInstance().requestRssOffers();
        }
    };

    private int getRssOfferCnt() {
        int cnt = 0;
        final JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.open();
        Cursor cursor = dbHelper.getAllRssOffers();
        if (cursor != null) {
            cnt = cursor.getCount();
            cursor.close();
        }
        dbHelper.close();
        return cnt;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case Common.LOADING_DIALOG:
            ProgressDialog loadingDialog = new ProgressDialog(this);
            loadingDialog.setMessage("Loading...");
            loadingDialog.setIndeterminate(true);
            return loadingDialog;
        default:
            break;
        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void onDestroy() {
        EventHandler.getInstance().deleteObserver(this);
        super.onDestroy();
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {

            Intent intent = new Intent(RSSOffersListActivity.this,
                    RssOfferDetailActivity.class);
            intent.putExtra(DBOfferHandler.KEY_ROWID, arg3);
            startActivity(intent);
        }
    };

    public void update(Observable observable, Object data) {
        loadRSSOffers();
        dismissDialog(Common.LOADING_DIALOG);
    }

    private void loadRSSOffers() {
        final JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.open();
        Cursor cursor = dbHelper.getAllRssOffers();
        if (cursor != null) {
            if (mRssListAdapter == null) {
                mRssListAdapter = new RssOfferCursorAdapter(
                        getApplicationContext(), cursor);
                mRSSOffers.setAdapter(mRssListAdapter);
            } else {
                mRssListAdapter.changeCursor(cursor);
            }
            findViewById(R.id.reload).setEnabled(true);

        }
        dbHelper.close();
    }

}
