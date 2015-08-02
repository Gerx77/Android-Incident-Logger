package com.riskwizard.incidentlogger.helper;

import com.riskwizard.incidentlogger.Constants;
import com.riskwizard.incidentlogger.model.Contact;
import com.riskwizard.incidentlogger.model.Incident;
import com.riskwizard.incidentlogger.model.IncidentType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mattg on 5/02/2015.
 */

/*
http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
*/

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    public static DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public static DatabaseHelper getInstanceForUnitTests(Context context) {
        sInstance = new DatabaseHelper(context);
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        try {
            db.execSQL(Constants.CREATE_TABLE_INCIDENTS);
            db.execSQL(Constants.CREATE_TABLE_INCIDENTTYPES);
            db.execSQL(Constants.CREATE_TABLE_CONTACTS);
            db.execSQL(Constants.CREATE_TABLE_INCIDENT_NOTIFY_CONTACTS);
            db.execSQL(Constants.CREATE_TABLE_ATTACHMENTS);
            db.execSQL(Constants.CREATE_TABLE_INCIDENT_ATTACHMENTS);

            //SeedDatabase.Seed();
            //SeedDatabase();
        }
        catch (Exception e) {
            //TODO Bug: This happens on every launch that isn't the first one.
            Log.w("IncidentLogger", "Error while creating db: " + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_INCIDENT);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_INCIDENTTYPE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_INCIDENT_NOTIFY_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_ATTACHMENT);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_INCIDENT_ATTACHMENT);

        // create new tables
        onCreate(db);
    }

	//TODO Remove the rawQuery statements = BAD!!

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public void SeedDatabase() {

        //TODO add progress bar to the activity that has called this method

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ Constants.TABLE_CONTACT);
        db.execSQL("delete from "+ Constants.TABLE_INCIDENTTYPE);
        db.execSQL("delete from "+ Constants.TABLE_INCIDENT);
        db.execSQL("delete from "+ Constants.TABLE_INCIDENT_NOTIFY_CONTACT);
        db.execSQL("delete from "+ Constants.TABLE_INCIDENT_ATTACHMENT);

        String INSERT_TABLE_CONTACT = "INSERT INTO " + Constants.TABLE_CONTACT + "(LastName, FirstName, PositionTitle, Company, EmailAddress) VALUES " +
            "('ADMINISTRATOR', 'Admin', 'System Administrator', 'Risk Wizard', 'admin@riskwizard.com'), " +
            "('Acer', 'Aidan', '', '', ''), " +
            "('Adlam', 'Alice', '', '', ''), " +
            "('Biere', 'Bridie', '', '', ''), " +
            "('Clanger', 'Ciara', '', '', ''), " +
            "('Nugent', 'Nicole', '', '', ''), " +
            "('Locke', 'Lewis', '', '', ''), " +
            "('Drake', 'Daniel', '', '', ''), " +
            "('Blunt', 'Blake', '', '', ''), " +
            "('Ottens', 'Olivia', '', '', ''), " +
            "('Anonymous', 'System', '', '', ''), " +
            "('Scheduler', 'System', '', '', ''), " +
            "('Gercovitch', 'Matt', 'Software Developer', 'Risk Wizard', 'mattg@riskwizard.com');";

        Log.d("SeedDatabase", INSERT_TABLE_CONTACT);
        db.execSQL(INSERT_TABLE_CONTACT);

        String INSERT_TABLE_INCIDENTTYPE = "INSERT INTO " + Constants.TABLE_INCIDENTTYPE + "(IncidentTypeName, Description) VALUES " +
                "('Injury (First Aid applied)', 'Personal injury resulting in First Aid treatment'), " +
                "('Injury (Onsite medical attention)', 'Personal injury requiring attention from qualified medical practitioner.'), " +
                "('Injury (Hospitalisation)', 'Personal injury requiring visit/period of stay at hospital'), " +
                "('Theft (Stock and/or cash)', ''), " +
                "('Fraud (Credit card)', 'Credit card fraud perpetrated through identity theft'), " +
                "('Loss of data (Customer payments)', 'Payments related to customers cannot be found/reconciled'), " +
                "('Breach of Privacy (Staff/Customer)', 'Breach of staff/customer confidentiality'), " +
                "('Complaint (Quality of goods)', 'Complaint (Quality of goods)'), " +
                "('Fire (Office/warehouse)', 'Fire at premises'), " +
                "('Flood (Retail premises)', 'Flood at retail premises'), " +
                "('False alarm (Fire alarm malfunction)', 'Fire emergency alarm malfunction'), " +
                "('Accident (Vehicle)', 'Damage to car fleet'), " +
                "('Damage (Office property)', 'Damage to premises/property'), " +
                "('Misuse of property (Laptops/phones)', 'Laptops/phones contain unauthorised material'), " +
                "('Work stoppage (Union sanctioned)', 'Union sanctioned action.'), " +
                "('Unauthorised entry (Office)', 'Unauthorised person(s) enter buildings'), " +
                "('System failure (IT system)', 'Failure of IT system to operate as specified'), " +
                "('Infringement of patent (Product design)', 'Infringed upon another company''s patent rights'), " +
                "('Network outage (Corporate Intranet)', 'IT network/Intranet failure'), " +
                "('Unapproved purchase >$10,000', '$10k Spending limits exceeded without authority'), " +
                "('Unauthorised access', '');";

        Log.d("SeedDatabase", INSERT_TABLE_INCIDENTTYPE);
        db.execSQL(INSERT_TABLE_INCIDENTTYPE);

        String INSERT_TABLE_INCIDENT = "INSERT INTO " + Constants.TABLE_INCIDENT + "(IncidentTypeId, CreatedByContactId, IncidentDate, Description, InitialAction, Reportable) VALUES " +
                "(1, 1, '" + DateTimeHelper.formatToIS08601(new Date()) + "', 'Description Text', 'Initial Action Text', 1), " +
                "(1, 3, '" + DateTimeHelper.formatToIS08601(new Date()) + "', 'Blake Blunt was involved in a car accident, resulting a  write-off of the vehicle.  No injuries.  But charged with driving under the influence of alcohol with a reading of 2.5%.  Loss of licence pending.  Accident occurred on the way home from a New Years Eve party which Marketing were running for number of Platinum Accounts.', 'Insurance policies are being revamped for cases like drink driving to implement strict rules for reimbursements.', 0), " +
                "(2, 7, '" + DateTimeHelper.formatToIS08601(new Date()) + "', 'Marketing sent out an introductory letter to new account holders introducing the range of products and services offered by the firm.  Unfortunately in the letter, a mail merge error printed out all the name and account details of all other new accounts for the month.', 'Letter of apology sent out within 24 hours of the original mailout.  Each member was also called by their account manager with an unreserved apology.  Internal Audit requested to review mailout procedures to ensure there is no repeat of this incident.', 0), " +
                "(3, 4, '" + DateTimeHelper.formatToIS08601(new Date()) + "', 'Sales assistant at Sydney retail branch was found to be falsely processing product returns by taking the labels off the stock at time of purchase then re-processing them as a return the next day and getting the cash for the return.', 'Sales assistant has been fined and terminated. Strict monitoring standards have been implement to avoid such fraud in the future', 0), " +
                "(4, 9, '" + DateTimeHelper.formatToIS08601(new Date()) + "', 'Fire started in the server room when one of the operators dropped a cigarette butt in the shredded paper bin', '', 1), " +
                "(5, 7, '" + DateTimeHelper.formatToIS08601(new Date()) + "', 'Hacker got into the HR system and extracted contact and banking details of 15 people, including 3 Executives.  Police have been notified of the breach.', 'Firewall security procedures revamped', 0), " +
                "(6, 8, '" + DateTimeHelper.formatToIS08601(new Date()) + "', 'Customer complained the product purchased was faulty.  On close inspection, it appears to be a manufacturing process failure.', '', 1), " +
                "(4, 1, '" + DateTimeHelper.formatToIS08601(new Date()) + "', 'Description Text', 'Initial Action Text', 1);";


        Log.d("SeedDatabase", INSERT_TABLE_INCIDENT);
        db.execSQL(INSERT_TABLE_INCIDENT);

        String INSERT_TABLE_INCIDENT_NOTIFY_CONTACT = "INSERT INTO " + Constants.TABLE_INCIDENT_NOTIFY_CONTACT + "(Incident_Id, Contact_Id) VALUES " +
                "(2, 1), " +
                "(2, 2);";


        Log.d("SeedDatabase", INSERT_TABLE_INCIDENT);
        db.execSQL(INSERT_TABLE_INCIDENT_NOTIFY_CONTACT);

        /*
        !IMPORTANT IF SLOW TRY THIS!
        By default, each INSERT statement is its own transaction. But if you surround multiple INSERT statements with BEGIN...COMMIT then all the inserts are grouped into a single transaction. The time needed to commit the transaction is amortized over all the enclosed insert statements and so the time per insert statement is greatly reduced.
        */

        closeDB();
    }

    public void SeedDatabaseForUnitTests() {

        /* Changing stuff in here could break unit tests. */

        SQLiteDatabase db = this.getWritableDatabase();

        String INSERT_TABLE_CONTACT = "INSERT INTO " + Constants.TABLE_CONTACT + "(LastName, FirstName, PositionTitle, Company, EmailAddress) VALUES " +
                "('ADMINISTRATOR', 'Admin', 'System Administrator', 'Risk Wizard', 'admin@riskwizard.com'), " +
                "('Gercovitch', 'Matt', 'Software Developer', 'Risk Wizard', 'mattg@riskwizard.com');";

        Log.d("SeedDatabaseUnitTests", INSERT_TABLE_CONTACT);
        db.execSQL(INSERT_TABLE_CONTACT);

        String INSERT_TABLE_INCIDENTTYPE = "INSERT INTO " + Constants.TABLE_INCIDENTTYPE + "(IncidentTypeName, Description) VALUES " +
                "('Accident', 'Personal injury resulting in First Aid treatment'), " +
                "('Breach of Privacy', 'Personal injury requiring attention from qualified medical practitioner.'), " +
                "('Unauthorised access', '');";

        Log.d("SeedDatabaseUnitTests", INSERT_TABLE_INCIDENTTYPE);
        db.execSQL(INSERT_TABLE_INCIDENTTYPE);

        String INSERT_TABLE_INCIDENT = "INSERT INTO " + Constants.TABLE_INCIDENT + "(IncidentTypeId, CreatedByContactId, IncidentDate, Description, InitialAction, Reportable) VALUES " +
                "(1, 1, '" + DateTimeHelper.formatToIS08601(new Date()) + "', 'Description Text', 'Initial Action Text', 1), " +
                "(2, 2, '" + DateTimeHelper.formatToIS08601(new Date()) + "', 'Blake Blunt was involved in a car accident, resulting a  write-off of the vehicle.  No injuries.  But charged with driving under the influence of alcohol with a reading of 2.5%.  Loss of licence pending.  Accident occurred on the way home from a New Years Eve party which Marketing were running for number of Platinum Accounts.', 'Insurance policies are being revamped for cases like drink driving to implement strict rules for reimbursements.', 0), " +
                "(3, 2, '" + DateTimeHelper.formatToIS08601(new Date()) + "', 'Description Text', 'Initial Action Text', 1);";


        Log.d("SeedDatabaseUnitTests", INSERT_TABLE_INCIDENT);
        db.execSQL(INSERT_TABLE_INCIDENT);

        String INSERT_TABLE_ATTACHMENT = "INSERT INTO " + Constants.TABLE_ATTACHMENT + "(FileName, Description, AttachedBy, AttachedDate, Type, Size, PhysicalFileName) VALUES " +
                "('Text Document 1', 'Text File', 1, '" + DateTimeHelper.formatToIS08601(new Date()) + "', 'txt', '100kb', 'c:\\temp\\TextDocument1.txt'), " +
                "('Text Document 2', 'Text File', 1, '" + DateTimeHelper.formatToIS08601(new Date()) + "', 'txt', '150kb', 'c:\\temp\\TextDocument2.txt');";

        Log.d("SeedDatabaseUnitTests", INSERT_TABLE_ATTACHMENT);
        db.execSQL(INSERT_TABLE_ATTACHMENT);

        String INSERT_TABLE_INCIDENT_ATTACHMENT = "INSERT INTO " + Constants.TABLE_INCIDENT_ATTACHMENT + "(Incident_Id, Attachment_Id) VALUES " +
                "(2, 1), " +
                "(2, 2);";

        Log.d("SeedDatabaseUnitTests", INSERT_TABLE_INCIDENT_ATTACHMENT);
        db.execSQL(INSERT_TABLE_INCIDENT_ATTACHMENT);

        String INSERT_TABLE_INCIDENT_NOTIFY_CONTACT = "INSERT INTO " + Constants.TABLE_INCIDENT_NOTIFY_CONTACT + "(Incident_Id, Contact_Id) VALUES " +
                "(2, 1), " +
                "(2, 2);";

        Log.d("SeedDatabaseUnitTests", INSERT_TABLE_INCIDENT_NOTIFY_CONTACT);
        db.execSQL(INSERT_TABLE_INCIDENT_NOTIFY_CONTACT);

        closeDB();
    }

}
