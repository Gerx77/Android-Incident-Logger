package com.riskwizard.incidentlogger;

/**
 * Created by mattg on 11/03/2015.
 */
public final class Constants {

    // Database Version
    public static final int DATABASE_VERSION = 7;

    // Database Name
    public static final String DATABASE_NAME = "RiskWizard";

    // Image Directory Name
    public static final String IMAGE_DIR_NAME = "RiskWizard_IncidentLogger";

    // Table Names
    public static final String TABLE_INCIDENT = "Incidents";
    public static final String TABLE_INCIDENTTYPE = "IncidentTypes";
    public static final String TABLE_CONTACT = "Contacts";
    public static final String TABLE_INCIDENT_NOTIFY_CONTACT = "IncidentNotifyContacts";
    public static final String TABLE_ATTACHMENT = "Attachments";
    public static final String TABLE_INCIDENT_ATTACHMENT = "IncidentAttachments";

    // Table Create Statements
    public static final String CREATE_TABLE_INCIDENTS = "CREATE TABLE Incidents (Id INTEGER PRIMARY KEY, IncidentTypeId INTEGER, CreatedByContactId INTEGER, IncidentDate DATETIME, Description TEXT, InitialAction TEXT, Reportable Bit)";
    public static final String CREATE_TABLE_INCIDENTTYPES = "CREATE TABLE IncidentTypes (Id INTEGER PRIMARY KEY, IncidentTypeName TEXT, Description TEXT)";
    public static final String CREATE_TABLE_CONTACTS = "CREATE TABLE Contacts (Id INTEGER PRIMARY KEY, LastName TEXT, FirstName TEXT, PositionTitle TEXT, Company TEXT, EmailAddress TEXT)";
    public static final String CREATE_TABLE_INCIDENT_NOTIFY_CONTACTS = "CREATE TABLE IncidentNotifyContacts (Id INTEGER PRIMARY KEY, Incident_Id INTEGER, Contact_Id INTEGER)";
    public static final String CREATE_TABLE_ATTACHMENTS  = "CREATE TABLE Attachments (Id INTEGER PRIMARY KEY, FileName TEXT, Description TEXT, AttachedBy INTEGER, AttachedDate DATETIME, Type TEXT, Size TEXT, PhysicalFileName TEXT)";
    public static final String CREATE_TABLE_INCIDENT_ATTACHMENTS = "CREATE TABLE IncidentAttachments (Id INTEGER PRIMARY KEY, Incident_Id INTEGER, Attachment_Id INTEGER)";

}
