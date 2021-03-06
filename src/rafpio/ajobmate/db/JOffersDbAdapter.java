package rafpio.ajobmate.db;

import java.util.List;

import rafpio.ajobmate.R;
import rafpio.ajobmate.model.Alarm;
import rafpio.ajobmate.model.Offer;
import rafpio.ajobmate.model.RSSMessage;
import rafpio.ajobmate.model.Task;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class JOffersDbAdapter {

    private static final String DATABASE_NAME = "AJobMateDb";
    private static final int DATABASE_VERSION = 3;
    private static final String TAG = "JOffersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private Context mCtx;
    private DBOfferHandler mOfferHandler;
    private DBTaskHandler mTaskHandler;
    private DBRSSOfferHandler mRSSOfferHandler;
    private DBAlarmHandler mAlarmHandler;

    private static class JOffersDbAdapterHolder {
        private static final JOffersDbAdapter INSTANCE = new JOffersDbAdapter();
    }

    public static JOffersDbAdapter getInstance() {
        return JOffersDbAdapterHolder.INSTANCE;
    }

    private JOffersDbAdapter() {
    }

    public void open(Context ctxt) {
        if (mDbHelper == null) {
            mCtx = ctxt;
            open();
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DBOfferHandler.TABLE_CREATE);
            db.execSQL(DBTaskHandler.TABLE_CREATE);
            db.execSQL(DBRSSOfferHandler.TABLE_CREATE);
            db.execSQL(DBAlarmHandler.TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DBOfferHandler.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBTaskHandler.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBRSSOfferHandler.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBAlarmHandler.TABLE_NAME);

            onCreate(db);
        }
    }

    private JOffersDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        try {
            mDb = mDbHelper.getWritableDatabase();
            mOfferHandler = new DBOfferHandler(mDb);
            mTaskHandler = new DBTaskHandler(mDb);
            mRSSOfferHandler = new DBRSSOfferHandler(mDb);
            mAlarmHandler = new DBAlarmHandler(mDb);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
            mDbHelper = null;
        }
    }

    public long createOffer(Offer offer) {
        return mOfferHandler.create(offer);
    }

    public long createOffer(final RSSMessage rssOffer) {
        return mOfferHandler.create(new Offer(0, rssOffer.getTitle(),
                "unknown employer", "", "", "", rssOffer.getDescription(), 0d,
                0d, false));
    }

    public boolean archiveOffer(long rowId) {
        return mOfferHandler.archive(rowId);
    }

    public long deleteAllRssOffers() {
        return mRSSOfferHandler.deleteAll();
    }

    public void archiveAllOffers() {
        mOfferHandler.archiveAll();
    }

    public void deleteAllOffers() {
        mOfferHandler.deleteAll();
    }

    public void deleteAllTasks() {
        mTaskHandler.deleteAll();
    }

    public Cursor getAllOffers() {
        return mOfferHandler.getAll();
    }

    public Cursor getRecentOffers() {
        return mOfferHandler.getRecent();
    }

    public Offer getOffer(long rowId) {
        return (Offer) mOfferHandler.getItem(rowId);
    }

    public String getOfferName(long offerId) {
        String offerName = null;
        Offer offer = (Offer) mOfferHandler.getItem(offerId);
        if (offer != null) {
            offerName = offer.getPosition();
        } else {
            offerName = mCtx.getString(R.string.none);
        }
        return offerName;
    }

    public RSSMessage getRssOffer(long rowId) {
        return (RSSMessage) mRSSOfferHandler.getItem(rowId);
    }

    public boolean updateOffer(Offer offer) {
        return mOfferHandler.update(offer);
    }

    public long createTask(Task task) {
        return mTaskHandler.create(task);
    }

    public Cursor getAllTasks() {
        return mTaskHandler.getAll();
    }

    public Cursor getRecentTasks() {
        return mTaskHandler.getRecent();
    }

    public boolean archiveTask(long rowId) {
        return mTaskHandler.archive(rowId);
    }

    public void archiveAllTasks() {
        mTaskHandler.archiveAll();
    }

    public List<Object> getAllOffersAsList() {
        return mOfferHandler.getAllAsList();
    }

    public List<Object> getRecentOffersAsList() {
        return mOfferHandler.getRecentAsList();
    }

    public Task getTask(long rowId) {
        return (Task) mTaskHandler.getItem(rowId);
    }

    public List<String> getRecentOffersPositions() {
        return mOfferHandler.getRecentPositions();
    }

    public List<String> getOffersPositions() {
        return mOfferHandler.getPositions();
    }

    public Cursor getRecentTasksForOffer(long mRowId) {
        return mTaskHandler.getRecentForOffer(mRowId);
    }

    public List<Long> getOffersIds() {
        return mOfferHandler.getIds();
    }

    public boolean updateTask(Task task) {
        return mTaskHandler.update(task);
    }

    public Cursor getAllRssOffers() {
        return mRSSOfferHandler.getAll();
    }

    public void addRssOffersFromList(List<RSSMessage> rssOffers) {

        if (rssOffers == null || rssOffers.isEmpty()) {
            return;
        }
        for (RSSMessage rssOffer : rssOffers) {
            mRSSOfferHandler.create(rssOffer);
        }
    }

    public void resetTaskNotification(long taskId) {
        mTaskHandler.resetNotification(taskId);
    }

    public Cursor getArchivedOffers() {
        return mOfferHandler.getArchived();
    }

    public void deleteOffer(long rowid) {
        mOfferHandler.delete(rowid);
    }

    public void deleteTask(long rowid) {
        mTaskHandler.delete(rowid);
    }

    public Cursor getArchivedTasks() {
        return mTaskHandler.getArchived();
    }

    public Cursor getArchivedTasksForOffer(long mRowId) {
        return mTaskHandler.getArchivedForOffer(mRowId);
    }

    public void deleteAllRecentOffers() {
        mOfferHandler.deleteAllRecent();
    }

    public void deleteAllArchivedOffers() {
        mOfferHandler.deleteAllArchived();
    }

    public void deleteAllArchivedTasks() {
        mTaskHandler.deleteAllArchived();
    }

    public void deleteAllRecentTasks() {
        mTaskHandler.deleteAllRecent();
    }

    public void unarchiveAllTasks() {
        mTaskHandler.unarchiveAll();
    }

    public void unarchiveAllOffers() {
        mOfferHandler.unarchiveAll();
    }

    public void unarchiveTask(Long param) {
        mTaskHandler.unArchive(param);
    }

    public void unarchiveOffer(long param) {
        mOfferHandler.unArchive(param);
    }

    public Task getTaskFromCursor(Cursor cursor) {
        return (Task) mTaskHandler.getItemFromCursor(cursor);
    }

    public Alarm getAlarmFromCursor(Cursor cursor) {
        return (Alarm) mAlarmHandler.getItemFromCursor(cursor);
    }

    public boolean isOfferExists(String position, String employer) {
        return mOfferHandler.isExists(position, employer);
    }

    public long addAlarm(Alarm alarm) {
        return mAlarmHandler.create(alarm);
    }

    public boolean updateAlarm(Alarm alarm) {
        return mAlarmHandler.update(alarm);
    }

    public void deleteAlarm(long id) {
        mAlarmHandler.delete(id);
    }

    public Alarm getAlarmByTaskId(long taskId) {
        return (Alarm) mAlarmHandler.getByTaskId(taskId);
    }

    public Cursor getAllAlarms() {
        return mAlarmHandler.getAll();
    }

    public List<Object> getAllAlarmsAsList() {
        return mAlarmHandler.getAllAsList();
    }
}
