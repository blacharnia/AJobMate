package rafpio.ajobmate.db;

import rafpio.ajobmate.model.Alarm;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBAlarmHandler extends TableHandler {

    public static final String KEY_TASK_ID = "task_id";
    public static final String KEY_ALARM_TIME = "alarm_time";
    
    protected static String TABLE_NAME = "alarms";
    
    public static final String TABLE_CREATE = "create table " + TABLE_NAME
            + " (" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_TASK_ID + " long, "
            + KEY_ALARM_TIME + " long" + ");";
    
    public DBAlarmHandler(SQLiteDatabase db) {
        mDB = db;
        tableName = TABLE_NAME;
    }
    
    @Override
    public long create(Object object) {
        Alarm alarm = (Alarm) object;
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TASK_ID, alarm.getTaskId());
        initialValues.put(KEY_ALARM_TIME, alarm.getAlarmTime());
        return mDB.insert(TABLE_NAME, null, initialValues);
    }

    @Override
    public Alarm getItem(long rowId) {
        return (Alarm) getById(rowId);
    }

    @Override
    public boolean update(Object object) {
        Alarm alarm = (Alarm) object;
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TASK_ID, alarm.getTaskId());
        initialValues.put(KEY_ALARM_TIME, alarm.getAlarmTime());
        return mDB.update(TABLE_NAME, initialValues,
                KEY_ROWID + "=" + alarm.getId(), null) > 0;
    }

    @Override
    public Object getItemFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(KEY_ROWID));
        long taskId = cursor.getLong(cursor.getColumnIndex(KEY_TASK_ID));
        long time = cursor.getLong(cursor.getColumnIndex(KEY_ALARM_TIME));
        return new Alarm(id, taskId, time);
    }
    
    public Alarm getByTaskId(long id){
        Cursor cursor = mDB.query(true, TABLE_NAME, null, KEY_TASK_ID + "="
                + id, null, null, null, null, null);
        if (cursor == null) {
            return null;
        } else if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Alarm object = (Alarm) getItemFromCursor(cursor);
            cursor.close();
            return object;
        } else {
            return null;
        }
    }

}
