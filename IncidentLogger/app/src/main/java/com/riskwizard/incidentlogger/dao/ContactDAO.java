package com.riskwizard.incidentlogger.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.riskwizard.incidentlogger.Constants;
import com.riskwizard.incidentlogger.helper.DatabaseHelper;
import com.riskwizard.incidentlogger.model.Contact;
import com.riskwizard.incidentlogger.model.Incident;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mattg on 18/02/2015.
 */
public class ContactDAO {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    // Logcat tag
    private static final String LOG = ContactDAO.class.getName();

    public ContactDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // Close the db
    public void close() {
        db.close();
    }

    // <editor-fold defaultstate="collapsed" desc="Contact Table Methods">

    // Adding new contact
    public long createContact(Contact aContact) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("LastName", aContact.getLastName());
        values.put("FirstName", aContact.getFirstName());
        values.put("PositionTitle", aContact.getFirstName());
        values.put("Company", aContact.getFirstName());
        values.put("EmailAddress", aContact.getFirstName());

        return db.insert(Constants.TABLE_CONTACT, null, values);
    }

    // Getting single contact
    public Contact getContact(int id) {
        Contact aContact = new Contact();

        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Constants.TABLE_CONTACT + " WHERE Id = " + id;

        Log.e(LOG, selectQuery);

        //Cursor c = db.rawQuery(selectQuery, null);
        Cursor c = db.query(Constants.TABLE_CONTACT, // a. table
                new String[] {"Id", "LastName", "FirstName", "PositionTitle", "Company", "EmailAddress"}, // b. column names
                " Id = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);

        if (c.moveToFirst()) {
            aContact.setId(c.getInt(c.getColumnIndex("Id")));
            aContact.setLastName((c.getString(c.getColumnIndex("LastName"))));
            aContact.setFirstName((c.getString(c.getColumnIndex("FirstName"))));
            aContact.setPositionTitle((c.getString(c.getColumnIndex("PositionTitle"))));
            aContact.setCompany((c.getString(c.getColumnIndex("Company"))));
            aContact.setEmailAddress((c.getString(c.getColumnIndex("EmailAddress"))));
        }
        c.close();

        return aContact;
    }

    // Getting single contact
    public Contact getContactByName(String first_name, String last_name) {
        Contact aContact = new Contact();

        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Constants.TABLE_CONTACT + " WHERE FirstName + ' ' + LastName = " + first_name + " " + last_name;

        Log.e(LOG, selectQuery);

        //Cursor c = db.rawQuery(selectQuery, null);
        Cursor c = db.query(Constants.TABLE_CONTACT, // a. table
                new String[] {"Id", "LastName", "FirstName", "PositionTitle", "Company", "EmailAddress"}, // b. column names
                " LastName = ? AND FirstName = ?", // c. selections
                new String[] { String.valueOf(last_name), String.valueOf(first_name) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);

        if (c.moveToFirst()) {
            aContact.setId(c.getInt(c.getColumnIndex("Id")));
            aContact.setLastName((c.getString(c.getColumnIndex("LastName"))));
            aContact.setFirstName((c.getString(c.getColumnIndex("FirstName"))));
            aContact.setPositionTitle((c.getString(c.getColumnIndex("PositionTitle"))));
            aContact.setCompany((c.getString(c.getColumnIndex("Company"))));
            aContact.setEmailAddress((c.getString(c.getColumnIndex("EmailAddress"))));
        }
        c.close();

        return aContact;
    }

    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String selectQuery = "SELECT Id, LastName, FirstName, PositionTitle, Company, EmailAddress FROM " + Constants.TABLE_CONTACT;

        Log.e(LOG, selectQuery);

        db = dbHelper.getReadableDatabase();
        //Cursor c = db.rawQuery(selectQuery, null);

        Cursor c = db.query(Constants.TABLE_CONTACT, // a. table
                new String[] {"Id", "LastName", "FirstName", "PositionTitle", "Company", "EmailAddress"}, // b. column names
                null, // c. selections
                null, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Contact aContact = new Contact();
                aContact.setId(c.getInt((c.getColumnIndex("Id"))));
                aContact.setLastName((c.getString(c.getColumnIndex("LastName"))));
                aContact.setFirstName((c.getString(c.getColumnIndex("FirstName"))));
                aContact.setPositionTitle((c.getString(c.getColumnIndex("PositionTitle"))));
                aContact.setCompany((c.getString(c.getColumnIndex("Company"))));
                aContact.setEmailAddress((c.getString(c.getColumnIndex("EmailAddress"))));

                // adding to incident list
                contacts.add(aContact);
            } while (c.moveToNext());
        }
        c.close();

        return contacts;
    }

    // Updating single contact
    public void updateContact(Contact aContact) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("LastName", aContact.getLastName());
        values.put("FirstName", aContact.getFirstName());
        values.put("PositionTitle", aContact.getFirstName());
        values.put("Company", aContact.getFirstName());
        values.put("EmailAddress", aContact.getFirstName());

        // updating row
        db.update(Constants.TABLE_CONTACT, values, "Id=?", new String[] { String.valueOf(aContact.getId()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        db = dbHelper.getWritableDatabase();
        db.delete(Constants.TABLE_CONTACT, "Id" + " = ?", new String[] { String.valueOf(contact.getId()) });
    }

    //Getting Contact count
    public int getContactCount() {
        String countQuery = "SELECT Id FROM " + Constants.TABLE_CONTACT;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    // </editor-fold>

}
