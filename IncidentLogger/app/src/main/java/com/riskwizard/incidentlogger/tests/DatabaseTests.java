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

import com.riskwizard.incidentlogger.dao.IncidentDAO;
import com.riskwizard.incidentlogger.dao.IncidentTypeDAO;
import com.riskwizard.incidentlogger.helper.DatabaseHelper;
import com.riskwizard.incidentlogger.model.Contact;
import com.riskwizard.incidentlogger.model.IncidentType;
import android.test.mock.MockContext;

import junit.framework.TestCase;

/**
 * Created by mattg on 5/02/2015.
 */
public class DatabaseTests extends AndroidTestCase {

    private IncidentDAO incident_dao;
    private IncidentTypeDAO incidentType_dao;

    public void setUp(){
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");

        // Create DAO object
        incident_dao = new IncidentDAO(context);
        incidentType_dao = new IncidentTypeDAO(context);
    }

    public void test_AddData() throws Exception {

        assertEquals(1, 1);
    }

    public void tearDown() throws Exception{
        //db.close();
        //incident_dao.close();
        //incidentType_dao.close();
        super.tearDown();
    }

}