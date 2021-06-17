package com.angeloraso.plugins.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class AddToExistingContactActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    String number = intent.getStringExtra("number");

    // Creates a new Intent to insert a contact
    Intent phoneBookIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    // Sets the MIME type to match the Contacts Provider
    phoneBookIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

    phoneBookIntent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
    phoneBookIntent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);

    ActivityResultLauncher<Intent> addToExistingContactActivity = registerForActivityResult(
      new ActivityResultContracts.StartActivityForResult(),
      new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
          ContactsPlugin.onAddToExistingContactActivityResult();
          finishActivity();
        }
      });

    addToExistingContactActivity.launch(phoneBookIntent);

  }

  private void finishActivity() {
    finish();
  }
}
