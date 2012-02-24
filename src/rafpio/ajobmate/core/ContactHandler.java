package rafpio.ajobmate.core;

import java.util.ArrayList;

import rafpio.ajobmate.model.Offer;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

//FIXME: make it a singleton and cache app context
public class ContactHandler {
    public static boolean addEmployerToContacts(Offer offer) {
        boolean ret = false;
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                .withValue(RawContacts.ACCOUNT_TYPE, null)
                .withValue(RawContacts.ACCOUNT_NAME, null).build());

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        offer.getEmployer()).build());

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                        offer.getPhoneNr())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA,
                        offer.getEmail())
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                        ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());

        try {
            Context context = JobmateApplication.getInstance()
                    .getApplicationContext();
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,
                    ops);
            ret = true;
        } catch (RemoteException e) {
            Log.d("RP", "" + e);
        } catch (OperationApplicationException e) {
            Log.d("RP", "" + e);
        }
        return ret;
    }

    public static int getNumOfContacts(String contactName) {
        Context context = JobmateApplication.getInstance()
                .getApplicationContext();
        String where = ContactsContract.Data.DISPLAY_NAME + " = ? ";
        Cursor cur = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, where,
                new String[] { contactName }, null);

        int ret = 0;
        if (cur != null) {
            ret = cur.getCount();
            cur.close();
        }

        return ret;
    }

    public static void deleteContact(String contactName) {
        Context context = JobmateApplication.getInstance()
                .getApplicationContext();
        String where = ContactsContract.Data.DISPLAY_NAME + " = ? ";

        context.getContentResolver().delete(RawContacts.CONTENT_URI, where,
                new String[] { contactName });

    }
}
