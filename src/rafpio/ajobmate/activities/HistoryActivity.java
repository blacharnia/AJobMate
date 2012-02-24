package rafpio.ajobmate.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class HistoryActivity extends TabActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TabHost tabHost = getTabHost();

        tabHost.addTab(tabHost
                .newTabSpec("Offers")
                .setIndicator("Offers")
                .setContent(
                        new Intent(this, MyOffersArchivedListActivity.class)));

        tabHost.addTab(tabHost.newTabSpec("Tasks").setIndicator("Tasks")
                .setContent(new Intent(this, TaskArchivedListActivity.class)));

    }
}
