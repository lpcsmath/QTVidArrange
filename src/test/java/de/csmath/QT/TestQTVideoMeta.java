package de.csmath.QT;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Unit test for QTVideoMeta.
 */
public class TestQTVideoMeta extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestQTVideoMeta( String testName )
    {
        super( testName );
    }


    public void testInsufficientInfo() {
        try {
            QTVideoMeta.newFromAtoms("test", new ArrayList<QTAtom>());
            assertEquals(false,true);
        } catch (Exception e) {
            assertEquals(true, e instanceof IllegalArgumentException);
            assertEquals("not enough information", e.getMessage());
        }
    }
}
