package rafpio.ajobmate.adapters;

import rafpio.ajobmate.R;
import rafpio.ajobmate.core.Common;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainOptionsGridAdapter extends BaseAdapter {

    private Context context;
    private String[] options;

    public MainOptionsGridAdapter(Context context) {
	this.context = context;
	options = context.getResources().getStringArray(R.array.options);
    }

    @Override
    public int getCount() {
	return 6;
    }

    @Override
    public Object getItem(int position) {
	return null;
    }

    @Override
    public long getItemId(int position) {
	return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View view;
	if (convertView == null) {
	    view = LayoutInflater.from(context).inflate(
		    R.layout.main_option_row, parent, false);
	} else {
	    view = convertView;
	}
	((TextView) view.findViewById(R.id.label)).setText(options[position]);

	ImageView icon = (ImageView) view.findViewById(R.id.icon);
	switch (position) {
	case 0:
	    // icon.setBackgroundResource(R.drawable.info_icon);
	    break;
	case 1:
	    // icon.setBackgroundResource(R.drawable.agenda_icon);
	    break;
	case 2:
	    // icon.setBackgroundResource(R.drawable.teacher_icon);
	    break;
	case 3:
	    // icon.setBackgroundResource(R.drawable.fav_icon);
	    break;
	case 4:
	    // icon.setBackgroundResource(R.drawable.wallet_icon);
	    break;
	case 5:
	    // icon.setBackgroundResource(R.drawable.exclamation_icon);
	    break;
	default:
	    break;
	}

	return view;
    }
}
