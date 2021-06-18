package com.angeloraso.plugins.contacts;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import java.util.ArrayList;

public class AddToExistingContactActivity extends AppCompatActivity {
  static final int PICK_CONTACT = -1;
  public static Cursor cursor;
  // A content URI pointing to the contact
  public static Uri selectedContactUri;
  public static String phoneNumber;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    AddToExistingContactActivity.phoneNumber = intent.getStringExtra("number");

    Intent phoneBookIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

    ActivityResultLauncher<Intent> addToExistingContactActivity = registerForActivityResult(
      new ActivityResultContracts.StartActivityForResult(),
      new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
          if (result.getResultCode() == PICK_CONTACT) {
            if (result.getResultCode() == Activity.RESULT_OK) {
              Uri contactsData = result.getData().getData();
              // The Cursor that contains the Contact row
              AddToExistingContactActivity.cursor = getContentResolver().query(contactsData, null, null, null, null);
              if (null != AddToExistingContactActivity.cursor && AddToExistingContactActivity.cursor.getCount() > 0) {
                AddToExistingContactActivity.cursor.moveToFirst();
              }

              if(AddToExistingContactActivity.cursor.getCount() > 0) {

                int idIndex = AddToExistingContactActivity.cursor.getColumnIndex(ContactsContract.Contacts._ID);
                // Gets the lookup key column index
                int lookupKeyIndex = AddToExistingContactActivity.cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
                // Gets the lookup key value
                String currentLookupKey = AddToExistingContactActivity.cursor.getString(lookupKeyIndex);
                // Gets the _ID column index
                long currentId = AddToExistingContactActivity.cursor.getInt(idIndex);
                AddToExistingContactActivity.selectedContactUri = ContactsContract.Contacts.getLookupUri(currentId, currentLookupKey);

                Intent intent = new Intent("android.intent.action.EDIT_CONTACT_ACTIVITY");
                intent.setPackage(getPackageName());
                startActivity(intent);
                finishActivity();
              }
            }
          }
        }
      });

    addToExistingContactActivity.launch(phoneBookIntent);

  }

  private void finishActivity() {
    finish();
  }
}
