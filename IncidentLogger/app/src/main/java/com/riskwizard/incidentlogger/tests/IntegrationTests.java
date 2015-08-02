package com.riskwizard.incidentlogger.tests;

import android.app.Application;
import android.content.Context;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;
import android.test.InstrumentationTestCase;
import android.test.IsolatedContext;
import android.test.ProviderTestCase2;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.riskwizard.incidentlogger.dao.AttachmentDAO;
import com.riskwizard.incidentlogger.dao.ContactDAO;
import com.riskwizard.incidentlogger.dao.IncidentDAO;
import com.riskwizard.incidentlogger.dao.IncidentTypeDAO;
import com.riskwizard.incidentlogger.helper.DatabaseHelper;
import com.riskwizard.incidentlogger.model.Attachment;
import com.riskwizard.incidentlogger.model.Contact;
import com.riskwizard.incidentlogger.model.Incident;
import com.riskwizard.incidentlogger.model.IncidentType;
import android.test.mock.MockContext;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Date;
import java.util.List;

/**
 * Created by mattg on 4/03/2015.
 */
public class IntegrationTests extends AndroidTestCase {

    private DatabaseHelper dbHelper;
    private AttachmentDAO attachment_dao;
    private ContactDAO contact_dao;
    private IncidentDAO incident_dao;
    private IncidentTypeDAO incidentType_dao;

    public void setUp(){
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");

        // Create DAO object
        attachment_dao = new AttachmentDAO(context);
        contact_dao = new ContactDAO(context);
        incident_dao = new IncidentDAO(context);
        incidentType_dao = new IncidentTypeDAO(context);

        dbHelper = DatabaseHelper.getInstanceForUnitTests(context);
        dbHelper.SeedDatabaseForUnitTests();
    }

    // <editor-fold defaultstate="collapsed" desc="Attachment Tests">

    public void test_Attachment_CreateAttachment() throws Exception {
        final String testVal = "Document1.txt";

        Attachment attach1 =  new Attachment(testVal);
        attach1.setAttachedBy(new Contact("Smith", "John"));
        attach1.setAttachedDate(new Date());
        long attachment1_id = attachment_dao.createAttachment(attach1);

        assertEquals(testVal, attachment_dao.getAttachment((int)attachment1_id).getPhysicalFilename());
    }

    public void test_Attachment_GetSingleAttachment() throws Exception {
        final long attachmentid = 1;

        Attachment anAttachment = attachment_dao.getAttachment((int) attachmentid);
        assertEquals(attachmentid, anAttachment.getId());
    }

    public void test_Attachment_GetAllAttachmentsForIncident() throws Exception {
        final int expectedCount = 2;
        Incident anIncident = incident_dao.getIncident(2);

        List<Attachment> attachments = incident_dao.getAllAttachmentsForIncident(anIncident.getId());
        assertNotNull(attachments);

        assertEquals(expectedCount, attachments.size());
    }

    public void test_Attachment_GetAttachmentsCountForIncident() throws Exception {
        final int expectedCount = 2;
        //assertEquals(expected, incident_dao.getAllIncidents().size());
        assertEquals(expectedCount, incident_dao.getAttachmentsCountForIncident(incident_dao.getIncident(2).getId()));

    }

    public void test_Attachment_UpdateSingleAttachment() throws Exception {
        final String newVal = "Attachment new random XYZ";

        Attachment anAttachment = attachment_dao.getAttachment(1);
        anAttachment.setFilename(newVal);
        attachment_dao.updateAttachment(anAttachment);

        assertEquals(newVal, attachment_dao.getAttachment(1).getFilename());
    }

    public void test_Attachment_DeleteSingleAttachment() throws Exception {
        attachment_dao.deleteSingleAttachment(attachment_dao.getAttachment(1));
        assertEquals(0, attachment_dao.getAttachment(1).getId());
    }

    public void test_Attachment_DeleteAllAttachmentsFromIncident() throws Exception {
        incident_dao.deleteAllAttachmentsForIncident(incident_dao.getIncident(2));
        assertEquals(0, incident_dao.getIncident(2).getAttachments().size());
    }

