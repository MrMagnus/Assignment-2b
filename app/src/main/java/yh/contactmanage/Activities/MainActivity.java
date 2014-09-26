package yh.contactmanage.Activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import yh.contactmanage.Adapters.ContactCursorAdapter;
import yh.contactmanage.Data.ContactDbHelper;
import yh.contactmanage.Models.ContactModel;
import yh.contactmanage.R;


public class MainActivity extends ListActivity implements AdapterView.OnItemLongClickListener, DialogInterface.OnClickListener{

    //Class variables
    public static ContactCursorAdapter contactAdapter;
    ContactDbHelper contactDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactDbHelper = new ContactDbHelper(this);

        //Get latest data
        Cursor contactCursor = contactDbHelper.get();

        //Create a new instance of and pass context and a ContentCursor from a database
        contactAdapter = new ContactCursorAdapter(this, contactCursor, false);

        setListAdapter(contactAdapter);

        //Set the adapter to the ListView
        getListView().setOnItemLongClickListener(this);

    }

    @Override
    protected void onResume(){
        super.onResume();
        contactAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.actionCreateContact) {

            Intent intentAdd = new Intent(this, CreateContact.class);
            startActivity(intentAdd);
        }

        if (id == R.id.actionSortContactList) {

            contactDbHelper.toggleSorting();
            Cursor contactCursor = contactDbHelper.get();

            contactAdapter.changeCursor(contactCursor);

            contactAdapter.notifyDataSetChanged();

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Send the _ID of the selected contact to the ContactInfo activity
     * @param listView
     * @param view
     * @param position
     * @param id
     */
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {

        Intent contactInfoIntent = new Intent(this, ContactInfo.class);

        contactInfoIntent.putExtra("chosenContactItemId", id);

        startActivity(contactInfoIntent);

    }

    /**
     * If the user long clicks an item a Dialog will be displayed and the user will be given a
     * choice to remove the selected item or to cancel.
     * @param adapterView
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

        contactDbHelper.remove(id);

        // Get latest data
        Cursor contactCursor = contactDbHelper.get();

        contactAdapter.changeCursor(contactCursor);

        // Notify data set changed
        contactAdapter.notifyDataSetChanged();

        return false;
    }


    /**
     * If the user select the positive alternative in the dialog the selectet item will be removed.
     * Otherwise do nothing.
     * @param dialog
     * @param which
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == AlertDialog.BUTTON_POSITIVE){


        }
        dialog.dismiss();
    }

}
