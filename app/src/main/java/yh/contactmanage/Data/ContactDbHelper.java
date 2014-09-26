package yh.contactmanage.Data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Objects;

import yh.contactmanage.Data.Contracts.ContactContract;
import yh.contactmanage.Models.ContactModel;

/**
 * Created by Magnus on 2014-09-17.
 */
public class ContactDbHelper extends SQLiteOpenHelper {

    //Class variables
    private boolean sortByName = false;

    // Constants
    private final static String DATABASE_NAME = "ContactTable.db";
    private final static int DATABASE_VERSION = 11;
    private final static String COMMA_SEP = ",";
    private final static String TEXT_TYPE = " TEXT";


    // Projections
    String[] projection = {
            ContactContract.Contact._ID,
            ContactContract.Contact.COLUMN_NAME_URL,
            ContactContract.Contact.COLUMN_NAME_NAME,
            ContactContract.Contact.COLUMN_NAME_AGE,
            ContactContract.Contact.COLUMN_NAME_DESCRIPTION
    };

    String[] contactProjection = {
            ContactContract.Contact.COLUMN_NAME_URL,
            ContactContract.Contact.COLUMN_NAME_NAME,
            ContactContract.Contact.COLUMN_NAME_AGE,
            ContactContract.Contact.COLUMN_NAME_DESCRIPTION
    };

    // Variables
    SQLiteDatabase db;


    // Database creation SQL statement
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + ContactContract.Contact.TABLE_NAME + " (" +
                    ContactContract.Contact._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    ContactContract.Contact.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    ContactContract.Contact.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    ContactContract.Contact.COLUMN_NAME_AGE + TEXT_TYPE + COMMA_SEP +
                    ContactContract.Contact.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + " )";

    // Database delete SQL statement
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + ContactContract.Contact.TABLE_NAME;

    //CONSTRUCTOR
    public ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create database method
    @Override
    public void onCreate(SQLiteDatabase db) {

        try{
            System.out.println("Trying to create table");
            db.execSQL(SQL_CREATE_TABLE);
            Log.d("GalleryDbHelper", "Table created version" + DATABASE_VERSION);
            System.out.println("Created table");
        }
        catch (Exception ex){
            Log.d("GalleryDbHelper", ex.getMessage());
        }
    }

    /**
     * check if the newVersion is the same as old version. If not. Delete the current database table
     * and create a new one.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion != oldVersion){

            try{
                db.execSQL(SQL_DELETE_TABLE);
                onCreate(db);
            }
            catch (Exception ex){
                Log.d("GalleryDbHelper", ex.getMessage());
            }

        }
    }

    /**
     * Gets all the rows in the database and returns them.
     * @return
     */
    public Cursor get(){

        String sortName = null;
        if(sortByName){
            sortName = ContactContract.Contact.COLUMN_NAME_NAME + " ASC";
        }

        db = getReadableDatabase();

        Cursor cursor = db.query(
                ContactContract.Contact.TABLE_NAME,  // The table to query
                projection,              // The columns to return
                null,                    // The columns for the WHERE clause
                null,                    // The values for the WHERE clause
                null,                    // don't group the rows
                null,                    // don't filter by row groups
                sortName                     // The sort order
        );

        return cursor;
    }


    public void toggleSorting() {
        if(sortByName){
            sortByName = false;
        }else{
           sortByName = true;
        }
    }

    /**
     * Gets the row from the database where _ID equals the parameters id and returns it.
     * @param id
     * @return
     */
    public Cursor getContact(long id){

        String contactId = String.valueOf(id);

        db = getReadableDatabase();

        Cursor cursor = db.query(
                ContactContract.Contact.TABLE_NAME,     // The table to query
                contactProjection,                      // The columns to return
                ContactContract.Contact._ID + "=?",     // The columns for the WHERE clause
                new String[]{contactId},                // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );

        cursor.moveToFirst();
        return cursor;
    }

    /**
     * Creates a new row in the database of the parameter contactModel.
     * @param contactModel
     * @return
     */
    public long insert(ContactModel contactModel){

        db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactContract.Contact.COLUMN_NAME_URL, contactModel.getUrl());
        contentValues.put(ContactContract.Contact.COLUMN_NAME_NAME, contactModel.getName());
        contentValues.put(ContactContract.Contact.COLUMN_NAME_AGE, contactModel.getAge());
        contentValues.put(ContactContract.Contact.COLUMN_NAME_DESCRIPTION, contactModel.getDescription());

        long createdRowId = db.insert(ContactContract.Contact.TABLE_NAME, null, contentValues);

        return createdRowId;
    }

    /**
     * Deletes the row from the database where _ID equals the parameters id.
     * @param id
     */
    public void remove(long id){

        String rowId = String.valueOf(id);

        db.delete(ContactContract.Contact.TABLE_NAME, ContactContract.Contact._ID + "=?", new String[]{rowId});
    }

    /**
     * Updates the row in the database that has the _ID that is the same as the parameter id with
     * the content of parameter contactModel.
     * @param contactModel
     * @param id
     * @return
     */
    public long update(ContactModel contactModel, long id) {

        String rowId = String.valueOf(id);
        ContentValues contentValues = new ContentValues();

        contentValues.put(ContactContract.Contact.COLUMN_NAME_URL, contactModel.getUrl());
        contentValues.put(ContactContract.Contact.COLUMN_NAME_NAME, contactModel.getName());
        contentValues.put(ContactContract.Contact.COLUMN_NAME_AGE, contactModel.getAge());
        contentValues.put(ContactContract.Contact.COLUMN_NAME_DESCRIPTION, contactModel.getDescription());

        long updatedRowId = db.update(ContactContract.Contact.TABLE_NAME, contentValues, ContactContract.Contact._ID + "=?", new String[]{rowId});

        return updatedRowId;
    }
}