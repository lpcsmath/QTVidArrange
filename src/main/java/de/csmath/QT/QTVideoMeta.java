package de.csmath.QT;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Iterator;


/**
 * This class represents meta data of QuickTime video files.
 */
public final class QTVideoMeta {

    /**
     * The name of the video file.
     */
    private String fileName;

    /**
     * Timestamp of creation time (UTC).
     */
    private ZonedDateTime creationDate;

    /**
     * Duration of the video in seconds.
     */
    private int duration;

    /**
     * Frames per second.
     */
    private double fps;

    /**
     * Horizontal resolution of the video.
     */
    private int width;

    /**
     * Vertical resolution of the video.
     */
    private int heigth;

    /**
     * Factory method to create a new QTVideoMeta object.
     * @param fileName the name of the file
     * @param atoms a collection of atoms which contain the meta data
     * @return a new QTVideoMeta object
     */
    public static QTVideoMeta newFromAtoms(String fileName, Collection<QTAtom> atoms) {
        ZonedDateTime cd = null;
        int du = 0, ts = 0;
        int w = 0, h = 0;
        boolean foundMvhd = false, foundVsd = false;

        for (QTAtom atom : atoms) {
            switch (atom.getType()) {
                case QTAtom.MVHD:
                    MvhdAtom ma = (MvhdAtom) atom;
                    foundMvhd = true;
                    cd = ma.getCreationTime();
                    du = ma.getDuration();
                    ts = ma.getTimeScale();
                    break;
                case QTAtom.STSD:
                    VideoSampleDescription vsd = getVsd((StsdAtom) atom);
                    if (vsd != null) {
                        foundVsd = true;
                        w = vsd.getWidth();
                        h = vsd.getHeight();
                    }
            }
        }
        if (!foundMvhd || !foundVsd)
            throw new IllegalArgumentException("not enough information");
        return new QTVideoMeta(fileName,cd,du,ts,w,h);
    }

    /**
     * Returns the VideoSampleDescription from an StsdAtom.
     * @param sa the StsdAtom
     * @return the video sample description
     */
    private static VideoSampleDescription getVsd(StsdAtom sa) {
        VideoSampleDescription vsd = null;
        Iterator<SampleDescription> it = sa.getSDIterator();
        while (it.hasNext()) {
            SampleDescription sd = it.next();
            if (sd instanceof VideoSampleDescription) {
                vsd = (VideoSampleDescription) sd;
                break;
            }
        }
        return vsd;
    }

    /**
     * Constructs a new QTVideoMeta object.
     * @param fileName the name of the file
     * @param creationDate the timestamp of the creation of the video (UTC)
     * @param duration the duration of the video in seconds
     * @param timeScale the time scale according to the QT file
     * @param width the horizontal resolution of the video
     * @param heigth the vertical resolution of the video
     */
    public QTVideoMeta(String fileName, ZonedDateTime creationDate, int duration,
                       int timeScale, int width, int heigth) {
        this.fileName = fileName;
        this.creationDate = creationDate;
        this.duration = duration/timeScale;
        this.fps = timeScale / 1000.0;
        this.width = width;
        this.heigth = heigth;
    }

    /**
     * Returns the name of the file.
     * @return the name of the file
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Returns the timestamp of creation (UTC).
     * @return the timestamp of creation (UTC)
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Returns the duration of the video in seconds.
     * @return the duration in seconds.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Returns the frames per second.
     * @return the frames per second
     */
    public double getFps() {
        return fps;
    }

    /**
     * Returns the horizontal resolution of the video.
     * @return the horizontal resolution
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the vertical resolution of the video.
     * @return the vertical resolution
     */
    public int getHeigth() {
        return heigth;
    }

}
