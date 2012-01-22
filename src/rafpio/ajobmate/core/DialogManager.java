package rafpio.ajobmate.core;

import rafpio.ajobmate.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogManager {

    public interface CommonDialogListener {
        void onPositiveResponse();

        void onNegativeResponse();
    }

    private final static DialogManager INSTANCE = new DialogManager();

    public static DialogManager getInstance() {
        return INSTANCE;
    }

    private DialogManager() {
    }

    public static Dialog getDialog(final Context context, int id,
            final CommonDialogListener listener) {

        Dialog dlg;
        switch (id) {
        case Common.CONFIRM_ARCHIVE_ALL_OFFERS_DIALOG:
            dlg = new AlertDialog.Builder(context)
                    .setTitle(R.string.archive_all_offers_confirmation)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.confirm_archive_all_offers)
                    .setPositiveButton(R.string.archive,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    listener.onPositiveResponse();
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    listener.onNegativeResponse();
                                }
                            }).create();
            return dlg;
            
        case Common.CONFIRM_UNARCHIVE_ALL_OFFERS_DIALOG:
            dlg = new AlertDialog.Builder(context)
                    .setTitle(R.string.unarchive_all_offers_confirmation)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.confirm_unarchive_all_offers)
                    .setPositiveButton(R.string.unarchive,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    listener.onPositiveResponse();
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    listener.onNegativeResponse();
                                }
                            }).create();
            return dlg;
        case Common.CONFIRM_DELETE_ALL_OFFERS_DIALOG:
            dlg = new AlertDialog.Builder(context)
                    .setTitle(R.string.delete_all_offers_confirmation)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.confirm_delete_all_offers)
                    .setPositiveButton(R.string.delete,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    listener.onPositiveResponse();
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    listener.onNegativeResponse();
                                }
                            }).create();
            return dlg;
            
        case Common.CONFIRM_DELETE_ALL_ARCHIVED_OFFERS_DIALOG:
            dlg = new AlertDialog.Builder(context)
                    .setTitle(R.string.delete_all_offers_confirmation)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.confirm_delete_all_offers)
                    .setPositiveButton(R.string.delete,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    listener.onPositiveResponse();
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    listener.onNegativeResponse();
                                }
                            }).create();
            return dlg;
        case Common.CONFIRM_ARCHIVE_ALL_TASKS_DIALOG:
            dlg = new AlertDialog.Builder(context)
                    .setTitle(R.string.archive_all_tasks_confirmation)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.confirm_archive_all_tasks)
                    .setPositiveButton(R.string.archive,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    listener.onPositiveResponse();
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    listener.onNegativeResponse();
                                }
                            }).create();
            return dlg;
        case Common.CONFIRM_DELETE_ALL_TASKS_DIALOG:
            dlg = new AlertDialog.Builder(context)
                    .setTitle(R.string.delete_all_tasks_confirmation)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.confirm_delete_all_tasks)
                    .setPositiveButton(R.string.delete,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    listener.onPositiveResponse();
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    listener.onNegativeResponse();
                                }
                            }).create();
            return dlg;
        case Common.ADDING_EMPTY_OFFER_DIALOG:
            dlg = new AlertDialog.Builder(context)
                    .setTitle(R.string.enter_offer_position)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.enter_at_least_position_of_the_offer)
                    .setNegativeButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    if (listener != null) {
                                        listener.onNegativeResponse();
                                    }
                                }
                            }).create();
            return dlg;
        case Common.ADDING_EMPTY_TASK_DIALOG:
            dlg = new AlertDialog.Builder(context)
                    .setTitle(R.string.enter_task_description)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.enter_at_least_description_of_the_task)
                    .setNegativeButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                    if (listener != null) {
                                        listener.onNegativeResponse();
                                    }
                                }
                            }).create();
            return dlg;

        }
        return null;
    }
}
