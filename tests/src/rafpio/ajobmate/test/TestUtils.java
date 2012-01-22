package rafpio.ajobmate.test;

import rafpio.ajobmate.db.DBOfferHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.db.TableHandler;
import rafpio.ajobmate.model.Offer;
import rafpio.ajobmate.model.Task;
import android.content.Context;
import android.database.Cursor;

public class TestUtils {
    public static Offer createTestOffer(Context context) {
        Offer offer = new Offer(0, "position1", "employer1", "0700",
                "email@example.com", "Warsaw", "testDescription", 52d, 21d);

        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.init(context);
        dbHelper.open();
        dbHelper.archiveAllOffers();
        long id = dbHelper.createOffer(offer);
        offer.setId(id);
        dbHelper.close();
        return offer;
    }

    public static Task createTestTask(Context context, long offerId) {
        long now = System.currentTimeMillis();

        Task task = new Task(0, "TestTask1", offerId, now, now + 3600 * 1000,
                "testcategory", true);
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.init(context);
        dbHelper.open();
        long id = dbHelper.createTask(task);
        task.setId(id);
        dbHelper.close();
        return task;
    }

    public static void deleteAllOffers(Context context) {
        JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
        dbHelper.open();
        dbHelper.archiveAllOffers();
        dbHelper.close();
    }

    public static Offer getItemFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(TableHandler.KEY_ROWID));
        String position = cursor.getString(cursor
                .getColumnIndex(DBOfferHandler.KEY_POSITION));
        String employer = cursor.getString(cursor
                .getColumnIndex(DBOfferHandler.KEY_EMPLOYER));
        String email = cursor.getString(cursor
                .getColumnIndex(DBOfferHandler.KEY_EMAIL));
        String phone = cursor.getString(cursor
                .getColumnIndex(DBOfferHandler.KEY_PHONE_NR));
        String location = cursor.getString(cursor
                .getColumnIndex(DBOfferHandler.KEY_LOCATION));
        String description = cursor.getString(cursor
                .getColumnIndex(DBOfferHandler.KEY_DESCRIPTION));
        float latitude = cursor.getFloat(cursor
                .getColumnIndex(DBOfferHandler.KEY_LATITIUDE));
        float longitude = cursor.getFloat(cursor
                .getColumnIndex(DBOfferHandler.KEY_LONGITUDE));

        return new Offer(id, position, employer, phone, email, location,
                description, latitude, longitude);
    }
}
