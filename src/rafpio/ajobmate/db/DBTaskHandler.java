package rafpio.ajobmate.db;

import rafpio.ajobmate.model.Task;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBTaskHandler extends TableHandler {

    public static final String KEY_OFFER_ID = "offer_id";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_START_TIME = "startTime";
    public static final String KEY_NOTIFICATION_TIME = "notificationTime";
    public static final String KEY_END_TIME = "endTime";
    public static final String KEY_CATEGORY = "category";

    protected static String TABLE_NAME = "tasks";

    public static final String TABLE_CREATE = "create table " + TABLE_NAME
            + " (" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_DESCRIPTION + " text not null, " + KEY_OFFER_ID + " int, "
            + KEY_START_TIME + " long, " + KEY_END_TIME + " long, "
            + KEY_NOTIFICATION_TIME + " long, " + KEY_CATEGORY + " text, "
            + KEY_ARCHIVE + " integer" + ");";

    public DBTaskHandler(SQLiteDatabase db) {
        mDB = db;
        tableName = TABLE_NAME;
    }

    public long create(Object dataObject) {
        Task task = (Task) dataObject;
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DESCRIPTION, task.getDescription());
        initialValues.put(KEY_OFFER_ID, task.getOfferId());
        initialValues.put(KEY_START_TIME, task.getStartTime());
        initialValues.put(KEY_END_TIME, task.getEndTime());
        initialValues.put(KEY_NOTIFICATION_TIME, task.getNotificationTime());
        initialValues.put(KEY_CATEGORY, task.getCategory());
        initialValues.put(KEY_ARCHIVE, task.isArchive());
        return mDB.insert(TABLE_NAME, null, initialValues);
    }

    public Object getItem(long rowId) throws SQLException {
        Cursor cursor = mDB.query(true, TABLE_NAME, null, KEY_ROWID + "="
                + rowId, null, null, null, null, null);
        if (cursor == null) {
            return null;
        } else if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Task task = (Task) getItemFromCursor(cursor);
            cursor.close();
            return task;
        } else {
            return null;
        }

    }

    public boolean update(Object dataObject) {
        Task task = (Task) dataObject;
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DESCRIPTION, task.getDescription());
        initialValues.put(KEY_OFFER_ID, task.getOfferId());
        initialValues.put(KEY_START_TIME, task.getStartTime());
        initialValues.put(KEY_END_TIME, task.getEndTime());
        initialValues.put(KEY_ARCHIVE, task.isArchive());
        initialValues.put(KEY_NOTIFICATION_TIME, task.getNotificationTime());
        return mDB.update(TABLE_NAME, initialValues,
                KEY_ROWID + "=" + task.getId(), null) > 0;
    }

    public boolean archive(long taskId) {
        return setArchive(taskId, true);
    }

    public boolean unArchive(long taskId) {
        return setArchive(taskId, false);
    }

    public boolean setArchive(long taskId, boolean archive) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ARCHIVE, archive);

        return mDB.update(TABLE_NAME, initialValues, KEY_ROWID + "=" + taskId,
                null) > 0;
    }

    public void archiveAll() {
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(TABLE_NAME).append(" SET ").append(KEY_ARCHIVE)
                .append(" = 1 ;");

        mDB.execSQL(query.toString());
    }

    public void unarchiveAll() {
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(TABLE_NAME).append(" SET ").append(KEY_ARCHIVE)
                .append(" = 0 ;");

        mDB.execSQL(query.toString());
    }

    @Override
    public Object getItemFromCursor(Cursor cursor) {
        Long id = cursor.getLong(cursor.getColumnIndex(KEY_ROWID));
        String description = cursor.getString(cursor
                .getColumnIndex(KEY_DESCRIPTION));
        int offerId = cursor.getInt(cursor.getColumnIndex(KEY_OFFER_ID));
        long startTime = cursor.getLong(cursor.getColumnIndex(KEY_START_TIME));
        long endTime = cursor.getLong(cursor.getColumnIndex(KEY_END_TIME));
        long notificationTime = cursor.getLong(cursor
                .getColumnIndex(KEY_NOTIFICATION_TIME));
        String category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));

        boolean archive = cursor.getInt(cursor.getColumnIndex(KEY_ARCHIVE)) == 1 ? true
                : false;
        return new Task(id, description, offerId, startTime, endTime,
                notificationTime, category, archive);
    }

    public Cursor getAllForOffer(long mRowId) {
        return mDB.query(tableName, null, KEY_OFFER_ID + " = " + mRowId, null,
                null, null, null);
    }

    public Cursor getRecentForOffer(long mRowId) {
        return mDB.query(tableName, null, KEY_OFFER_ID + " = " + mRowId
                + " AND " + KEY_ARCHIVE + " = 0", null, null, null, null);
    }

    public void resetNotification(long taskId) {
        Task task = (Task) getItem(taskId);
        task.setNotificationTime(0);
        update(task);
    }

    public Cursor getArchivedForOffer(long mRowId) {
        return mDB.query(tableName, null, KEY_OFFER_ID + " = " + mRowId
                + " AND " + KEY_ARCHIVE + " = 1", null, null, null, null);
    }

    public boolean deleteAllRecent() {
        return mDB.delete(tableName, KEY_ARCHIVE + "= 0", null) > 0;
    }

    public boolean deleteAllArchived() {
        return mDB.delete(tableName, KEY_ARCHIVE + "= 1", null) > 0;
    }

}
