package rafpio.ajobmate.core;

import java.util.List;
import java.util.Observable;

import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.model.RSSMessage;
import android.os.AsyncTask;

public class EventHandler extends Observable {

    private final static EventHandler INSTANCE = new EventHandler();

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

    private int command;
    private Object param;

    public static EventHandler getInstance() {
        return INSTANCE;
    }

    private EventHandler() {
    }

    public void requestRssOffers() {
        command = Common.REQUEST_RSS_OFFERS_CMD;
        runAsyncCommand();
    }

    public void archiveOffer(final long id) {
        command = Common.ARCHIVE_OFFER_CMD;
        param = id;
        runAsyncCommand();
    }

    public void archiveAllOffers() {
        command = Common.ARCHIVE_ALL_OFFERS_CMD;
        runAsyncCommand();
    }

    public void archiveTask(final long id) {
        command = Common.ARCHIVE_TASK_CMD;
        param = id;
        runAsyncCommand();
    }

    public void archiveAllTasks() {
        command = Common.ARCHIVE_ALL_TASKS_CMD;
        runAsyncCommand();
    }

    private void runAsyncCommand() {
        new AsyncTask<Void, Integer, Void>() {

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                setChanged();
                notifyObservers();
            }

            @Override
            protected Void doInBackground(Void... params) {
                JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
                dbHelper.open();
                switch (command) {
                case Common.ARCHIVE_OFFER_CMD:
                    dbHelper.archiveOffer((Long) param);
                    break;
                case Common.DELETE_OFFER_CMD:
                    dbHelper.deleteOffer((Long) param);
                    break;
                case Common.ARCHIVE_ALL_OFFERS_CMD:
                    dbHelper.archiveAllOffers();
                    break;
                case Common.DELETE_ALL_OFFERS_CMD:
                    dbHelper.deleteAllOffers();
                    break;
                case Common.DELETE_ALL_RECENT_OFFERS_CMD:
                    dbHelper.deleteAllRecentOffers();
                case Common.DELETE_ALL_ARCHIVED_OFFERS_CMD:
                    dbHelper.deleteAllArchivedOffers();
                    break;
                case Common.ARCHIVE_TASK_CMD:
                    dbHelper.archiveTask((Long) param);
                    break;
                case Common.ARCHIVE_ALL_TASKS_CMD:
                    dbHelper.archiveAllTasks();
                    break;
                case Common.UNARCHIVE_ALL_TASKS_CMD:
                    dbHelper.unarchiveAllTasks();
                    break;
                case Common.UNARCHIVE_ALL_OFFERS_CMD:
                    dbHelper.unarchiveAllOffers();
                    break;
                case Common.DELETE_ALL_TASKS_CMD:
                    dbHelper.deleteAllTasks();
                    break;
                case Common.REQUEST_RSS_OFFERS_CMD:
                    dbHelper.deleteAllRssOffers();
                    List<RSSMessage> rssOffers = RequestHandler
                            .requestRssOffers();
                    if (rssOffers != null && !rssOffers.isEmpty()) {
                        dbHelper.addRssOffersFromList(rssOffers);
                    }
                    break;
                case Common.RESET_TASK_NOTIFICATION_CMD:
                    dbHelper.resetTaskNotification((Long)param);
                    break;
                 default:
                        break;
                }
                dbHelper.close();
                return null;
            }

        }.execute();
    }

    public void resetTaskNotification(long taskId){
        command = Common.RESET_TASK_NOTIFICATION_CMD;
        param = taskId;
        runAsyncCommand();
    }
    

    public void deleteAllOffers() {
        command = Common.DELETE_ALL_OFFERS_CMD;
        runAsyncCommand();
    }
    public void deleteAllTasks() {
        command = Common.DELETE_ALL_TASKS_CMD;
        runAsyncCommand();
    }

    public void deleteOffer(long id) {
        command = Common.DELETE_OFFER_CMD;
        param = id;
        runAsyncCommand();
    }
    
    public void deleteTask(long id) {
        command = Common.DELETE_TASK_CMD;
        param = id;
        runAsyncCommand();
    }

	public void deleteAllRecentOffers() {
		command = Common.DELETE_ALL_RECENT_OFFERS_CMD;
        runAsyncCommand();
	}
	
	public void deleteAllArchivedOffers() {
		command = Common.DELETE_ALL_ARCHIVED_OFFERS_CMD;
        runAsyncCommand();
	}
	
	public void deleteAllRecentTasks() {
		command = Common.DELETE_ALL_RECENT_TASKS_CMD;
        runAsyncCommand();
	}
	
	public void deleteAllArchivedTasks() {
		command = Common.DELETE_ALL_ARCHIVED_TASKS_CMD;
        runAsyncCommand();
	}

	public void unarchiveAllOffers() {
		command = Common.UNARCHIVE_ALL_OFFERS_CMD;
        runAsyncCommand();
	}
	
	public void unarchiveAllTasks() {
		command = Common.UNARCHIVE_ALL_TASKS_CMD;
        runAsyncCommand();
	}

}
