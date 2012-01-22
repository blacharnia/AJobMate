package rafpio.ajobmate.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rafpio.ajobmate.model.Offer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBOfferHandler extends TableHandler {
    public static final String KEY_POSITION = "position";
    public static final String KEY_EMPLOYER = "employer";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PHONE_NR = "phone_nr";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LATITIUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ARCHIVE = "archive";

    public static final String TABLE_NAME = "offers";

    public static final String TABLE_CREATE = "create table "
            + DBOfferHandler.TABLE_NAME + " (" + KEY_ROWID
            + " integer primary key autoincrement, " + KEY_POSITION
            + " text not null, " + KEY_EMPLOYER + " text not null, "
            + KEY_LOCATION + " text, " + KEY_DESCRIPTION + " text, "
            + KEY_LATITIUDE + " float, " + KEY_LONGITUDE + " float, "
            + KEY_PHONE_NR + " text, " + KEY_EMAIL + " text, " 
             + KEY_ARCHIVE + " integer" + ");";

    public DBOfferHandler(SQLiteDatabase db) {
        mDB = db;
        tableName = TABLE_NAME;
    }

    public long create(Object object) {
        Offer offer = (Offer) object;
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_POSITION, offer.getPosition());
        initialValues.put(KEY_EMPLOYER, offer.getEmployer());
        initialValues.put(KEY_PHONE_NR, offer.getPhoneNr());
        initialValues.put(KEY_EMAIL, offer.getEmail());
        initialValues.put(KEY_LOCATION, offer.getLocation());
        initialValues.put(KEY_DESCRIPTION, offer.getDescription());
        initialValues.put(KEY_LATITIUDE, offer.getLatitude());
        initialValues.put(KEY_LONGITUDE, offer.getLongitude());
        initialValues.put(KEY_ARCHIVE, offer.isArchive());

        return mDB.insert(TABLE_NAME, null, initialValues);
    }

    public Object getItem(long rowId) {
        Cursor cursor = mDB.query(true, TABLE_NAME, null, KEY_ROWID + "="
                + rowId, null, null, null, null, null);
        if (cursor == null) {
            return null;
        } else if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Offer offer = (Offer) getItemFromCursor(cursor);
            cursor.close();
            return offer;
        } else {
            return null;
        }
    }

    public Object getItemFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(KEY_ROWID));
        String position = cursor.getString(cursor.getColumnIndex(KEY_POSITION));
        String employer = cursor.getString(cursor.getColumnIndex(KEY_EMPLOYER));
        String email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL));
        String phone = cursor.getString(cursor.getColumnIndex(KEY_PHONE_NR));
        String location = cursor.getString(cursor.getColumnIndex(KEY_LOCATION));
        String description = cursor.getString(cursor
                .getColumnIndex(KEY_DESCRIPTION));
        float latitude = cursor.getFloat(cursor.getColumnIndex(KEY_LATITIUDE));
        float longitude = cursor.getFloat(cursor.getColumnIndex(KEY_LONGITUDE));
        boolean archive = cursor.getInt(cursor.getColumnIndex(KEY_ARCHIVE)) == 1 ? true
                : false;

        return new Offer(id, position, employer, phone, email, location,
                description, latitude, longitude, archive);
    }

    public boolean update(Object object) {
        Offer offer = (Offer) object;

        ContentValues args = new ContentValues();
        args.put(KEY_POSITION, offer.getPosition());
        args.put(KEY_EMPLOYER, offer.getEmployer());
        args.put(KEY_PHONE_NR, offer.getPhoneNr());
        args.put(KEY_EMAIL, offer.getEmail());
        args.put(KEY_LOCATION, offer.getLocation());
        args.put(KEY_DESCRIPTION, offer.getDescription());
        args.put(KEY_LATITIUDE, offer.getLatitude());
        args.put(KEY_LONGITUDE, offer.getLongitude());
        args.put(KEY_ARCHIVE, offer.isArchive());

        return mDB.update(TABLE_NAME, args, KEY_ROWID + "=" + offer.getId(),
                null) > 0;
    }

    public List<String> getRecentPositions() {
        List<String> positions = new ArrayList<String>();

        Cursor cursor = getRecent();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Offer offer = (Offer) getItemFromCursor(cursor);
                positions.add(offer.getPosition());
            } while (cursor.moveToNext());
        } else {
            positions = new ArrayList<String>();
        }
        cursor.close();

        return positions;
    }
    
    public List<String> getPositions() {
        List<String> positions = new ArrayList<String>();

        Cursor cursor = getAll();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Offer offer = (Offer) getItemFromCursor(cursor);
                positions.add(offer.getPosition());
            } while (cursor.moveToNext());
        } else {
            positions = new ArrayList<String>();
        }
        cursor.close();

        return positions;
    }

    public List<Long> getIds() {
        List<Long> ids = new ArrayList<Long>();
        Cursor cursor = getAll();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Offer offer = (Offer) getItemFromCursor(cursor);
                ids.add(offer.getId());
            } while (cursor.moveToNext());
        } else {
            ids = Collections.emptyList();
        }
        cursor.close();

        return ids;
    }
    
    public boolean archive(long taskId){
        return setArchive(taskId, true);
    }
    
    public boolean unArchive(long taskId){
        return setArchive(taskId, false);
    }
    
    public boolean setArchive(long taskId, boolean archive){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ARCHIVE, archive);
        
        return mDB.update(TABLE_NAME, initialValues,
                KEY_ROWID + "=" + taskId, null) > 0;
    }
    
    public void archiveAll(){
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(TABLE_NAME)
            .append(" SET ")
            .append(KEY_ARCHIVE)
            .append( " = 1 ;");
        
        mDB.execSQL(query.toString());
    }

    public boolean deleteAllRecent() {
        return mDB.delete(tableName, KEY_ARCHIVE + "= 0", null) > 0;
    }
	
	public boolean deleteAllArchived() {
        return mDB.delete(tableName, KEY_ARCHIVE + "= 1", null) > 0;
    }
	
	public void unarchiveAll(){
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(TABLE_NAME)
            .append(" SET ")
            .append(KEY_ARCHIVE)
            .append( " = 0 ;");
        
        mDB.execSQL(query.toString());
    }

}
