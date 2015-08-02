package com.riskwizard.incidentlogger.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.riskwizard.incidentlogger.Constants;
import com.riskwizard.incidentlogger.helper.DatabaseHelper;
import com.riskwizard.incidentlogger.helper.DateTimeHelper;
import com.riskwizard.incidentlogger.model.Attachment;
import com.riskwizard.incidentlogger.model.Contact;
import com.riskwizard.incidentlogger.model.Incident;
import com.riskwizard.incidentlogger.model.IncidentType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by mattg on 18/02/2015.
 */
public class IncidentDAO {

    static private SQLiteDatabase db;
    static private DatabaseHelper dbHelper;
    private IncidentTypeDAO incidentType_dao;
    private ContactDAO contact_dao;
    private AttachmentDAO attachment_dao;

    // Logcat tag
    private static final String LOG = IncidentDAO.class.getName();

    public IncidentDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);

        incidentType_dao = new IncidentTypeDAO(context);
        contact_dao = new ContactDAO(context);
        attachment_dao = new AttachmentDAO(context);
    }

    // Close the db
    public void close() {
        db.close();
    }


    // <editor-fold defaultstate="collapsed" desc="Incident Table Methods">

    //*** Creating an Incident          
    public long createIncident(Incident incident) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("IncidentTypeId", incident.getIncidentType().getId());
        values.put("CreatedByContactId", incident.getCreatedBy().getId());
        values.put("IncidentDate", DateTimeHelper.formatToIS08601(incident.getIncidentDate()));
        values.put("Description", incident.getDescription());
        values.put("InitialAction", incident.getInitialAction());
        values.put("Reportable", incident.getReportable());

        // insert row
        long incident_id = db.insert(Constants.TABLE_INCIDENT, null, values);

        incident.setId((int)incident_id);

        if (incident.getNotifyContacts().size() > 0) {
            for(Contact c : incident.getNotifyContacts()) {
                addNotifyContactToIncident(incident, c);
            }
        }

        return incident_id;
    }

    //get single Incident
    public Incident getIncident(long incident_id) {
        Incident anIncident = new Incident();
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Constants.TABLE_INCIDENT + " WHERE Id = " + incident_id;

        Log.e(LOG, selectQuery);

        //Cursor c = db.rawQuery(selectQuery, null);
        Cursor c = db.query(Constants.TABLE_INCIDENT, // a. table
                new String[] {"Id", "IncidentTypeId", "CreatedByContactId", "IncidentDate", "Description", "InitialAction", "Reportable"}, // b. column names
                " Id = ?", // c. selections
                new String[] { String.valueOf(incident_id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);

        //if (c != null)
        //    c.moveToFirst();
        if (c.moveToFirst()) {
            anIncident.setId(c.getInt(c.getColumnIndex("Id")));
            anIncident.setIncidentType(incidentType_dao.getIncidentType(c.getInt(c.getColumnIndex("IncidentTypeId"))));
            anIncident.setCreatedBy(contact_dao.getContact(c.getColumnIndex("CreatedByContactId")));
            anIncident.setIncidentDate(DateTimeHelper.parseFromISO8601(c.getString(c.getColumnIndex("IncidentDate"))));
            anIncident.setDescription((c.getString(c.getColumnIndex("Description"))));
            anIncident.setInitialAction((c.getString(c.getColumnIndex("InitialAction"))));
            anIncident.setReportable(c.getInt(c.getColumnIndex("Reportable"))>0);
            anIncident.setNotifyContacts(getAllNotifyContactsForIncident(incident_id));
            anIncident.setAttachments(getAllAttachmentsForIncident(incident_id));
        }
        c.close();

        return anIncident;
    }

    //getting all Incidents
    public List<Incident> getAllIncidents() {
        List<Incident> incidents = new ArrayList<>();
        String selectQuery = "SELECT Id, IncidentTypeId, CreatedByContactId, IncidentDate, Description, InitialAction, Reportable FROM " + Constants.TABLE_INCIDENT;

        Log.e(LOG, selectQuery);

        db = dbHelper.getReadableDatabase();
        //Cursor c = db.rawQuery(selectQuery, null);

        Cursor c = db.query(Constants.TABLE_INCIDENT, // a. table
                new String[] {"Id", "IncidentTypeId", "CreatedByContactId", "IncidentDate", "Description", "InitialAction", "Reportable"}, // b. column names
                null, // c. selections
                null, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Incident anIncident = new Incident();
                anIncident.setId(c.getInt((c.getColumnIndex("Id"))));
                anIncident.setIncidentType(incidentType_dao.getIncidentType(c.getInt(c.getColumnIndex("IncidentTypeId"))));
                anIncident.setCreatedBy(contact_dao.getContact(c.getInt(c.getColumnIndex("CreatedByContactId"))));
                anIncident.setIncidentDate(DateTimeHelper.parseFromISO8601(c.getString(c.getColumnIndex("IncidentDate"))));
                anIncident.setDescription((c.getString(c.getColumnIndex("Description"))));
                anIncident.setInitialAction((c.getString(c.getColumnIndex("InitialAction"))));
                anIncident.setReportable(c.getInt(c.getColumnIndex("Reportable"))>0);
                //anIncident.setNotifyContacts();

                // adding to incident list
                incidents.add(anIncident);
            } while (c.moveToNext());
        }
        c.close();

        return incidents;
    }

    //getting all incidents under single incident type
    public List<Incident> getAllIncidentsByIncidentType(IncidentType incidentType) {
        List<Incident> incidents = new ArrayList<>();
        String selectQuery = "SELECT Id, IncidentTypeId, CreatedByContactId, IncidentDate, Description, InitialAction, Reportable FROM " + Constants.TABLE_INCIDENT;

        Log.e(LOG, selectQuery);

        db = dbHelper.getReadableDatabase();
        //Cursor c = db.rawQuery(selectQuery, null);

        Cursor c = db.query(Constants.TABLE_INCIDENT, // a. table
                new String[] {"Id", "IncidentTypeId", "CreatedByContactId", "IncidentDate", "Description", "InitialAction", "Reportable"}, // b. column names
                " IncidentTypeId = ?", // c. selections
                new String[] { String.valueOf(incidentType.getId()) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Incident anIncident = new Incident();
                anIncident.setId(c.getInt((c.getColumnIndex("Id"))));
                anIncident.setIncidentType(incidentType_dao.getIncidentType(c.getInt(c.getColumnIndex("IncidentTypeId"))));
                anIncident.setCreatedBy(contact_dao.getContact(c.getInt(c.getColumnIndex("CreatedByContactId"))));
                anIncident.setIncidentDate(DateTimeHelper.parseFromISO8601(c.getString(c.getColumnIndex("IncidentDate"))));
                anIncident.setDescription((c.getString(c.getColumnIndex("Description"))));
                anIncident.setInitialAction((c.getString(c.getColumnIndex("InitialAction"))));
                anIncident.setReportable(c.getInt(c.getColumnIndex("Reportable"))>0);
                //anIncident.setNotifyContacts();

                // adding to incident list
                incidents.add(anIncident);
            } while (c.moveToNext());
        }
        c.close();

        return incidents;
    }

    //getting Incident count
    public int getIncidentCount() {
        String countQuery = "SELECT Id FROM " + Constants.TABLE_INCIDENT;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    //Updating a Incident
    public void updateIncident(Incident incident) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("IncidentTypeId", incident.getIncidentType().getId());
        values.put("CreatedByContactId", incident.getCreatedBy().getId());
        values.put("IncidentDate", DateTimeHelper.formatToIS08601(incident.getIncidentDate()));
        values.put("Description", incident.getDescription());
        values.put("InitialAction", incident.getInitialAction());
        values.put("Reportable", incident.getReportable());

        // updating row
        db.update(Constants.TABLE_INCIDENT, values, "Id=?", new String[] { String.valueOf(incident.getId()) });

        removeAllNotifyContactsFromIncident(incident.getId());

        if (incident.getNotifyContacts().size() > 0) {
            for(Contact c : incident.getNotifyContacts()) {
                addNotifyContactToIncident(incident, c);
            }
        }

    }

    //Deleting a Incident
    static public void deleteIncident(long incident_id) {
        db = dbHelper.getWritableDatabase();
        db.delete(Constants.TABLE_INCIDENT, "Id" + " = ?", new String[] { String.valueOf(incident_id) });
    }

    //Add notify contact to incident
    public void addNotifyContactToIncident(Incident anIncident, Contact aContact) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Incident_Id", anIncident.getId());
        values.put("Contact_Id", aContact.getId());

        // updating row
        db.insert(Constants.TABLE_INCIDENT_NOTIFY_CONTACT, null, values);
    }

    //Get all notify contacts for incident
    public List<Contact> getAllNotifyContactsForIncident(long incident_id) {
        List<Contact> contacts = new ArrayList<>();
        String selectQuery = "SELECT Id, Incident_Id, Contact_Id FROM " + Constants.TABLE_INCIDENT_NOTIFY_CONTACT;

        Log.e(LOG, selectQuery);

        db = dbHelper.getReadableDatabase();
        //Cursor c = db.rawQuery(selectQuery, null);

        Cursor c = db.query(Constants.TABLE_INCIDENT_NOTIFY_CONTACT, // a. table
                new String[] {"Id", "Incident_Id", "Contact_Id"}, // b. column names
                " Incident_Id = ?", // c. selections
                new String[] { String.valueOf(incident_id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Contact aContact = contact_dao.getContact(c.getInt(c.getColumnIndex("Contact_Id")));

                // adding to incident list
                contacts.add(aContact);
            } while (c.moveToNext());
        }
        c.close();

        return contacts;
    }

    //Remove notify contact from incident
    public void removeAllNotifyContactsFromIncident(long incident_id) {
        db = dbHelper.getWritableDatabase();
        db.delete(Constants.TABLE_INCIDENT_NOTIFY_CONTACT, "Incident_Id" + " = ?", new String[] { String.valueOf(incident_id) });
    }

    //Get all attachments for incident
    public List<Attachment> getAllAttachmentsForIncident(long incident_id) {
        List<Attachment> attachments = new ArrayList<>();
        String selectQuery = "SELECT Id, Incident_Id, Attachment_Id FROM " + Constants.TABLE_INCIDENT_ATTACHMENT;

        Log.e(LOG, selectQuery);

        db = dbHelper.getReadableDatabase();
        //Cursor c = db.rawQuery(selectQuery, null);

        Cursor c = db.query(Constants.TABLE_INCIDENT_ATTACHMENT, // a. table
                new String[] {"Id", "Incident_Id", "Attachment_Id"}, // b. column names
                " Incident_Id = ?", // c. selections
                new String[] { String.valueOf(incident_id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Attachment anAttachment = attachment_dao.getAttachment(c.getInt(c.getColumnIndex("Attachment_Id")));

                // adding to incident list
                attachments.add(anAttachment);
            } while (c.moveToNext());
        }
        c.close();

        return attachments;
    }

    // Getting Attachments Count
    public int getAttachmentsCountForIncident(long incident_id) {
        String countQuery = "SELECT Id FROM " + Constants.TABLE_INCIDENT_ATTACHMENT + " WHERE Incident_Id=" + String.valueOf(incident_id);
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    // Deleting all attachments for an Incident
    public void deleteAllAttachmentsForIncident(Incident incident) {
        db = dbHelper.getWritableDatabase();

        if (incident.getAttachments().size() > 0) {
            for (Attachment anAttachment : incident.getAttachments()) {
                attachment_dao.deleteSingleAttachment(anAttachment);
            }
        }

        db.delete(Constants.TABLE_INCIDENT_ATTACHMENT, "Incident_Id" + " = ?", new String[] { String.valueOf(incident.getId()) });

    }

    // Adding new attachment to incident
    public void addAttachmentToIncident(Attachment attachment, Incident incident) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Incident_Id", incident.getId());
        values.put("Attachment_Id", attachment.getId());

        // updating row
        db.insert(Constants.TABLE_INCIDENT_ATTACHMENT, null, values);
    }

    // </editor-fold>

}
