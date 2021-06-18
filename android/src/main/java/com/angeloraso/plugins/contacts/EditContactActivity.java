package com.angeloraso.plugins.contacts;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;

public class EditContactActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Creates a new Intent to edit a contact
    Intent editIntent = new Intent(Intent.ACTION_EDIT);
    /*
     * Sets the contact URI to edit, and the data type that the
     * Intent must match
     */
    editIntent.setDataAndType(AddToExistingContactActivity.selectedContactUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);

    editIntent.putExtra(ContactsContract.Intents.Insert.PHONE, AddToExistingContactActivity.phoneNumber);
    editIntent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);

    ActivityResultLauncher<Intent> editContactActivity = registerForActivityResult(
      new ActivityResultContracts.StartActivityForResult(),
      new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
          ContactsPlugin.onAddToExistingContactActivityResult();
          finishActivity();
        }
      });

    editContactActivity.launch(editIntent);

  }

  private void finishActivity() {
    finish();
  }
}
