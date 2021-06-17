package com.angeloraso.plugins.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Base64;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

import org.json.JSONException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@CapacitorPlugin(
    name = "Contacts",
    permissions = {
        @Permission(
            alias = "contacts",
            strings = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
            }
        )
    }
)
public class ContactsPlugin extends Plugin {
    private static PluginCall createContactCall;
    private static PluginCall addToExistingContactCall;
    private static final String CONTACT_ID = "contactId";
    private static final String EMAILS = "emails";
    private static final String EMAIL_LABEL = "label";
    private static final String EMAIL_ADDRESS = "address";
    private static final String PHONE_NUMBERS = "phoneNumbers";
    private static final String PHONE_LABEL = "label";
    private static final String PHONE_NUMBER = "number";
    private static final String DISPLAY_NAME = "displayName";
    private static final String PHOTO_THUMBNAIL = "photoThumbnail";
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String ORGANIZATION_ROLE = "organizationRole";
    private static final String BIRTHDAY = "birthday";

    @PluginMethod
    public void getPermissions(PluginCall call) {
        JSObject output = new JSObject();
        if (getPermissionState("contacts") != PermissionState.GRANTED) {
            output.put("granted", false);
        } else {
            output.put("granted", true);
        }
        call.resolve(output);
    }

    @PluginMethod
    public void requestPermissions(PluginCall call) {
        if (getPermissionState("contacts") != PermissionState.GRANTED) {
            requestPermissionForAlias("contacts", call, "contactsPermsCallback");
        } else {
            JSObject output = new JSObject();
            output.put("granted", true);
            call.resolve(output);
        }
    }

    @PermissionCallback
    private void contactsPermsCallback(PluginCall call) {
        JSObject output = new JSObject();
        if (getPermissionState("contacts") == PermissionState.GRANTED) {
            output.put("granted", true);
        } else {
            output.put("granted", false);
        }
        call.resolve(output);
    }

