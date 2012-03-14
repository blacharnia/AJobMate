package rafpio.ajobmate.core;

import java.util.List;
import java.util.Observable;

import rafpio.ajobmate.db.DBTaskHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.model.Alarm;
import rafpio.ajobmate.model.Offer;
import rafpio.ajobmate.model.RSSMessage;
import rafpio.ajobmate.model.Task;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
    private static final int ADD_OFFER_CMD = 18;
    private static final int UPDATE_OFFER_CMD = 20;
    private static final int ADD_TASK_CMD = 22;
    private static final int UPDATE_TASK_CMD = 23;

    public static final int CONTACT_ADDED = 0;
    public static final int CONTACT_EXISTS = 1;
    public static final int MULTIPLE_CONTACTS_EXIST = 2;
    public static final int OFFER_ADDED = 3;
    public static final int OFFER_NOT_ADDED = 4;
    public static final int OFFER_EXISTS = 5;
    public static final int OFFER_UPDATED = 6;
    public static final int OFFER_NOT_UPDATED = 7;
    public static final int NO_NETWORK = 8;
    public static final int RSS_OFFERS_ADDED = 9;
    public static final int NO_RSS_OFFERS_ADDED = 10;
    public static final int TASK_ADDED = 11;
    public static final int TASK_NOT_ADDED = 12;
    public static final int TASK_UPDATED = 13;
    public static final int TASK_NOT_UPDATED = 14;

    private int command;
    private Object param;
    private OpResult outParam;
    private Context appContext;

    public static EventHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        appContext = context;
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

    public void runAsyncCommand() {
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
                        outParam.status = RSS_OFFERS_ADDED;
                    } else {
                        outParam.status = NO_RSS_OFFERS_ADDED;
                    }
                    break;
                case RESET_TASK_NOTIFICATION_CMD:
                    dbHelper.resetTaskNotification((Long) param);
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
                case UPDATE_OFFER_CMD:
                    boolean ret = dbHelper.updateOffer((Offer) param);
                    if (ret) {
                        outParam.status = OFFER_UPDATED;
                    } else {
                        outParam.status = OFFER_NOT_UPDATED;
                    }

                    break;
                case ADD_TASK_CMD:
                    Task task = (Task) param;
                    long taskId = dbHelper.createTask(task);
                    task.setId(taskId);
                    if (task.isAlarmTimeSet()) {
                        Log.d("RP", "ADD_TASK_CMD:alarm set");
                        Alarm alarm = new Alarm(0, taskId,
                                task.getNotificationTime());
                        dbHelper.addAlarm(alarm);
                        setAlarm(alarm);
                    }
                    if (taskId == -1) {
                        outParam.status = TASK_NOT_ADDED;
                    } else {
                        outParam.status = TASK_ADDED;
                    }
                    break;
                case UPDATE_TASK_CMD:
                    Task newTask = (Task) param;
                    Task oldTask = dbHelper.getTask(newTask.getId());

                    // handle alarm
                    boolean wasAlarmSet = oldTask.isAlarmTimeSet();
                    boolean isAlarmSet = newTask.isAlarmTimeSet();

                    if (isAlarmSet) {
                        Alarm alarm;
                        if (wasAlarmSet) {
                            alarm = dbHelper.getAlarmByTaskId(newTask.getId());
                            alarm.setAlarmTime(newTask.getNotificationTime());
                            dbHelper.updateAlarm(alarm);
                        } else {
                            alarm = new Alarm(0, newTask.getId(),
                                    newTask.getNotificationTime());
                            dbHelper.updateAlarm(alarm);
                        }
                        setAlarm(alarm);
                    } else {
                        if (wasAlarmSet) {
                            Alarm alarm = dbHelper.getAlarmByTaskId(newTask
                                    .getId());
                            dbHelper.deleteAlarm(alarm.getId());
                            cancelAlarm(alarm);
                        }
                    }

                    boolean ret1 = dbHelper.updateTask(newTask);

                    if (ret1) {
                        outParam.status = TASK_UPDATED;
                    } else {
                        outParam.status = TASK_NOT_UPDATED;
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

    public void addOffer(Offer offer) {
        command = ADD_OFFER_CMD;
        param = offer;
        runAsyncCommand();
    }

    public void updateOffer(Offer offer) {
        command = UPDATE_OFFER_CMD;
        param = offer;
        runAsyncCommand();
    }

    private void setAlarm(Alarm alarm) {

        PendingIntent pendingIntent = getAlarmPendingIntent(alarm);
        AlarmManager alarmManager = (AlarmManager) appContext
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getAlarmTime(),
                pendingIntent);

    }

    private PendingIntent getAlarmPendingIntent(Alarm alarm) {
        Intent intent = new Intent(appContext, TimeAlarmBroadcastReceiver.class);
        intent.putExtra(DBTaskHandler.KEY_ROWID, alarm.getTaskId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(appContext,
                (int) alarm.getTaskId(), intent, PendingIntent.FLAG_ONE_SHOT
                        | PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private void cancelAlarm(Alarm alarm) {
        PendingIntent pendingIntent = getAlarmPendingIntent(alarm);
        AlarmManager alarmManager = (AlarmManager) appContext
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public class OpResult {
        public int status;
        public String description;
    }

    public void addTask(Task task) {
        command = ADD_TASK_CMD;
        param = task;
        runAsyncCommand();

    }

    public void updateTask(Task task) {
        command = UPDATE_TASK_CMD;
        param = task;
        runAsyncCommand();
    }

    /*
     * public void setAlarms() { long now = System.currentTimeMillis(); Context
     * context = JobmateApplication.getInstance() .getApplicationContext();
     * Intent intent = new Intent(appContext, TimeAlarmBroadcastReceiver.class);
     * 
     * PendingIntent pendingIntent = PendingIntent.getBroadcast(appContext, 0,
     * intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);
     * 
     * PendingIntent pendingIntent2 = PendingIntent.getBroadcast(appContext, 1,
     * intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);
     * 
     * PendingIntent pendingIntent3 = PendingIntent.getBroadcast(appContext, 2,
     * intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);
     * 
     * AlarmManager alarmManager = (AlarmManager) context
     * .getSystemService(Context.ALARM_SERVICE);
     * alarmManager.cancel(pendingIntent);
     * 
     * alarmManager.set(AlarmManager.RTC_WAKEUP, now + 20 * 1000,
     * pendingIntent);
     * 
     * alarmManager.set(AlarmManager.RTC_WAKEUP, now + 40 * 1000,
     * pendingIntent2);
     * 
     * alarmManager.set(AlarmManager.RTC_WAKEUP, now + 60 * 1000,
     * pendingIntent3); }
     */
}
