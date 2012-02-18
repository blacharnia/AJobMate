package rafpio.ajobmate.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.model.Offer;
import rafpio.ajobmate.model.RSSMessage;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class EventHandler extends Observable {

    private final static EventHandler INSTANCE = new EventHandler();

    public static final int ARCHIVE_OFFER_CMD = 0;
    public static final int ARCHIVE_TASK_CMD = 1;
    public static final int ARCHIVE_ALL_TASKS_CMD = 2;
    public static final int ARCHIVE_ALL_OFFERS_CMD = 3;
    public static final int REQUEST_RSS_OFFERS_CMD = 4;
    public static final int RESET_TASK_NOTIFICATION_CMD = 5;
    public static final int DELETE_ALL_OFFERS_CMD = 6;
    public static final int DELETE_ALL_TASKS_CMD = 7;
    public static final int DELETE_OFFER_CMD = 8;
    public static final int DELETE_TASK_CMD = 9;
    public static final int DELETE_ALL_RECENT_OFFERS_CMD = 10;
    public static final int DELETE_ALL_ARCHIVED_OFFERS_CMD = 11;
    public static final int DELETE_ALL_RECENT_TASKS_CMD = 12;
    public static final int DELETE_ALL_ARCHIVED_TASKS_CMD = 13;	
	public static final int UNARCHIVE_ALL_OFFERS_CMD = 14;
	public static final int UNARCHIVE_ALL_TASKS_CMD = 15;
	public static final int UNARCHIVE_OFFER_CMD = 16;
	public static final int UNARCHIVE_TASK_CMD = 17;
	private static final int ADD_TO_CONTACTS_CMD = 18;
	
	public static final int CONTACT_ADDED = 0;
	public static final int CONTACT_EXISTS = 1;
	public static final int MULTIPLE_CONTACTS_EXIST = 2;
	/*
    public static final int DATA_UP_TO_DATE = 0;
    public static final int UPDATE_NEEDED = 1;
    public final static int UPDATE_REQUEST_OK = 2;
    public final static int UPDATE_REQUEST_FAILED = 3;
    public final static int CLIENT_SERVER_UPDATE_INIT_OK = 4;
    public final static int CLIENT_SERVER_UPDATE_INIT_FAILED = 5;
    public final static int CLIENT_SERVER_UPDATE_OK = 6;
    public final static int CLIENT_SERVER_UPDATE_FAILED = 7;
    public static final int RSS_DATA_OK = 8;
    public static final int RSS_DATA_FAILED = 9;
*/
	

    private int command;
    private Object param;
    private OpResult outParam;
    
	private Context mContext;

    public static EventHandler getInstance() {
        return INSTANCE;
    }

    private EventHandler() {
        outParam = new OpResult();
    }

    public void requestRssOffers() {
        command = REQUEST_RSS_OFFERS_CMD;
        runAsyncCommand();
    }

    public void archiveOffer(final long id) {
        command = ARCHIVE_OFFER_CMD;
        param = id;
        runAsyncCommand();
    }

    public void archiveAllOffers() {
        command = ARCHIVE_ALL_OFFERS_CMD;
        runAsyncCommand();
    }

    public void archiveTask(final long id) {
        command = ARCHIVE_TASK_CMD;
        param = id;
        runAsyncCommand();
    }

    public void archiveAllTasks() {
        command = ARCHIVE_ALL_TASKS_CMD;
        runAsyncCommand();
    }

    private void runAsyncCommand() {
        new AsyncTask<Void, Integer, Void>() {

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                setChanged();
                notifyObservers(outParam);
            }

            @Override
            protected Void doInBackground(Void... params) {
                JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
                switch (command) {
                case ARCHIVE_OFFER_CMD:
                    dbHelper.archiveOffer((Long) param);
                    break;
                case DELETE_OFFER_CMD:
                    dbHelper.deleteOffer((Long) param);
                    break;
                case DELETE_TASK_CMD:
                    dbHelper.deleteTask((Long) param);
                    break;
                case ARCHIVE_ALL_OFFERS_CMD:
                    dbHelper.archiveAllOffers();
                    break;
                case DELETE_ALL_OFFERS_CMD:
                    dbHelper.deleteAllOffers();
                    break;
                case DELETE_ALL_RECENT_OFFERS_CMD:
                    dbHelper.deleteAllRecentOffers();
                case DELETE_ALL_ARCHIVED_OFFERS_CMD:
                    dbHelper.deleteAllArchivedOffers();
                    break;
                case ARCHIVE_TASK_CMD:
                    dbHelper.archiveTask((Long) param);
                    break;
                case UNARCHIVE_TASK_CMD:
                    dbHelper.unarchiveTask((Long) param);
                    break;
                case UNARCHIVE_OFFER_CMD:
                    dbHelper.unarchiveOffer((Long) param);
                    break;
                case ARCHIVE_ALL_TASKS_CMD:
                    dbHelper.archiveAllTasks();
                    break;
                case UNARCHIVE_ALL_TASKS_CMD:
                    dbHelper.unarchiveAllTasks();
                    break;
                case UNARCHIVE_ALL_OFFERS_CMD:
                    dbHelper.unarchiveAllOffers();
                    break;
                case DELETE_ALL_RECENT_TASKS_CMD:
                    dbHelper.deleteAllRecentTasks();
                    break;
                case DELETE_ALL_ARCHIVED_TASKS_CMD:
                    dbHelper.deleteAllArchivedTasks();
                    break;
                case REQUEST_RSS_OFFERS_CMD:
                    dbHelper.deleteAllRssOffers();
                    List<RSSMessage> rssOffers = RequestHandler
                            .requestRssOffers();
                    if (rssOffers != null && !rssOffers.isEmpty()) {
                        dbHelper.addRssOffersFromList(rssOffers);
                    }
                    break;
                case RESET_TASK_NOTIFICATION_CMD:
                    dbHelper.resetTaskNotification((Long)param);
                    break;
                case ADD_TO_CONTACTS_CMD:                    
                    addToContacts(param);
                    break;
                 default:
                        break;
                }
                return null;
            }

        }.execute();
    }

    public void resetTaskNotification(long taskId){
        command = RESET_TASK_NOTIFICATION_CMD;
        param = taskId;
        runAsyncCommand();
    }
    

    public void deleteAllOffers() {
        command = DELETE_ALL_OFFERS_CMD;
        runAsyncCommand();
    }
    public void deleteAllTasks() {
        command = DELETE_ALL_TASKS_CMD;
        runAsyncCommand();
    }

    public void deleteOffer(long id) {
        command = DELETE_OFFER_CMD;
        param = id;
        runAsyncCommand();
    }
    
    public void deleteTask(long id) {
        command = DELETE_TASK_CMD;
        param = id;
        runAsyncCommand();
    }

	public void deleteAllRecentOffers() {
		command = DELETE_ALL_RECENT_OFFERS_CMD;
        runAsyncCommand();
	}
	
	public void deleteAllArchivedOffers() {
		command = DELETE_ALL_ARCHIVED_OFFERS_CMD;
        runAsyncCommand();
	}
	
	public void deleteAllRecentTasks() {
		command = DELETE_ALL_RECENT_TASKS_CMD;
        runAsyncCommand();
	}
	
	public void deleteAllArchivedTasks() {
		command = DELETE_ALL_ARCHIVED_TASKS_CMD;
        runAsyncCommand();
	}

	public void unarchiveAllOffers() {
		command = UNARCHIVE_ALL_OFFERS_CMD;
        runAsyncCommand();
	}
	
	public void unarchiveAllTasks() {
		command = UNARCHIVE_ALL_TASKS_CMD;
        runAsyncCommand();
	}

	public void unarchiveOffer(long id) {
		command = UNARCHIVE_OFFER_CMD;
		param = id;
        runAsyncCommand();
	}
	
	public void unarchiveTask(long id) {
		command = UNARCHIVE_TASK_CMD;
		param = id;
        runAsyncCommand();
	}
	
	public void addContact(Offer offer, Context context) {
		mContext = context;
		command = ADD_TO_CONTACTS_CMD;
		param = offer;
        runAsyncCommand();
	}
	
	public int getNumOfContacts(String name, Context context){
	    
	    String where = ContactsContract.Data.DISPLAY_NAME
	            + " = ? "
	            ;
	    Cursor cur = context.getContentResolver().
	            query(ContactsContract.Contacts.CONTENT_URI,
                null, 
                where, 
                new String[]{name},
                null);
	    
	    
	    int ret =  0;
	    if(cur != null){ 
	        ret = cur.getCount();
	        cur.close();
        }
        
	    return ret;
	}
	
	private void addToContacts(Object param) {
	    Offer offer = (Offer) param;
	    int numOfContacts = getNumOfContacts(offer.getEmployer(), mContext);
	    switch(numOfContacts){
	    case 1:
	        outParam.status = CONTACT_EXISTS;
	        return;
	    case 2:
	        outParam.status = MULTIPLE_CONTACTS_EXIST;
	        return;
	    default:
	        break;
	    }
	    
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		int rawContactInsertIndex = ops.size();

		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null).build());

		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				.withValue(
						ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
						offer.getEmployer()).build());

		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
						offer.getPhoneNr())
				.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
						ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
				.build());
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Email.DATA,
						offer.getEmail())
				.withValue(ContactsContract.CommonDataKinds.Email.TYPE,
						ContactsContract.CommonDataKinds.Email.TYPE_WORK)
				.build());

		try {
			ContentProviderResult res[] = mContext.getContentResolver().applyBatch(
					ContactsContract.AUTHORITY, ops);
			Log.d("RP", "" + res[0]);
		} catch (RemoteException e) {
			Log.d("RP", "" + e);
		} catch (OperationApplicationException e) {
			Log.d("RP", "" + e);
		}
		
		outParam.status = CONTACT_ADDED;		
	}
	
	public class OpResult {
	    public int status;
	    public String description;
	}

}
