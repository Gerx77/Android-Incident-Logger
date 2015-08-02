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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by mattg on 25/02/2015.
 */
public class AttachmentDAO {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private ContactDAO contact_dao;
    //private IncidentDAO incident_dao;

    // Logcat tag
    private static final String LOG = AttachmentDAO.class.getName();

    public AttachmentDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);

        contact_dao = new ContactDAO(context);
    }

    // Close the db
    public void close() {
        db.close();
    }

    // <editor-fold defaultstate="collapsed" desc="Attachment Table Methods">

    public long createAttachment(Attachment anAttachment) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("FileName", anAttachment.getFilename());
        values.put("Description", anAttachment.getDescription());
        values.put("AttachedBy", anAttachment.getAttachedBy().getId());
        values.put("AttachedDate", DateTimeHelper.formatToIS08601(anAttachment.getAttachedDate()));
        values.put("Type", anAttachment.getType());
        values.put("Size", anAttachment.getSize());
        values.put("PhysicalFileName", anAttachment.getPhysicalFilename());

        // Insert into DB
        return db.insert(Constants.TABLE_ATTACHMENT, null, values);
    }

    // Getting single attachment
    public Attachment getAttachment(int id) {
        Attachment anAttachment = new Attachment();

        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Constants.TABLE_ATTACHMENT + " WHERE Id = " + id;

        Log.e(LOG, selectQuery);

        //Cursor c = db.rawQuery(selectQuery, null);
        Cursor c = db.query(Constants.TABLE_ATTACHMENT, // a. table
                new String[] {"Id", "FileName", "Description", "AttachedBy", "AttachedDate", "Type", "Size", "PhysicalFileName"}, // b. column names
                " Id = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);

        if (c.moveToFirst()) {
            anAttachment.setId(c.getInt(c.getColumnIndex("Id")));
            anAttachment.setFilename((c.getString(c.getColumnIndex("FileName"))));
            anAttachment.setDescription((c.getString(c.getColumnIndex("Description"))));
            anAttachment.setAttachedBy(contact_dao.getContact(c.getColumnIndex("AttachedBy")));
            anAttachment.setAttachedDate(DateTimeHelper.parseFromISO8601(c.getString(c.getColumnIndex("AttachedDate"))));
            anAttachment.setType((c.getString(c.getColumnIndex("Type"))));
            anAttachment.setSize((c.getString(c.getColumnIndex("Size"))));
            anAttachment.setPhysicalFilename((c.getString(c.getColumnIndex("PhysicalFileName"))));
        }
        c.close();

        return anAttachment;
    }

    // Updating single attachment
    public void updateAttachment(Attachment anAttachment) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("FileName", anAttachment.getFilename());
        values.put("Description", anAttachment.getDescription());
        values.put("AttachedBy", anAttachment.getAttachedBy().getId());
        values.put("AttachedDate", DateTimeHelper.formatToIS08601(anAttachment.getAttachedDate()));
        values.put("Type", anAttachment.getType());
        values.put("Size", anAttachment.getSize());
        values.put("PhysicalFileName", anAttachment.getPhysicalFilename());

        // updating row
        db.update(Constants.TABLE_ATTACHMENT, values, "Id=?", new String[] { String.valueOf(anAttachment.getId()) });
    }

    // Deleting single attachment
    public void deleteSingleAttachment(Attachment anAttachment) {
        db = dbHelper.getWritableDatabase();
        db.delete(Constants.TABLE_ATTACHMENT, "Id" + " = ?", new String[] { String.valueOf(anAttachment.getId()) });
    }

    // </editor-fold>
}
