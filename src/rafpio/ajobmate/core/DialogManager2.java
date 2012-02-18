package rafpio.ajobmate.core;

import rafpio.ajobmate.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogManager2 {

    public interface CommonDialogListener {
        void onPositiveResponse();

        void onNegativeResponse();
    }

    private final static DialogManager2 INSTANCE = new DialogManager2();
    public static final int POSITIVE_BUTTON = 1;
    public static final int NEGATIVE_BUTTON = 2;

    public static DialogManager2 getInstance() {
        return INSTANCE;
    }

    private DialogManager2() {
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

    public static Dialog getDialog(final DialogInfo dialogInfo) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(dialogInfo.context);
        dialogBuilder
            .setTitle(dialogInfo.titleId)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(dialogInfo.messageId);
        
        if((dialogInfo.flags & NEGATIVE_BUTTON) != 0){
            dialogBuilder.setNegativeButton(
                    dialogInfo.negativeButtonLabelId,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                            int whichButton) {
                        if (dialogInfo.listener != null) {
                            dialogInfo.listener.onNegativeResponse();
                        }
                    }
                });
        }
        
        if((dialogInfo.flags & POSITIVE_BUTTON) != 0){
            dialogBuilder.setPositiveButton(
                    dialogInfo.positiveButtonLabelId,
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
