package yh.contactmanage.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

import yh.contactmanage.Data.ContactDbHelper;
import yh.contactmanage.R;

import static yh.contactmanage.Data.Contracts.ContactContract.*;


public class ContactInfo extends Activity {

    //Class variables
    ContactDbHelper contactDbHelper;

    private long chosenContactId;

    String chosenContactUrl;
    String chosenContactName;
    String chosenContactAge;
    String chosenContactDescription;

    private ImageView portraitIv;
    private TextView nameAndAgeTv;
    private TextView descriptionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        contactDbHelper = new ContactDbHelper(this);

        portraitIv = (ImageView) findViewById(R.id.portraitIv);
        nameAndAgeTv = (TextView) findViewById(R.id.nameAndAgeTv);
        descriptionTv = (TextView) findViewById(R.id.descriptionTv);

        //Gets the values of the chosen contact and displays it on the view
        chosenContactId = getIntent().getExtras().getLong("chosenContactItemId", 0);

        //Get the values of the contact that was clicked in the list
        Cursor chosenContactCursor = contactDbHelper.getContact(chosenContactId);

        //Get the values from the database columns
        chosenContactUrl = chosenContactCursor.getString((chosenContactCursor.getColumnIndex(Contact.COLUMN_NAME_URL)));
        chosenContactName = chosenContactCursor.getString((chosenContactCursor.getColumnIndex(Contact.COLUMN_NAME_NAME)));
        chosenContactAge = chosenContactCursor.getString((chosenContactCursor.getColumnIndex(Contact.COLUMN_NAME_AGE)));
        chosenContactDescription = chosenContactCursor.getString((chosenContactCursor.getColumnIndex(Contact.COLUMN_NAME_DESCRIPTION)));

        Picasso.with(this).load(chosenContactUrl)
            .placeholder(R.drawable.ic_placeholder_big)
            .error(R.drawable.ic_placeholder_big)
            .into(portraitIv);

        //Set the texts to the values from the database
        nameAndAgeTv.setText(chosenContactName + ", " + chosenContactAge + getResources().getString(R.string.contactInfoAgeString));
        descriptionTv.setText(chosenContactDescription);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_info, menu);
        return true;
    }

    @Override
    protected void onResume() {
        onCreate(null);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){

            //Removes the current contact
            case R.id.actionRemoveContact:

                contactDbHelper.remove(chosenContactId);

                // Get latest data
                Cursor contactCursor = contactDbHelper.get();

                // Set new cursor data on the adapter.
                MainActivity.contactAdapter.changeCursor(contactCursor);

                finish();

            break;

            //Sends the chosen contact id to the EditContact activity
            case R.id.actionEditContact:

                Intent editContactIntent = new Intent(this, EditContact.class);

                editContactIntent.putExtra("chosenContactItemId", chosenContactId);

                startActivity(editContactIntent);

            break;
        }

        return super.onOptionsItemSelected(item);
    }

}
