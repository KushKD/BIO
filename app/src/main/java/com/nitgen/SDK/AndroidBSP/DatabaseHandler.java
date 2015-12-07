package com.nitgen.SDK.AndroidBSP;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuush on 12/2/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    boolean bool = false;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Attendance";

    // Contacts table name
    private static final String TABLE_USERDETAILS = "UserDetails";


    //User Details Columns
    private static final String KEY_ID = "id";
    private static final String FINGER_ONE = "FingerOne";
    private static final String FINGER_TWO = "FingerTwo";
    private static final String AADHAAR = "Aadhaar";
    private static final String NAME = "Name";
    private static final String CAREOFF = "CareOFf";
    private static final String DOB = "DOB";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERDATA_TABLE = "CREATE TABLE " + TABLE_USERDETAILS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                +FINGER_ONE+" TEXT,"
                +FINGER_TWO+" TEXT,"
                +AADHAAR+" TEXT,"
                +NAME+" TEXT,"
                +CAREOFF+" TEXT,"
                + DOB + " TEXT" + ")";
        db.execSQL(CREATE_USERDATA_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDETAILS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    Boolean addContact(POJO_User_Save Details ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FINGER_ONE, Details.getFingure_One_User()); // Fingure String
        values.put(FINGER_TWO, Details.getFingure_Two_User());
        values.put(AADHAAR, Details.getAadhaar_Save_User());
        values.put(NAME, Details.getName_Save_User());
        values.put(CAREOFF, Details.getCO_Save_User());
        values.put(DOB, Details.getDOB_Save_User());
        // Inserting Row
        db.insert(TABLE_USERDETAILS, null, values);
        db.close(); // Closing database connection

        try{
            exportDatabse(DATABASE_NAME);
        }catch (Exception e){
            Log.d("Got Error ..",e.getLocalizedMessage());
        }
        return true;
    }

    // Adding new contact
   /* void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }*/

    // Getting single contact
   /* Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REPORTING, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }*/

    // Getting All Contacts
  /*  public List<FingurePoJo> getAllContacts() {
        List<FingurePoJo> FingureList = new ArrayList<FingurePoJo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REPORTING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FingurePoJo FP = new FingurePoJo();
               // contact.setID(Integer.parseInt(cursor.getString(0)));
               // contact.setName(cursor.getString(1));
               // contact.setPhoneNumber(cursor.getString(2));
                FP.setFingure_DB(cursor.getString(1));
                Log.d(cursor.getString(1) , "Nothing");
                FingureList.add(FP);
            } while (cursor.moveToNext());
        }
        // return contact list
        return FingureList;
    }*/

    // Updating single contact
  /*  public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }*/

    // Deleting single contact
  /*  public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }*/


    // Getting contacts Count
 /*   public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REPORTING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }*/

    public void exportDatabse(String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//"+DatabaseHandler.class.getPackage().getName()+"//databases//"+databaseName+"";
                String backupDBPath = "fingure.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
                else{
                    Log.d("Error","No Idea");
                }
            }else{
                Log.d("Error", "No Idea 2");
            }
        } catch (Exception e) {

        }
    }
}
