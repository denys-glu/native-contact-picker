/**
 */
package com.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;

//pickContacts dependencies
import android.content.Intent;
import android.provider.ContactsContract;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.app.Activity;
//******************
import android.util.Log;

import java.util.Date;

public class NativeContactPicker extends CordovaPlugin {
    private static final String TAG = "NativeContactPicker";
    private CallbackContext callback = null;

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.d(TAG, "Initializing NativeContactPicker");
    }

    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        Context context = cordova.getActivity().getApplicationContext();
        if (action.equals("parseLastNineDigits")) {
            callback = callbackContext;
            this.openNewActivity(context, callbackContext);

        }
        return true;
    }

    private void openNewActivity(Context context, CallbackContext callbackContext) {

        PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
        r.setKeepCallback(true);
        callbackContext.sendPluginResult(r);

        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        cordova.startActivityForResult((CordovaPlugin) this, pickContactIntent, 111);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Context context = cordova.getActivity().getApplicationContext();

        if ((requestCode == 111) && (resultCode == Activity.RESULT_OK)) {
            Uri contactUri = data.getData();
            contactUri.toString();
            String id, name;
            String queriedNumber = "";
            ContentResolver resolver = context.getContentResolver();
            Cursor cur = resolver.query(contactUri, null, null, null, null);
            if (cur.moveToFirst()) {
                id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                // If contact record has phone number
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    // Get cursor
                    Cursor pCur = resolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone._ID + " = ?",
                            new String[]{id},
                            null);
                    // Iterate cursor
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        queriedNumber = parseLastNineDigits(phoneNo);
                        if (callback != null) {
                            callback.sendPluginResult(new PluginResult(PluginResult.Status.OK, queriedNumber));
                            callback = null;
                        }
                    }
                    pCur.close();



                }
            }
        }
    }


    private static String parseLastNineDigits(String phoneNumber) {
        //Remove all non numeric symbols in number
        String nospaceNumber = "0";
        try {
            nospaceNumber = phoneNumber.replaceAll("\\D+", "");
        } catch (NullPointerException e) {
            Log.e("noPhoneNumber", e.toString());
        } finally {

        }
        // Get last 10 digits of phone number
        return nospaceNumber.length() > 9
                ? nospaceNumber.substring(nospaceNumber.length() - 9)
                : nospaceNumber;
    }
}
