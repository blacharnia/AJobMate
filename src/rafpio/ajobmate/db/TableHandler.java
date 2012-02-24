package rafpio.ajobmate.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class TableHandler {
    public static String KEY_ROWID = "_id";
    public static final String KEY_ARCHIVE = "archive";
    String tableName;
    protected SQLiteDatabase mDB;

    abstract public long create(Object object);

    abstract public Object getItem(long rowId);

    abstract public boolean update(Object object);

    abstract public Object getItemFromCursor(Cursor cursor);

    public boolean delete(long rowId) {
        return mDB.delete(tableName, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public long deleteAll() {
        return mDB.delete(tableName, null, null);
    }

    public Cursor getAll() {
        return mDB.query(tableName, null, null, null, null, null, null);
    }

    public Cursor getRecent() {
        return mDB.query(tableName, null, KEY_ARCHIVE + " = 0", null, null,
                null, null);
    }

    public Cursor getArchived() {
        return mDB.query(tableName, null, KEY_ARCHIVE + " = 1", null, null,
                null, null);
    }

    public List<Object> getAllAsList() {
        List<Object> list = null;
        Cursor cursor = getAll();
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                list = Collections.emptyList();
            } else {
                list = new ArrayList<Object>();
                cursor.moveToFirst();
                do {
                    list.add(getItemFromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } else {
            list = Collections.emptyList();
        }
        return list;
    }

    public List<Object> getRecentAsList() {
        List<Object> list = null;
        Cursor cursor = getRecent();
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                list = Collections.emptyList();
            } else {
                list = new ArrayList<Object>();
                cursor.moveToFirst();
                do {
                    list.add(getItemFromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } else {
            list = Collections.emptyList();
        }
        return list;
    }

}
