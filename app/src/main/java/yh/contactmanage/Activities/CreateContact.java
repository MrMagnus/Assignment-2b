package yh.contactmanage.Activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import yh.contactmanage.Data.ContactDbHelper;
import yh.contactmanage.Models.ContactModel;
import yh.contactmanage.R;


public class CreateContact extends Activity {

    //Class variables
    ContactDbHelper contactDbHelper;

    private EditText urlEt;
    private EditText nameEt;
    private EditText ageEt;
    private EditText descrEt;

    /**
     * If the user comes from the ContactInfo activity and wants to edit a user, the Edit text
     * fields will already be filled in with the values of the contact.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        contactDbHelper = new ContactDbHelper(this);

        urlEt = (EditText) findViewById(R.id.nameAndAgeTv);
        nameEt = (EditText) findViewById(R.id.nameEt);
        ageEt = (EditText) findViewById(R.id.ageEt);
        descrEt = (EditText) findViewById(R.id.descrEt);
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

            String newContactUrl = urlEt.getText().toString().trim();
            String newContactName = nameEt.getText().toString().trim();
            String newContactAge = ageEt.getText().toString().trim();
            String newContactDescr = descrEt.getText().toString().trim();

            if (!newContactUrl.equals("") && !newContactName.equals("") &&
                !newContactAge.equals("") && !newContactDescr.equals("")) {

                addContact(newContactUrl, newContactName, newContactAge, newContactDescr);

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
     * Creates a new contact, insert it into the database and updates the adapter
     * @param url
     * @param name
     * @param age
     * @param description
     */
    public void addContact(String url, String name, String age, String description ) {

        // Create a new object of GalleryFrame
        ContactModel contactModel = new ContactModel(url, name, age, description);

        // Insert galleryFrame object
        contactDbHelper.insert(contactModel);

        // Get latest data
        Cursor contactCursor = contactDbHelper.get();

        // Set new cursor data on the adapter.
        MainActivity.contactAdapter.changeCursor(contactCursor);

    }
}
