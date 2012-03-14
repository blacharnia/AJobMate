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
    public static final String DATE_FORMAT = "dd-MMM-yyyy hh:mm";

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

    public static String getPackageName() {
        return "rafpio.ajobmate";
    }
}
