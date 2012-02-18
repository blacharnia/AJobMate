package rafpio.ajobmate.core;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;

public class Common {

	private static final String PLAIN_TEXT = "plain/text";
	public static final int LOADING_DIALOG = 0;
	public static final String PREF_FILE_NAME = "_prefs";
	public static final String PREF_VERSION = "version";
	public static final int ACTIVITY_EDIT = 1;
	public static final String DATE_FORMAT = "yy-MM-dd:hh-mm";
	public static final int CONFIRM_ARCHIVE_ALL_OFFERS_DIALOG = 1;
	public static final int ADDING_EMPTY_OFFER_DIALOG = 2;
	public static final int CONFIRM_ARCHIVE_ALL_TASKS_DIALOG = 3;
	public static final int ADDING_EMPTY_TASK_DIALOG = 4;

	public static final int CONFIRM_DELETE_ALL_TASKS_DIALOG = 5;
	public static final int CONFIRM_DELETE_ALL_OFFERS_DIALOG = 6;
	public static final int CONFIRM_DELETE_ALL_RECENT_TASKS_DIALOG = 11;
	public static final int CONFIRM_DELETE_ALL_RECENT_OFFERS_DIALOG = 12;
	public static final int CONFIRM_DELETE_ALL_ARCHIVED_TASKS_DIALOG = 9;
	public static final int CONFIRM_DELETE_ALL_ARCHIVED_OFFERS_DIALOG = 10;
	public static final int CONFIRM_UNARCHIVE_ALL_OFFERS_DIALOG = 7;
	public static final int CONFIRM_UNARCHIVE_ALL_TASKS_DIALOG = 8;

	/*
	 * public static final int ARCHIVE_OFFER_CMD = 0; public static final int
	 * ARCHIVE_TASK_CMD = 1; public static final int ARCHIVE_ALL_TASKS_CMD = 2;
	 * public static final int ARCHIVE_ALL_OFFERS_CMD = 3; public static final
	 * int REQUEST_RSS_OFFERS_CMD = 4; public static final int
	 * RESET_TASK_NOTIFICATION_CMD = 5; public static final int
	 * DELETE_ALL_OFFERS_CMD = 6; public static final int DELETE_ALL_TASKS_CMD =
	 * 7; public static final int DELETE_OFFER_CMD = 8; public static final int
	 * DELETE_TASK_CMD = 9; public static final int DELETE_ALL_RECENT_OFFERS_CMD
	 * = 10; public static final int DELETE_ALL_ARCHIVED_OFFERS_CMD = 11; public
	 * static final int DELETE_ALL_RECENT_TASKS_CMD = 12; public static final
	 * int DELETE_ALL_ARCHIVED_TASKS_CMD = 13; public static final int
	 * UNARCHIVE_ALL_OFFERS_CMD = 14; public static final int
	 * UNARCHIVE_ALL_TASKS_CMD = 15; public static final int UNARCHIVE_OFFER_CMD
	 * = 16; public static final int UNARCHIVE_TASK_CMD = 17;
	 */
	private static final boolean SUPPORTS_GINGERBREAD = true;

	public static String getPrefTextValue(Context context, String prefFileName,
			String valueName) {
		return context.getSharedPreferences(prefFileName, 0).getString(
				valueName, "");
	}

	public static boolean setPrefTextValue(Context context,
			String prefFileName, String valueName, String valueContent) {
		return context.getSharedPreferences(prefFileName, 0).edit()
				.putString(valueName, valueContent).commit();
	}

	public static String getTimeAsString(long timeInMilis) {
		return (String) DateFormat.format(DATE_FORMAT, timeInMilis);
	}

	public static void sendEmail(String email, Context context) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		String aEmailList[] = { email };
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
		emailIntent.setType(PLAIN_TEXT);
		context.startActivity(emailIntent);
	}

	public static ILastLocationFinder getLastLocationFinder(Context context) {
		return SUPPORTS_GINGERBREAD ? new GingerbreadLastLocationFinder(context)
				: new LegacyLastLocationFinder(context);
	}
	
	public static String getPackageName(){
		return "rafpio.ajobmate";
	}
}
