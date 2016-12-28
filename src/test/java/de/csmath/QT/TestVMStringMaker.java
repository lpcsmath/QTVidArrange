package de.csmath.QT;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by lpfeiler on 11.12.2016.
 */
public class TestVMStringMaker extends TestCase {

    public void testAlignment() {
        QTVideoMeta meta = new QTVideoMeta("test", ZonedDateTime.now(), 120 * 24000, 24000, 1920, 1080);
        VideoMetaStringMaker sm = new VideoMetaStringMaker();
        String hl = sm.mkHeadline();
        String line = sm.mkString("ABC", meta);
        //System.out.println(hl);
        //System.out.println(line);
        alignment(hl,line);

    }

    private void alignment(String hl, String line) {
        //File is left aligned
        assertEquals(hl.indexOf("FILE"),line.indexOf("ABC"));
        //Duration is right aligned
        assertEquals(hl.indexOf("N  W"),line.indexOf("2:00") + 3);
        //Width is right aligned
        assertEquals(hl.indexOf("H  H"),line.indexOf("1920") + 3);
        //Height is right aligned
        assertEquals(hl.indexOf("T  F"),line.indexOf("1080") + 3);
        //FPS is right aligned
        //assertEquals(hl.length(),line.length());
    }
}
