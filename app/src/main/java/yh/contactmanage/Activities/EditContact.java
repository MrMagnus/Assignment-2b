package yh.contactmanage.Activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import yh.contactmanage.Data.ContactDbHelper;
import yh.contactmanage.Data.Contracts.ContactContract;
import yh.contactmanage.Models.ContactModel;
import yh.contactmanage.R;


public class EditContact extends Activity {

    //Class variables
    ContactDbHelper contactDbHelper;

    private EditText editUrlEt;
    private EditText editNameEt;
    private EditText editAgeEt;
    private EditText editDescriptionEt;

    private long chosenContactId;

    private String chosenContactUrl;
    private String chosenContactName;
    private String chosenContactAge;
    private String chosenContactDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        contactDbHelper = new ContactDbHelper(this);

        editUrlEt = (EditText) findViewById(R.id.nameAndAgeTv);
        editNameEt = (EditText) findViewById(R.id.nameEt);
        editAgeEt = (EditText) findViewById(R.id.ageEt);
        editDescriptionEt = (EditText) findViewById(R.id.descrEt);

        //Gets the values of the chosen contact and displays it on the view
        chosenContactId = getIntent().getExtras().getLong("chosenContactItemId", 0);

        //Get the values of the contact that was clicked in the list
        Cursor chosenContactCursor = contactDbHelper.getContact(chosenContactId);

        //Get the values from the database columns
        chosenContactUrl = chosenContactCursor.getString((chosenContactCursor.getColumnIndex(ContactContract.Contact.COLUMN_NAME_URL)));
        chosenContactName = chosenContactCursor.getString((chosenContactCursor.getColumnIndex(ContactContract.Contact.COLUMN_NAME_NAME)));
        chosenContactAge = chosenContactCursor.getString((chosenContactCursor.getColumnIndex(ContactContract.Contact.COLUMN_NAME_AGE)));
        chosenContactDescription = chosenContactCursor.getString((chosenContactCursor.getColumnIndex(ContactContract.Contact.COLUMN_NAME_DESCRIPTION)));

        //Set the database values to the edit texts
        editUrlEt.setText(chosenContactUrl, TextView.BufferType.EDITABLE);
        editNameEt.setText(chosenContactName, TextView.BufferType.EDITABLE);
        editAgeEt.setText(chosenContactAge, TextView.BufferType.EDITABLE);
        editDescriptionEt.setText(chosenContactDescription, TextView.BufferType.EDITABLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_contact, menu);
        return true;
    }

    /**
     * Sends off the edited or the new data to the MainActivity through the ContactInfo activity.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.actionCreateAccept) {

            String editContactUrl = editUrlEt.getText().toString().trim();
            String editContactName = editNameEt.getText().toString().trim();
            String editContactAge = editAgeEt.getText().toString().trim();
            String editContactDescr = editDescriptionEt.getText().toString().trim();

            if (!editContactUrl.equals("") && !editContactName.equals("") &&
                !editContactAge.equals("") && !editContactDescr.equals("")) {

                updateContact(editContactUrl, editContactName, editContactAge, editContactDescr, chosenContactId);

                finish();

            } else {

                Toast.makeText(this, R.string.toastInputErrorText, Toast.LENGTH_SHORT).show();
            }

        }

        if(id == R.id.actionCreateCancel){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates a new contact, sends it to the insert method and updates the adapter
     * @param url
     * @param name
     * @param age
     * @param description
     */
    public void updateContact(String url, String name, String age, String description, long id) {

        ContactModel contactModel = new ContactModel(url, name, age, description);

        // Insert galleryFrame object
        contactDbHelper.update(contactModel, id);

        // Get latest data
        Cursor contactCursor = contactDbHelper.get();

        // Set new cursor data on the adapter.
        MainActivity.contactAdapter.changeCursor(contactCursor);

    }
}
