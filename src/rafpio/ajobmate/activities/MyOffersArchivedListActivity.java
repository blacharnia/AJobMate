package rafpio.ajobmate.activities;

import java.util.Observable;
import java.util.Observer;

import rafpio.ajobmate.R;
import rafpio.ajobmate.adapters.OfferCursorAdapter;
import rafpio.ajobmate.core.Common;
import rafpio.ajobmate.core.DialogManager;
import rafpio.ajobmate.core.DialogManager.CommonDialogListener;
import rafpio.ajobmate.core.EventHandler;
import rafpio.ajobmate.db.JOffersDbAdapter;
import rafpio.ajobmate.db.TableHandler;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MyOffersArchivedListActivity extends Activity implements Observer {

	private ListView mOffersList;
	private OfferCursorAdapter mListAdapter;
	private Button deleteAllButton;
	private Button unarchiveAllButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_offers_archived);

		EventHandler.getInstance().addObserver(this);

		mOffersList = (ListView) findViewById(R.id.offers);
		mOffersList.setEmptyView(findViewById(R.id.empty));

		deleteAllButton = (Button) findViewById(R.id.deleteAll);
		deleteAllButton.setOnClickListener(deleteAllClickListener);
		
		unarchiveAllButton = (Button) findViewById(R.id.unarchiveAll);
		unarchiveAllButton.setOnClickListener(unarchiveAllClickListener);

		registerForContextMenu(mOffersList);

		mOffersList.setOnItemClickListener(mItemListener);
	}

	@Override
	protected void onStart() {
		super.onStart();
		showDialog(Common.LOADING_DIALOG);
		loadOffers();
		dismissDialog(Common.LOADING_DIALOG);
	}


	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case Common.LOADING_DIALOG:
			ProgressDialog loadingDialog = new ProgressDialog(this);
			loadingDialog.setMessage("Loading...");
			loadingDialog.setIndeterminate(true);
			return loadingDialog;
		case Common.CONFIRM_UNARCHIVE_ALL_OFFERS_DIALOG:
			return DialogManager.getDialog(this, id,
					new OnConfirmUnarchiveDialogListener());
		case Common.CONFIRM_DELETE_ALL_ARCHIVED_OFFERS_DIALOG:
			return DialogManager.getDialog(this, id,
					new OnConfirmDeleteDialogListener());
		default:
			break;
		}
		return super.onCreateDialog(id);
	}

	private void loadOffers() {
		final JOffersDbAdapter dbHelper = JOffersDbAdapter.getInstance();
		dbHelper.open();
		Cursor cursor = dbHelper.getArchivedOffers();

		if (cursor != null) {
			if (mListAdapter == null) {
				mListAdapter = new OfferCursorAdapter(getApplicationContext(),
						cursor);
				mOffersList.setAdapter(mListAdapter);
			} else {
				mListAdapter.changeCursor(cursor);
			}
			int cnt = cursor.getCount();
			deleteAllButton.setEnabled(cnt > 0);
			unarchiveAllButton.setEnabled(cnt > 0);
		}
		dbHelper.close();
	}

	private static class OnConfirmUnarchiveDialogListener implements
			CommonDialogListener {

		public void onPositiveResponse() {
			EventHandler.getInstance().unarchiveAllOffers();
		}

		public void onNegativeResponse() {}

	}

	private static class OnConfirmDeleteDialogListener implements
			CommonDialogListener {

		public void onPositiveResponse() {
			EventHandler.getInstance().deleteAllArchivedOffers();
		}

		public void onNegativeResponse() {}

	}

	private OnClickListener deleteAllClickListener = new OnClickListener() {

		public void onClick(View v) {
			if (mListAdapter.getCount() > 0) {
				showDialog(Common.CONFIRM_DELETE_ALL_ARCHIVED_OFFERS_DIALOG);
			}
		}
	};
	
	private OnClickListener unarchiveAllClickListener = new OnClickListener() {

		public void onClick(View v) {
			if (mListAdapter.getCount() > 0) {
				showDialog(Common.CONFIRM_UNARCHIVE_ALL_OFFERS_DIALOG);
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventHandler.getInstance().deleteObserver(this);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(Menu.NONE, 0, 0, "Delete offer");

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();

		if (item.getItemId() == 0) {
			EventHandler.getInstance().deleteOffer(info.id);
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO: check this
		if (resultCode == RESULT_OK) {
			loadOffers();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private OnItemClickListener mItemListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(MyOffersArchivedListActivity.this,
					MyOfferDetailActivity.class);
			intent.putExtra(TableHandler.KEY_ROWID, arg3);
			startActivityForResult(intent, Common.ACTIVITY_EDIT);
		}
	};

	public void update(Observable observable, Object data) {
		loadOffers();
	}

}