    @PluginMethod
    public void getContacts(PluginCall call) {
        // initialize array
        JSArray jsContacts = new JSArray();

        ContentResolver contentResolver = getContext().getContentResolver();

        String[] projection = new String[] {
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Organization.TITLE,
                ContactsContract.Contacts._ID,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.Photo.PHOTO,
                ContactsContract.CommonDataKinds.Contactables.DATA,
                ContactsContract.CommonDataKinds.Contactables.TYPE,
                ContactsContract.CommonDataKinds.Contactables.LABEL,
        };
        String selection = ContactsContract.Data.MIMETYPE + " in (?, ?, ?, ?, ?)";
        String[] selectionArgs = new String[] {
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE,
        };

        Cursor contactsCursor = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (contactsCursor != null && contactsCursor.getCount() > 0) {
            HashMap<Object, JSObject> contactsById = new HashMap<>();

            while (contactsCursor.moveToNext()) {
                String _id = contactsCursor.getString(
                        contactsCursor.getColumnIndex(ContactsContract.Contacts._ID)
                );
                String contactId = contactsCursor.getString(
                        contactsCursor.getColumnIndex(ContactsContract.Data.CONTACT_ID)
                );

                JSObject jsContact = new JSObject();

                if (!contactsById.containsKey(contactId)) {
                    // this contact does not yet exist in HashMap,
                    // so put it to the HashMap

                    jsContact.put(CONTACT_ID, contactId);
                    String displayName = contactsCursor.getString(
                            contactsCursor.getColumnIndex(
                                    ContactsContract.Contacts.DISPLAY_NAME
                            )
                    );

                    jsContact.put(DISPLAY_NAME, displayName);
                    JSArray jsPhoneNumbers = new JSArray();
                    jsContact.put(PHONE_NUMBERS, jsPhoneNumbers);
                    JSArray jsEmailAddresses = new JSArray();
                    jsContact.put(EMAILS, jsEmailAddresses);

                    jsContacts.put(jsContact);
                } else {
                    // this contact already exists,
                    // retrieve it
                    jsContact = contactsById.get(contactId);
                }

                if (jsContact != null) {
                    String mimeType = contactsCursor.getString(
                            contactsCursor.getColumnIndex(ContactsContract.Data.MIMETYPE)
                    );
                    String data = contactsCursor.getString(
                            contactsCursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Contactables.DATA
                            )
                    );
                    int type = contactsCursor.getInt(
                            contactsCursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Contactables.TYPE
                            )
                    );
                    String label = contactsCursor.getString(
                            contactsCursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Contactables.LABEL
                            )
                    );

                    // email
                    if (mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                        try {
                            // add this email to the list
                            JSArray emailAddresses = (JSArray) jsContact.get(EMAILS);
                            JSObject jsEmail = new JSObject();
                            jsEmail.put(EMAIL_LABEL, mapEmailTypeToLabel(type, label));
                            jsEmail.put(EMAIL_ADDRESS, data);
                            emailAddresses.put(jsEmail);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // phone
                    else if (mimeType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                        try {
                            // add this phone to the list
                            JSArray jsPhoneNumbers = (JSArray) jsContact.get(PHONE_NUMBERS);
                            JSObject jsPhone = new JSObject();
                            jsPhone.put(PHONE_LABEL, mapPhoneTypeToLabel(type, label));
                            jsPhone.put(PHONE_NUMBER, data);
                            jsPhoneNumbers.put(jsPhone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // birthday
                    else if (mimeType.equals(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)) {
                        int eventType = contactsCursor.getInt(
                                contactsCursor.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Contactables.TYPE
                                )
                        );
                        if (eventType == ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY) {
                            jsContact.put(BIRTHDAY, data);
                        }
                    }
                    // organization
                    else if (mimeType.equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
                        jsContact.put(ORGANIZATION_NAME, data);
                        String organizationRole = contactsCursor.getString(
                                contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE)
                        );
                        if (organizationRole != null) {
                            jsContact.put(ORGANIZATION_ROLE, organizationRole);
                        }
                    }
                    // photo
                    else if (mimeType.equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
                        byte[] thumbnailPhoto = contactsCursor.getBlob(
                                contactsCursor.getColumnIndex(
                                        ContactsContract.Contacts.Photo.PHOTO
                                )
                        );
                        if (thumbnailPhoto != null) {
                            String encodedThumbnailPhoto = Base64.encodeToString(
                                    thumbnailPhoto,
                                    Base64.NO_WRAP
                            );
                            jsContact.put(
                                    PHOTO_THUMBNAIL,
                                    "data:image/png;base64," + encodedThumbnailPhoto
                            );
                        }
                    }

                    contactsById.put(contactId, jsContact);
                }
            }
        }
        if (contactsCursor != null) {
            contactsCursor.close();
        }

        JSObject result = new JSObject();
        result.put("contacts", jsContacts);
        call.resolve(result);
    }

    @PluginMethod
    public void createContact(PluginCall call) {
        if (!call.hasOption("number")) {
            call.reject("The number is required");
            return;
        }

        ContactsPlugin.createContactCall = call;

        Intent intent = new Intent("android.intent.action.CREATE_CONTACT_ACTIVITY");
        intent.setPackage(getContext().getPackageName());

        intent.putExtra("number", call.getString("number"));
        if (call.hasOption("name")) {
            intent.putExtra("name", call.getString("name"));
        }

        getContext().startActivity(intent);
    }

    public static void onCreateContactActivityResult() {
        ContactsPlugin.createContactCall.resolve();
    }

    @PluginMethod
    public void addToExistingContact(PluginCall call) {
        if (!call.hasOption("number")) {
            call.reject("The number is required");
            return;
        }

        ContactsPlugin.addToExistingContactCall = call;

        Intent intent = new Intent("android.intent.action.PHONE_BOOK_ACTIVITY");
        intent.setPackage(getContext().getPackageName());

        intent.putExtra("number", call.getString("number"));

        getContext().startActivity(intent);
    }
    public static void onAddToExistingContactActivityResult() {
        ContactsPlugin.addToExistingContactCall.resolve();
    }