    public void test_Attachment_AddAttachmentToIncident() throws Exception {
        final int expectedCount = 1;
        Attachment anAttachment = attachment_dao.getAttachment(1);
        Incident anIncident = incident_dao.getIncident(1);

        incident_dao.deleteAllAttachmentsForIncident(anIncident);
        incident_dao.addAttachmentToIncident(anAttachment, anIncident);
        assertEquals(expectedCount, anIncident.getAttachments().size());
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Contact Tests">

    public void test_Contact_CreateContact() throws Exception {
        final String testVal = "New Contact XYZ";

        Contact contact1 =  new Contact(testVal, "Jo");
        long contact1_id = contact_dao.createContact(contact1);

        assertEquals(testVal, contact_dao.getContact((int)contact1_id).getLastName());
    }

    public void test_Contact_GetContact() throws Exception {
        final long contactid = 1;

        Contact aContact = contact_dao.getContact((int) contactid);
        assertEquals(contactid, aContact.getId());
    }

    public void test_Contact_GetContactByName() throws Exception {
        final long contactid = 1;

        Contact aContact = contact_dao.getContactByName("Admin", "ADMINISTRATOR");
        assertEquals(contactid, aContact.getId());
    }

    public void test_Contact_GetAllContacts() throws Exception {
        List<Contact> contacts = contact_dao.getAllContacts();
        assertNotNull(contacts);
        assertNotSame(0, contacts.size());
    }

    public void test_Contact_GetContactsCount() throws Exception {
        final int expectedCount = 2;
        //assertEquals(expected, incident_dao.getAllIncidents().size());
        assertEquals(expectedCount, contact_dao.getContactCount());
    }

    public void test_Contact_UpdateContact() throws Exception {
        final String newVal = "Contact new random XYZ";

        Contact aContact = contact_dao.getContact(1);
        aContact.setLastName(newVal);
        contact_dao.updateContact(aContact);

        assertEquals(newVal, contact_dao.getContact(1).getLastName());
    }

    public void test_Contact_DeleteContact() throws Exception {
        contact_dao.deleteContact(contact_dao.getContact(1));
        assertEquals(0, contact_dao.getContact(1).getId());
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="IncidentType Tests">

    public void test_IncidentType_CreateIncidentType() throws Exception {
        final String testVal = "New Incident Type XYZ";

        IncidentType incidenttype1 = new IncidentType(testVal);
        long incidenttype1_id = incidentType_dao.createIncidentType(incidenttype1);

        assertEquals(testVal, incidentType_dao.getIncidentType(incidenttype1_id).getIncidentTypeName());
    }

    public void test_IncidentType_GetIncidentTypeById() throws Exception {
        final long incidenttypeid = 1;

        IncidentType anIncidentType = incidentType_dao.getIncidentType(incidenttypeid);
        assertEquals(incidenttypeid, anIncidentType.getId());
    }

    public void test_IncidentType_GetIncidentTypeByName() throws Exception {
        final String incidentTypeName = "Accident";
        IncidentType anIncidentType = incidentType_dao.getIncidentType(incidentTypeName);
        assertEquals(incidentTypeName, anIncidentType.getIncidentTypeName());
    }

    public void test_IncidentType_GetAllIncidentTypes() throws Exception {
        List<IncidentType> incident_types = incidentType_dao.getAllIncidentTypes();
        assertNotNull(incident_types);
        assertNotSame(0, incident_types.size());
    }

    public void test_IncidentType_GetIncidentTypeCount() throws Exception {
        final int expectedCount = 3;
        //assertEquals(expected, incidentType_dao.getAllIncidentTypes().size());
        assertEquals(expectedCount, incidentType_dao.getIncidentTypeCount());
    }

    public void test_IncidentType_UpdateIncidentType() throws Exception {
        final String newVal = "Incident type new random name XYZ";

        IncidentType anIncidentType = incidentType_dao.getIncidentType(1);
        anIncidentType.setIncidentTypeName(newVal);
        incidentType_dao.updateIncidentType(anIncidentType);

        assertNotNull(incidentType_dao.getIncidentType(newVal));
    }

    public void test_IncidentType_DeleteIncidentType() throws Exception {

        incidentType_dao.deleteIncidentType(incidentType_dao.getIncidentType(1), false);
        assertEquals(0, incidentType_dao.getIncidentType(1).getId());
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Incident Tests">

    public void test_Incident_CreateIncident() throws Exception {
        final String testVal = "New Incident Type XYZ";

        Incident incident1 = new Incident(new IncidentType("New"), new Contact("Jo", "Blogs"));
        incident1.setIncidentDate(new Date());
        incident1.setDescription(testVal);
        long incident1_id = incident_dao.createIncident(incident1);

        assertEquals(testVal, incident_dao.getIncident(incident1_id).getDescription());
    }

    public void test_Incident_GetIncidentById() throws Exception {
        final long incidentid = 1;

        Incident anIncident = incident_dao.getIncident(incidentid);
        assertEquals(incidentid, anIncident.getId());
    }

    public void test_Incident_GetAllIncidents() throws Exception {

        List<Incident> incidents = incident_dao.getAllIncidents();
        assertNotNull(incidents);
        assertNotSame(0, incidents.size());

        //List<Incident> allIncidents = db.getAllIncidents();
        //for (Incident incident : allIncidents) {
        //    Log.d("Incident", incident.getId());
        //   }

    }

    public void test_Incident_GetAllIncidentsByIncidentType() throws Exception {
        final int expectedCount = 1;
        List<Incident> incidents = incident_dao.getAllIncidentsByIncidentType(incidentType_dao.getIncidentType(3));
        assertNotNull(incidents);
        assertEquals(expectedCount, incidents.size());

        // Getting incidents under a specific incident type name
        //Log.d("Incident", "Get incidents under single IncidentType name");

        //List<Incident> itList = db.getAllIncidentsByIncidentType(incidenttype2.getIncidentTypeName());
        //for (Incident incident : itList) {
        //    Log.d("Incident Type (Breach of Privacy) list", incident.getId());
        //    }
    }

    public void test_Incident_GetIncidentCount() throws Exception {
        final int expectedCount = 3;
        //assertEquals(expected, incident_dao.getAllIncidents().size());
        assertEquals(expectedCount, incident_dao.getIncidentCount());
    }

    public void test_Incident_UpdateIncident() throws Exception {
        final String newVal = "Incident new random description XYZ";

        Incident anIncident = incident_dao.getIncident(1);
        anIncident.setDescription(newVal);
        incident_dao.updateIncident(anIncident);

        assertEquals(newVal, incident_dao.getIncident(1).getDescription());
    }

    public void test_Incident_DeleteIncident() throws Exception {

        incident_dao.deleteIncident(incident_dao.getIncident(1).getId());
        assertEquals(0, incident_dao.getIncident(1).getId());

        // Deleting a Incident
        //Log.d("Delete Incident", "Deleting an Incident");
        //Log.d("Incident Count", "Incident Count Before Deleting: " + db.getIncidentCount());

        //db.deleteIncident(incident8_id);
    }

    public void test_Incident_AddNotifyContactToIncident() throws Exception {
        final int expectedCount = 1;
        Contact aContact = contact_dao.getContact(1);
        Incident anIncident = incident_dao.getIncident(1);

        incident_dao.addNotifyContactToIncident(anIncident, aContact);
        assertEquals(expectedCount, incident_dao.getIncident(1).getNotifyContacts().size());
    }

    public void test_Incident_GetAllNotifyContactForIncident() throws Exception {
        final int expectedCount = 2;
        Incident anIncident = incident_dao.getIncident(2);

        List<Contact> notifyContacts = incident_dao.getAllNotifyContactsForIncident(anIncident.getId());
        assertNotNull(notifyContacts);

        assertEquals(expectedCount, notifyContacts.size());
    }

    public void test_Incident_RemoveAllNotifyContactsFromIncident() throws Exception {
        final int expectedCount = 0;
        incident_dao.removeAllNotifyContactsFromIncident(incident_dao.getIncident(2).getId());
        assertEquals(expectedCount, incident_dao.getIncident(2).getNotifyContacts().size());
    }

    // </editor-fold>

    public void tearDown() throws Exception{
        dbHelper.closeDB();
        //attachment_dao.close();
        //contact_dao.close();
        //incident_dao.close();
        //incidentType_dao.close();
        super.tearDown();
    }

}
