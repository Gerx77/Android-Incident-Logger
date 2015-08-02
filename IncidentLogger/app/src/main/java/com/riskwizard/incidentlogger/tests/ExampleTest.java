package com.riskwizard.incidentlogger.tests;

import android.test.InstrumentationTestCase;

/**
 * Created by mattg on 6/02/2015.
 */
public class ExampleTest extends InstrumentationTestCase {

    public void test() throws Exception {
        final int expected = 5;
        final int reality = 5;
        assertEquals(expected, reality);
    }

    /*
    public void test2() throws Exception {
        final int expected = 4;
        final int reality = 1;
        assertEquals(expected, reality);
    }

    public void test_CreateIncident() throws Exception {
        final int expected = 4;
        final int reality = 1;
        assertEquals(expected, reality);
    }
    */

}
