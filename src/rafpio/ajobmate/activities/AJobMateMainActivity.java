package rafpio.ajobmate.activities;

import rafpio.ajobmate.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AJobMateMainActivity extends Activity {

    private ListView mOptionsList;
    private String[] options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        options = getResources().getStringArray(R.array.options);
        mOptionsList = (ListView) findViewById(R.id.options_list);
        mOptionsList.setAdapter(mListAdapter);
        mOptionsList.setOnItemClickListener(mItemClickListener);
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {

            switch (arg2) {
            case 0:
                startActivity(new Intent(AJobMateMainActivity.this,
                        MyOffersListActivity.class));
                break;
            case 1:
                startActivity(new Intent(AJobMateMainActivity.this,
                        RSSOffersListActivity.class));
                break;
            case 2:
                startActivity(new Intent(AJobMateMainActivity.this,
                        TaskListActivity.class));
                break;
            case 3:
                startActivity(new Intent(AJobMateMainActivity.this,
                        HistoryActivity.class));
                break;
            default:
                break;
            }

        }
    };

    private BaseAdapter mListAdapter = new BaseAdapter() {

        public View getView(int arg0, View arg1, ViewGroup arg2) {

            View view;
            if (arg1 == null) {
                view = LayoutInflater.from(AJobMateMainActivity.this).inflate(
                        android.R.layout.simple_list_item_1, arg2, false);
            } else {
                view = arg1;
            }

            ((TextView) view).setText(options[arg0]);

            return view;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public Object getItem(int arg0) {
            return null;
        }

        public int getCount() {
            return options.length;
        }
    };

}
