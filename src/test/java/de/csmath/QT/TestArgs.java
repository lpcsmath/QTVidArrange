package de.csmath.QT;

import junit.framework.TestCase;

import java.util.Arrays;

import static de.csmath.QT.QTVidArrange.*;

/**
 * Created by lpfeiler on 27.12.2016.
 */
public class TestArgs extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestArgs( String testName )
    {
        super( testName );
    }


    public void testCheckedArgsShow() {
        String[] args = {"show"};
        String[] res = checkedArgs(args);

        assertEquals(3, res.length);
        assertEquals("show", res[0]);
        assertEquals(".", res[1]);
        assertEquals(".", res[2]);

        args = Arrays.asList("show",".").toArray(new String[0]);
        res = checkedArgs(args);

        assertEquals(3, res.length);
        assertEquals("show", res[0]);
        assertEquals(".", res[1]);
        assertEquals(".", res[2]);

        args = Arrays.asList("show","source").toArray(new String[0]);
        res = checkedArgs(args);

        assertEquals(3, res.length);
        assertEquals("show", res[0]);
        assertEquals("source", res[1]);

    }

    public void testCheckedArgsLink() {
        String[] args = {"link"};
        String[] res = checkedArgs(args);

        assertEquals(3, res.length);
        assertEquals("link", res[0]);
        assertEquals(".", res[1]);
        assertEquals(".", res[2]);

        args = Arrays.asList("link",".").toArray(new String[0]);
        res = checkedArgs(args);

        assertEquals(3, res.length);
        assertEquals("link", res[0]);
        assertEquals(".", res[1]);
        assertEquals(".", res[2]);

        args = Arrays.asList("link","source","target").toArray(new String[0]);
        res = checkedArgs(args);

        assertEquals(3, res.length);
        assertEquals("link", res[0]);
        assertEquals("source", res[1]);
        assertEquals("target", res[2]);

    }
}
