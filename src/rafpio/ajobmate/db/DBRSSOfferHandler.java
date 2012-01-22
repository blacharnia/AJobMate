package rafpio.ajobmate.db;

import rafpio.ajobmate.model.RSSMessage;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBRSSOfferHandler extends TableHandler {

    public static final String KEY_TITLE = "title";
    public static final String KEY_LINK = "link";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DATE = "date";

    public static final String TABLE_NAME = "rss_offers";

    public static final String TABLE_CREATE = "create table "
            + DBRSSOfferHandler.TABLE_NAME + " (" + KEY_ROWID
            + " integer primary key autoincrement, " + KEY_TITLE + " text, "
            + KEY_LINK + " text, " + KEY_DATE + " text, " + KEY_DESCRIPTION
            + ");";

    public DBRSSOfferHandler(SQLiteDatabase db) {
        mDB = db;
        tableName = TABLE_NAME;
    }

    public long create(Object object) {
        RSSMessage rssOffer = (RSSMessage) object;
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_DESCRIPTION, rssOffer.getDescription());
        initialValues.put(KEY_TITLE, rssOffer.getTitle());
        initialValues.put(KEY_LINK, rssOffer.getLink());
        initialValues.put(KEY_DATE, rssOffer.getDate());

        return mDB.insert(TABLE_NAME, null, initialValues);
    }

    public Object getItem(long rowId) {
        Cursor cursor = mDB.query(true, TABLE_NAME, null, KEY_ROWID + "="
                + rowId, null, null, null, null, null);
        if (cursor == null) {
            return null;
        } else if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            RSSMessage offer = (RSSMessage) getItemFromCursor(cursor);
            cursor.close();
            return offer;
        } else {
            return null;
        }
    }

    public Object getItemFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(KEY_ROWID));
        String title = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
        String link = cursor.getString(cursor.getColumnIndex(KEY_LINK));
        String date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
        String description = cursor.getString(cursor
                .getColumnIndex(KEY_DESCRIPTION));

        return new RSSMessage(id, title, link, date, description);
    }

    public boolean update(Object object) {
        RSSMessage rssOffer = (RSSMessage) object;

        ContentValues args = new ContentValues();
        args.put(KEY_DESCRIPTION, rssOffer.getDescription());
        args.put(KEY_TITLE, rssOffer.getTitle());
        args.put(KEY_LINK, rssOffer.getLink());
        args.put(KEY_DESCRIPTION, rssOffer.getDescription());

        return mDB.update(TABLE_NAME, args, KEY_ROWID + "=" + rssOffer.getId(),
                null) > 0;
    }

}
