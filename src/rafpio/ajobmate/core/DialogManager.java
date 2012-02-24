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
    public static final int POSITIVE_BUTTON = 1;
    public static final int NEGATIVE_BUTTON = 2;

    public static final int CONFIRM_ARCHIVE_ALL_OFFERS_DIALOG = 1;
    public static final int ADDING_EMPTY_OFFER_DIALOG = 2;
    public static final int CONFIRM_ARCHIVE_ALL_TASKS_DIALOG = 3;
    public static final int ADDING_EMPTY_TASK_DIALOG = 4;
    public static final int CONFIRM_DELETE_ALL_TASKS_DIALOG = 5;
    public static final int CONFIRM_DELETE_ALL_OFFERS_DIALOG = 6;
    public static final int CONFIRM_UNARCHIVE_ALL_OFFERS_DIALOG = 7;
    public static final int CONFIRM_UNARCHIVE_ALL_TASKS_DIALOG = 8;
    public static final int CONFIRM_DELETE_ALL_ARCHIVED_TASKS_DIALOG = 9;
    public static final int CONFIRM_DELETE_ALL_ARCHIVED_OFFERS_DIALOG = 10;
    public static final int CONFIRM_DELETE_ALL_RECENT_TASKS_DIALOG = 11;
    public static final int CONFIRM_DELETE_ALL_RECENT_OFFERS_DIALOG = 12;
    public static final int CONTACT_ADDED_DIALOG = 13;
    public static final int CONTACT_EXISTS_DIALOG = 14;
    public static final int OFFER_EXISTS_DIALOG = 15;

    public static DialogManager getInstance() {
        return INSTANCE;
    }

    private DialogManager() {
    }

    class DialogInfo {
        Context context;
        int titleId;
        int messageId;
        int positiveButtonLabelId;
        int negativeButtonLabelId;
        int flags;
        CommonDialogListener listener;
    }

    public DialogInfo getDialogInfoFromId(int dialogId, Context ctxt,
            final CommonDialogListener listener) {
        DialogInfo dialogInfo = new DialogInfo();

        dialogInfo.context = ctxt;
        dialogInfo.listener = listener;

        switch (dialogId) {
        case CONFIRM_ARCHIVE_ALL_OFFERS_DIALOG:
            dialogInfo.titleId = R.string.archive_all_offers_confirmation;
            dialogInfo.messageId = R.string.confirm_archive_all_offers;
            dialogInfo.positiveButtonLabelId = R.string.archive;
            dialogInfo.negativeButtonLabelId = R.string.cancel;
            dialogInfo.flags = POSITIVE_BUTTON | NEGATIVE_BUTTON;
            break;
        case CONFIRM_UNARCHIVE_ALL_OFFERS_DIALOG:
            dialogInfo.titleId = R.string.unarchive_all_offers_confirmation;
            dialogInfo.messageId = R.string.confirm_unarchive_all_offers;
            dialogInfo.positiveButtonLabelId = R.string.unarchive;
            dialogInfo.negativeButtonLabelId = R.string.cancel;
            dialogInfo.flags = POSITIVE_BUTTON | NEGATIVE_BUTTON;
            break;
        case CONFIRM_UNARCHIVE_ALL_TASKS_DIALOG:
            dialogInfo.titleId = R.string.unarchive_all_tasks_confirmation;
            dialogInfo.messageId = R.string.confirm_unarchive_all_tasks;
            dialogInfo.positiveButtonLabelId = R.string.unarchive;
            dialogInfo.negativeButtonLabelId = R.string.cancel;
            dialogInfo.flags = POSITIVE_BUTTON | NEGATIVE_BUTTON;
            break;
        case CONFIRM_DELETE_ALL_OFFERS_DIALOG:
            dialogInfo.titleId = R.string.delete_all_offers_confirmation;
            dialogInfo.messageId = R.string.confirm_delete_all_offers;
            dialogInfo.positiveButtonLabelId = R.string.delete;
            dialogInfo.negativeButtonLabelId = R.string.cancel;
            dialogInfo.flags = POSITIVE_BUTTON | NEGATIVE_BUTTON;
            break;
        case CONFIRM_DELETE_ALL_ARCHIVED_OFFERS_DIALOG:// FIXME: distinguish
                                                       // from previous one
            dialogInfo.titleId = R.string.delete_all_offers_confirmation;
            dialogInfo.messageId = R.string.confirm_delete_all_offers;
            dialogInfo.positiveButtonLabelId = R.string.delete;
            dialogInfo.negativeButtonLabelId = R.string.cancel;
            dialogInfo.flags = POSITIVE_BUTTON | NEGATIVE_BUTTON;
            break;
        case CONFIRM_ARCHIVE_ALL_TASKS_DIALOG:
            dialogInfo.titleId = R.string.archive_all_tasks_confirmation;
            dialogInfo.messageId = R.string.confirm_archive_all_tasks;
            dialogInfo.positiveButtonLabelId = R.string.archive;
            dialogInfo.negativeButtonLabelId = R.string.cancel;
            dialogInfo.flags = POSITIVE_BUTTON | NEGATIVE_BUTTON;
            break;
        case CONFIRM_DELETE_ALL_RECENT_TASKS_DIALOG:
            dialogInfo.titleId = R.string.delete_all_tasks_confirmation;
            dialogInfo.messageId = R.string.confirm_delete_all_tasks;
            dialogInfo.positiveButtonLabelId = R.string.delete;
            dialogInfo.negativeButtonLabelId = R.string.cancel;
            dialogInfo.flags = POSITIVE_BUTTON | NEGATIVE_BUTTON;
            break;
        case CONFIRM_DELETE_ALL_ARCHIVED_TASKS_DIALOG:
            dialogInfo.titleId = R.string.delete_all_archived_tasks_confirmation;
            dialogInfo.messageId = R.string.confirm_delete_all_archived_tasks;
            dialogInfo.positiveButtonLabelId = R.string.delete;
            dialogInfo.negativeButtonLabelId = R.string.cancel;
            dialogInfo.flags = POSITIVE_BUTTON | NEGATIVE_BUTTON;
            break;
        case ADDING_EMPTY_OFFER_DIALOG:
            dialogInfo.titleId = R.string.enter_offer_position;
            dialogInfo.messageId = R.string.enter_at_least_position_and_employer;
            dialogInfo.negativeButtonLabelId = R.string.ok;
            dialogInfo.flags = NEGATIVE_BUTTON;
            break;
        case ADDING_EMPTY_TASK_DIALOG:
            dialogInfo.titleId = R.string.enter_task_description;
            dialogInfo.messageId = R.string.enter_at_least_description_of_the_task;
            dialogInfo.negativeButtonLabelId = R.string.ok;
            dialogInfo.flags = NEGATIVE_BUTTON;
            break;
        case CONTACT_ADDED_DIALOG:
            dialogInfo.titleId = R.string.contact_added;
            dialogInfo.messageId = R.string.contact_added_msg;
            dialogInfo.negativeButtonLabelId = R.string.ok;
            dialogInfo.flags = NEGATIVE_BUTTON;
            break;
        case CONTACT_EXISTS_DIALOG:
            dialogInfo.titleId = R.string.contact_exists;
            dialogInfo.messageId = R.string.contact_exists_msg;
            dialogInfo.negativeButtonLabelId = R.string.ok;
            dialogInfo.flags = NEGATIVE_BUTTON;
        case OFFER_EXISTS_DIALOG:
            dialogInfo.titleId = R.string.offer_exists;
            dialogInfo.messageId = R.string.offer_exists_msg;
            dialogInfo.negativeButtonLabelId = R.string.ok;
            dialogInfo.flags = NEGATIVE_BUTTON;
            break;
        default:
            break;
        }

        return dialogInfo;
    }

    public Dialog getDialog(final Context context, int id,
            final CommonDialogListener listener) {

        final DialogInfo dialogInfo = getDialogInfoFromId(id, context, listener);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                dialogInfo.context);
        dialogBuilder.setTitle(dialogInfo.titleId)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(dialogInfo.messageId);

        if ((dialogInfo.flags & NEGATIVE_BUTTON) != 0) {
            dialogBuilder.setNegativeButton(dialogInfo.negativeButtonLabelId,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int whichButton) {
                            if (dialogInfo.listener != null) {
                                dialogInfo.listener.onNegativeResponse();
                            }
                        }
                    });
        }

        if ((dialogInfo.flags & POSITIVE_BUTTON) != 0) {
            dialogBuilder.setPositiveButton(dialogInfo.positiveButtonLabelId,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int whichButton) {
                            if (dialogInfo.listener != null) {
                                dialogInfo.listener.onPositiveResponse();
                            }
                        }
                    });
        }
        return dialogBuilder.create();
    }

}
