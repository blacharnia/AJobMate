package rafpio.ajobmate.adapters;

import rafpio.ajobmate.R;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.db.DBTaskHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.model.Offer;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TaskCursorAdapter extends CursorAdapter {
    public TaskCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        setupItem(context, cursor, view);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.tasks_row,
                parent, false);
        setupItem(context, cursor, view);
        return view;
    }

    private void setupItem(Context context, Cursor cursor, View view) {

        Resources res = context.getResources();

        String description = cursor.getString(cursor
                .getColumnIndex(DBTaskHandler.KEY_DESCRIPTION));
        long startTime = cursor.getLong(cursor
                .getColumnIndex(DBTaskHandler.KEY_START_TIME));
        long endTime = cursor.getLong(cursor
                .getColumnIndex(DBTaskHandler.KEY_END_TIME));
        long notificationTime = cursor.getLong(cursor
                .getColumnIndex(DBTaskHandler.KEY_NOTIFICATION_TIME));
        long offerId = cursor.getLong(cursor
                .getColumnIndex(DBTaskHandler.KEY_OFFER_ID));

        ((TextView) view.findViewById(R.id.description)).setText(description);

        // start time
        StringBuilder startTimeText = new StringBuilder(
                res.getString(R.string.start_time));
        if (startTime > 0) {
            startTimeText.append(Common.getTimeAsString(startTime));
        } else {
            startTimeText.append(res.getString(R.string.none));
        }
        ((TextView) view.findViewById(R.id.startTime)).setText(startTimeText);

        // end time
        StringBuilder endTimeText = new StringBuilder(
                res.getString(R.string.endtime));
        if (endTime > 0) {
            endTimeText.append(Common.getTimeAsString(endTime));
        } else {
            endTimeText.append(res.getString(R.string.none));
        }
        ((TextView) view.findViewById(R.id.endTime)).setText(endTimeText);

        // notification time
        StringBuilder notificationTimeText = new StringBuilder(
                res.getString(R.string.notification_time));
        if (notificationTime > 0) {
            notificationTimeText.append(Common
                    .getTimeAsString(notificationTime));
        } else {
            notificationTimeText.append(res.getString(R.string.none));
        }
        ((TextView) view.findViewById(R.id.notificationTime))
                .setText(notificationTimeText);

        // offer id
        ((TextView) view.findViewById(R.id.offerId)).setText("offer: "
                + getOfferPositionbyId(offerId));
    }

    private String getOfferPositionbyId(long id) {
        String position;
        Offer offer = JOffersDbAdapter.getInstance().getOffer(id);
        if (offer != null) {
            position = offer.getPosition();
        } else {
            position = "none";
        }
        return position;
    }
}
