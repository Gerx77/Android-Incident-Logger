package com.riskwizard.incidentlogger.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.riskwizard.incidentlogger.Constants;
import com.riskwizard.incidentlogger.helper.DatabaseHelper;
import com.riskwizard.incidentlogger.model.Incident;
import com.riskwizard.incidentlogger.model.IncidentType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mattg on 18/02/2015.
 */
public class IncidentTypeDAO {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    // Logcat tag
    private static final String LOG = IncidentTypeDAO.class.getName();

    public IncidentTypeDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // Close the db
    public void close() {
        db.close();
    }


    // <editor-fold defaultstate="collapsed" desc="Incident Type Table Methods">

    // Getting single incident type
    public IncidentType getIncidentType(long id) {
        IncidentType anIncidentType = new IncidentType();
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Constants.TABLE_INCIDENTTYPE + " WHERE Id = " + id;

        Log.e(LOG, selectQuery);

        //Cursor c = db.rawQuery(selectQuery, null);
        Cursor c = db.query(Constants.TABLE_INCIDENTTYPE, // a. table
                new String[] {"Id", "IncidentTypeName", "Description"}, // b. column names
                " Id = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);

        if (c.moveToFirst()) {
            anIncidentType.setId(c.getInt(c.getColumnIndex("Id")));
            anIncidentType.setIncidentTypeName((c.getString(c.getColumnIndex("IncidentTypeName"))));
            anIncidentType.setIncidentTypeDescription((c.getString(c.getColumnIndex("Description"))));
        }
        c.close();

        return anIncidentType;
    }

    public IncidentType getIncidentType(String incidentTypeName) {
        IncidentType anIncidentType = new IncidentType();

        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT Id, IncidentTypeName, Description FROM " + Constants.TABLE_INCIDENTTYPE + " WHERE IncidentTypeName=?";

        Log.e(LOG, selectQuery);

        //Cursor c = db.rawQuery(selectQuery, new String[] { String.valueOf(incidentTypeName) });
        Cursor c = db.query(Constants.TABLE_INCIDENTTYPE, // a. table
                        new String[] {"Id", "IncidentTypeName", "Description"}, // b. column names
                        " IncidentTypeName = ?", // c. selections
                        new String[] { String.valueOf(incidentTypeName) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null);

        if (c.moveToFirst()) {
            anIncidentType.setId(c.getInt(c.getColumnIndex("Id")));
            anIncidentType.setIncidentTypeName((c.getString(c.getColumnIndex("IncidentTypeName"))));
            anIncidentType.setIncidentTypeDescription((c.getString(c.getColumnIndex("Description"))));
        }
        c.close();

        return anIncidentType;
    }

    //Creating incident type
    public long createIncidentType(IncidentType incidenttype) {

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("IncidentTypeName", incidenttype.getIncidentTypeName());
        values.put("Description", incidenttype.getIncidentTypeDescription());

        // insert row
        long incidenttype_id = db.insert(Constants.TABLE_INCIDENTTYPE, null, values);

        return incidenttype_id;
    }

    //getting all incident types
    public List<IncidentType> getAllIncidentTypes() {

        List<IncidentType> incidenttypes = new ArrayList<>();
        String selectQuery = "SELECT Id, IncidentTypeName, Description FROM " + Constants.TABLE_INCIDENTTYPE;
        Log.e(LOG, selectQuery);

        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                IncidentType t = new IncidentType();
                t.setId(c.getInt((c.getColumnIndex("Id"))));
                t.setIncidentTypeName(c.getString(c.getColumnIndex("IncidentTypeName")));
                t.setIncidentTypeDescription(c.getString(c.getColumnIndex("Description")));

                // adding to incidents list
                incidenttypes.add(t);
            } while (c.moveToNext());
        }
        return incidenttypes;
    }

    //getting IncidentType count
    public int getIncidentTypeCount() {
        String countQuery = "SELECT Id FROM " + Constants.TABLE_INCIDENTTYPE;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    //Updating an incidenttype
    public int updateIncidentType(IncidentType incidenttype) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("IncidentTypeName", incidenttype.getIncidentTypeName());
        values.put("Description", incidenttype.getIncidentTypeDescription());

        // updating row
        return db.update(Constants.TABLE_INCIDENTTYPE, values, "Id" + " = ?", new String[] { String.valueOf(incidenttype.getId()) });
    }

    //Deleting a tag
    public void deleteIncidentType(IncidentType incidenttype, boolean should_delete_all_incidents_of_this_type) {
        db = dbHelper.getWritableDatabase();

        // before deleting an incident type
        // check if incidents under this type should also be deleted
        if (should_delete_all_incidents_of_this_type) {
            // get all incidents under this type
            List<Incident> allIncidentTypeIncidents = null; //getAllIncidentsByType(incidenttype.getId());

            // delete all incidents
            for (Incident incident : allIncidentTypeIncidents) {
                // delete incident
                IncidentDAO.deleteIncident(incident.getId());
            }
        }

        // now delete the tag
        db.delete(Constants.TABLE_INCIDENTTYPE, "Id" + " = ?", new String[] { String.valueOf(incidenttype.getId()) });
    }

    // </editor-fold>

}
