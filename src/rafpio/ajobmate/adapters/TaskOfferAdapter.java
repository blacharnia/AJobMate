package rafpio.ajobmate.adapters;

import rafpio.ajobmate.db.DBOfferHandler;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TaskOfferAdapter extends CursorAdapter {

    public TaskOfferAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void bindView(View arg0, Context arg1, Cursor arg2) {
        TextView tv = (TextView) arg0;
        tv.setText(arg2.getString(arg2
                .getColumnIndex(DBOfferHandler.KEY_DESCRIPTION)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        TextView tv = new TextView(context);
        tv.setText(cursor.getString(cursor
                .getColumnIndex(DBOfferHandler.KEY_DESCRIPTION)));
        tv.setTag(cursor.getPosition());
        return tv;
    }

}