    @PluginMethod
    public void getGroups(PluginCall call) {
        JSObject result = new JSObject();
        JSArray jsGroups = new JSArray();
        Cursor dataCursor = getContext()
                .getContentResolver()
                .query(ContactsContract.Groups.CONTENT_URI, null, null, null, null);

        while (dataCursor.moveToNext()) {
            JSObject jsGroup = new JSObject();
            String groupId = dataCursor.getString(
                    dataCursor.getColumnIndex(ContactsContract.Groups._ID)
            );
            jsGroup.put("groupId", groupId);
            jsGroup.put(
                    "accountType",
                    dataCursor.getString(
                            dataCursor.getColumnIndex(ContactsContract.Groups.ACCOUNT_TYPE)
                    )
            );
            jsGroup.put(
                    "accountName",
                    dataCursor.getString(
                            dataCursor.getColumnIndex(ContactsContract.Groups.ACCOUNT_NAME)
                    )
            );
            jsGroup.put(
                    "title",
                    dataCursor.getString(
                            dataCursor.getColumnIndex(ContactsContract.Groups.TITLE)
                    )
            );
            jsGroups.put(jsGroup);
        }
        dataCursor.close();

        result.put("groups", jsGroups);
        call.resolve(result);
    }

    @PluginMethod
    public void getContactGroups(PluginCall call) {
        Cursor dataCursor = getContext()
                .getContentResolver()
                .query(
                        ContactsContract.Data.CONTENT_URI,
                        new String[] {
                                ContactsContract.Data.CONTACT_ID,
                                ContactsContract.Data.DATA1,
                        },
                        ContactsContract.Data.MIMETYPE + "=?",
                        new String[] {
                                ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
                        },
                        null
                );

        Map<String, Set<String>> contact2GroupMap = new HashMap<>();
        while (dataCursor.moveToNext()) {
            String contact_id = dataCursor.getString(0);
            String group_id = dataCursor.getString(1);

            Set<String> groups = new HashSet<>();
            if (contact2GroupMap.containsKey(contact_id)) {
                groups = contact2GroupMap.get(contact_id);
            }
            groups.add(group_id);
            contact2GroupMap.put(contact_id, groups);
        }
        dataCursor.close();

        JSObject result = new JSObject();
        for (Map.Entry<String, Set<String>> entry : contact2GroupMap.entrySet()) {
            JSArray jsGroups = new JSArray();
            Set<String> groups = entry.getValue();
            for (String group : groups) {
                jsGroups.put(group);
            }
            result.put(entry.getKey(), jsGroups);
        }

        call.resolve(result);
    }

    @PluginMethod
    public void deleteContact(PluginCall call) {
        Uri uri = Uri.withAppendedPath(
                ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                call.getString(CONTACT_ID)
        );
        getContext().getContentResolver().delete(uri, null, null);

        JSObject result = new JSObject();
        call.resolve(result);
    }

    private String mapPhoneTypeToLabel(int type, String defaultLabel) {
        switch (type) {
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                return "mobile";
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                return "home";
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                return "work";
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                return "fax work";
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                return "fax home";
            case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                return "pager";
            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                return "other";
            case ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK:
                return "callback";
            case ContactsContract.CommonDataKinds.Phone.TYPE_CAR:
                return "car";
            case ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN:
                return "company main";
            case ContactsContract.CommonDataKinds.Phone.TYPE_ISDN:
                return "isdn";
            case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                return "main";
            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX:
                return "other fax";
            case ContactsContract.CommonDataKinds.Phone.TYPE_RADIO:
                return "radio";
            case ContactsContract.CommonDataKinds.Phone.TYPE_TELEX:
                return "telex";
            case ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD:
                return "tty";
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                return "work mobile";
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER:
                return "work pager";
            case ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT:
                return "assistant";
            case ContactsContract.CommonDataKinds.Phone.TYPE_MMS:
                return "mms";
            default:
                return defaultLabel;
        }
    }

    private String mapEmailTypeToLabel(int type, String defaultLabel) {
        switch (type) {
            case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                return "home";
            case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                return "work";
            case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:
                return "other";
            case ContactsContract.CommonDataKinds.Email.TYPE_MOBILE:
                return "mobile";
            default:
                return defaultLabel;
        }
    }
}
