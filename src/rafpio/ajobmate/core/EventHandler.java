package rafpio.ajobmate.core;

import java.util.List;
import java.util.Observable;

import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.model.Offer;
import rafpio.ajobmate.model.RSSMessage;
import android.os.AsyncTask;

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
    private static final int ADD_TO_CONTACTS_CMD = 19;
    private static final int ADD_OFFER_CMD = 18;

    public static final int CONTACT_ADDED = 0;
    public static final int CONTACT_EXISTS = 1;
    public static final int MULTIPLE_CONTACTS_EXIST = 2;
    public static final int OFFER_ADDED = 3;
    public static final int OFFER_NOT_ADDED = 4;
    public static final int OFFER_EXISTS = 5;

    private int command;
    private Object param;
    private OpResult outParam;

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
                    dbHelper.resetTaskNotification((Long) param);
                    break;
                case ADD_TO_CONTACTS_CMD:
                    addToContacts(param);
                    break;
                case ADD_OFFER_CMD:
                    Offer offer = (Offer) param;
                    boolean exists = dbHelper.isOfferExists(
                            offer.getPosition(), offer.getEmployer());
                    if (exists) {
                        outParam.status = OFFER_EXISTS;
                    } else {
                        long rowId = dbHelper.createOffer((Offer) param);
                        if (rowId == -1) {
                            outParam.status = OFFER_NOT_ADDED;
                        } else {
                            outParam.status = OFFER_ADDED;
                        }
                    }
                    break;
                default:
                    break;
                }
                return null;
            }

        }.execute();
    }

    public void resetTaskNotification(long taskId) {
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

    // FIXME: drop this
    public void addContact(Offer offer) {
        command = ADD_TO_CONTACTS_CMD;
        param = offer;
        runAsyncCommand();
    }

    public void addOffer(Offer offer) {
        command = ADD_OFFER_CMD;
        param = offer;
        runAsyncCommand();
    }

    private void addToContacts(Object param) {

        Offer offer = (Offer) param;
        int numOfContacts = ContactHandler
                .getNumOfContacts(offer.getEmployer());
        switch (numOfContacts) {
        case 1:
            outParam.status = CONTACT_EXISTS;
            return;
        case 2:
            outParam.status = MULTIPLE_CONTACTS_EXIST;
            return;
        default:
            break;
        }

        ContactHandler.addEmployerToContacts(offer);
        outParam.status = CONTACT_ADDED;
    }

    public class OpResult {
        public int status;
        public String description;
    }

}
