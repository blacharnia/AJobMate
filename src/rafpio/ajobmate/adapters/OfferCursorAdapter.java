package rafpio.ajobmate.adapters;

import rafpio.ajobmate.R;
import rafpio.ajobmate.db.DBOfferHandler;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class OfferCursorAdapter extends CursorAdapter {

    public OfferCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        setupItem(cursor, view);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.offers_row,
                parent, false);
        setupItem(cursor, view);
        return view;
    }

    private void setupItem(Cursor cursor, View view) {
        String position = cursor.getString(cursor
                .getColumnIndex(DBOfferHandler.KEY_POSITION));
        if (TextUtils.isEmpty(position)) {
            ((TextView) view.findViewById(R.id.position))
                    .setText("No position provided");
        } else {
            ((TextView) view.findViewById(R.id.position)).setText(position);
        }

        String employer = cursor.getString(cursor
                .getColumnIndex(DBOfferHandler.KEY_EMPLOYER));
        if (TextUtils.isEmpty(employer)) {
            ((TextView) view.findViewById(R.id.employer))
                    .setText("No employer provided");
        } else {
            ((TextView) view.findViewById(R.id.employer)).setText(employer);
        }

        String phoneNumber = cursor.getString(cursor
                .getColumnIndex(DBOfferHandler.KEY_PHONE_NR));
        if (TextUtils.isEmpty(phoneNumber)) {
            ((TextView) view.findViewById(R.id.phoneNr))
                    .setText("No phone number provided");
        } else {
            ((TextView) view.findViewById(R.id.phoneNr)).setText(phoneNumber);
        }

        String email = cursor.getString(cursor
                .getColumnIndex(DBOfferHandler.KEY_EMAIL));
        if (TextUtils.isEmpty(email)) {
            ((TextView) view.findViewById(R.id.email))
                    .setText("No email provided");
        } else {
            ((TextView) view.findViewById(R.id.email)).setText(email);
        }
    }
}
