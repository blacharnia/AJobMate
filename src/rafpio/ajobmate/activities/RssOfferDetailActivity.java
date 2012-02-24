package rafpio.ajobmate.activities;

import rafpio.ajobmate.R;
import rafpio.ajobmate.db.DBOfferHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.model.RSSMessage;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RssOfferDetailActivity extends Activity {

    private RSSMessage rssOffer;
    private static final int CALL_OFFERS_LIST_FOM_IMPORT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_offer_details);
        ((Button) findViewById(R.id.button_import))
                .setOnClickListener(mImportClickListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkInputParams();
        if (rssOffer == null) {
            finish();
        } else {
            setupFields();
        }
    }

    private void setupFields() {
        ((TextView) findViewById(R.id.title)).setText(rssOffer.getTitle());
        ((TextView) findViewById(R.id.date)).setText(rssOffer.getDate());

        final TextView link = ((TextView) findViewById(R.id.link));
        link.setText(rssOffer.getLink());
        Linkify.addLinks(link, Linkify.ALL);

        final TextView description = ((TextView) findViewById(R.id.description));
        description.setText(Html.fromHtml(rssOffer.getDescription()));
        description.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private OnClickListener mImportClickListener = new OnClickListener() {

        public void onClick(View v) {
            JOffersDbAdapter.getInstance().createOffer(rssOffer);
            startActivityForResult(new Intent(getApplicationContext(),
                    MyOffersListActivity.class), CALL_OFFERS_LIST_FOM_IMPORT);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CALL_OFFERS_LIST_FOM_IMPORT) {
            finish();
        }
    }

    private void checkInputParams() {
        long rowId = -1;

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            rowId = extras.getLong(DBOfferHandler.KEY_ROWID, -1);
            if (-1 != rowId) {
                rssOffer = getRssOffer(rowId);
            }
        }

    }

    private RSSMessage getRssOffer(long id) {
        return JOffersDbAdapter.getInstance().getRssOffer(id);
    }
}
