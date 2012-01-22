package rafpio.ajobmate.adapters;

import rafpio.ajobmate.R;
import rafpio.ajobmate.db.DBRSSOfferHandler;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class RssOfferCursorAdapter extends CursorAdapter {

    public RssOfferCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        setupItem(cursor, view);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.rss_offer,
                parent, false);
        setupItem(cursor, view);
        return view;
    }

    private void setupItem(Cursor cursor, View view) {
        int col = cursor.getColumnIndex(DBRSSOfferHandler.KEY_TITLE);
        String title = col > -1 ? cursor.getString(col) : "";

        col = cursor.getColumnIndex(DBRSSOfferHandler.KEY_DATE);
        String date = col > -1 ? cursor.getString(col) : "";

        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.date)).setText(date);

    }
}
