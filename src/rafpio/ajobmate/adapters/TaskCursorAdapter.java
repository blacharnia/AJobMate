package rafpio.ajobmate.adapters;

import rafpio.ajobmate.R;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.model.Offer;
import rafpio.ajobmate.model.Task;
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
        Task task = JOffersDbAdapter.getInstance().getTaskFromCursor(cursor);
        
        //start time
        StringBuilder startTimeText = new StringBuilder(
                res.getString(R.string.start_time));

        if(task.isStartTimeSet()){
            startTimeText.append(Common.getTimeAsString(task.getStartTime()));
        }
        else{
            startTimeText.append(res.getString(R.string.none));
        }
        ((TextView) view.findViewById(R.id.startTime)).setText(startTimeText);
        
      //end time
        StringBuilder endTimeText = new StringBuilder(
                res.getString(R.string.endtime));

        if(task.isEndTimeSet()){
            endTimeText.append(Common.getTimeAsString(task.getEndTime()));
        }
        else{
            endTimeText.append(res.getString(R.string.none));
        }
        ((TextView) view.findViewById(R.id.endTime)).setText(endTimeText);
        
      //alarm time
        StringBuilder notificationTimeText = new StringBuilder(
                res.getString(R.string.notification_time));

        if(task.isAlarmTimeSet()){
            notificationTimeText.append(Common.getTimeAsString(task.getNotificationTime()));
        }
        else{
            notificationTimeText.append(res.getString(R.string.none));
        }
        ((TextView) view.findViewById(R.id.notificationTime)).setText(notificationTimeText);
        
        //description
        ((TextView) view.findViewById(R.id.description)).setText(task.getDescription());

        // offer id
        ((TextView) view.findViewById(R.id.offerId)).setText("offer: "
                + getOfferPositionbyId(task.getOfferId()));
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
