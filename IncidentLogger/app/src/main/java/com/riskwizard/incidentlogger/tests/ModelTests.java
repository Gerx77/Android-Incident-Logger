package com.riskwizard.incidentlogger.tests;

import android.test.InstrumentationTestCase;

/**
 * Created by mattg on 5/02/2015.
 */
public class ModelTests extends InstrumentationTestCase {

    public void test() throws Exception {
        final int expected = 5;
        final int reality = 5;
        assertEquals(expected, reality);
    }

}
