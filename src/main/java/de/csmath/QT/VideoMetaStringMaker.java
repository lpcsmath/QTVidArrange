package de.csmath.QT;

import java.time.format.DateTimeFormatter;

/**
 * Provides methods to create formatted strings of meta data
 * of video files.
 */
public final class VideoMetaStringMaker {

    /**
     * Timestamp formatter for the creation time.
     */
    private static final DateTimeFormatter dtf =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Space between columns.
     */
    private static final String colSpace = "  ";

    /**
     * Column width of the duration of the video.
     */
    private static final int durationWidth = 8;

    /**
     * Column width of the horizontal resolution.
     */
    private static final int widthWidth = 5;

    /**
     * Column width of the vertical resolution.
     */
    private static final int heightWidth = 6;

    /**
     * Column width of the frames per second.
     */
    private static final int fpsWidth = 10;

    /**
     * Column width of the creation time.
     */
    private static final int cdWidth = 19;

    /**
     * Returns a formatted string of meta data
     * @param fileName the name of the file
     * @param meta the meta data object
     * @return a formatted string of meta data
     */
    public static String mkString(String fileName, QTVideoMeta meta) {
        String str;
        StringBuilder sb = new StringBuilder();
        str = String.format("%d:%02d", meta.getDuration()/60, meta.getDuration()%60);
        alignRight(sb, str, durationWidth);
        sb.append(colSpace);
        str = String.valueOf(meta.getWidth());
        alignRight(sb, str, widthWidth);
        sb.append(colSpace);
        str = String.valueOf(meta.getHeigth());
        alignRight(sb, str, heightWidth);
        sb.append(colSpace);
        str = String.format("%.02f", meta.getFps());
        alignRight(sb, str, fpsWidth);
        sb.append(colSpace);
        alignRight(sb, meta.getCreationDate().format(dtf), cdWidth);
        sb.append(colSpace);
        sb.append(fileName);
        return sb.toString();
    }

    /**
     * Appends a string to a given string builder by aligning it according to
     * the column width.
     * @param sb the string builder
     * @param str the string to append
     * @param width the column width
     */
    private static void alignLeft(StringBuilder sb, String str, int width) {
        sb.append(str);
        for (int i=0; i <  (width - str.length()); i++) {
            sb.append(" ");
        }
    }

    /**
     * Appends a string to a given string builder by aligning it according to
     * the column width.
     * @param sb the string builder
     * @param str the string to append
     * @param width the column width
     */
    private static void alignRight(StringBuilder sb, String str, int width) {
        for (int i=0; i <  (width - str.length()); i++) {
            sb.append(" ");
        }
        sb.append(str);
    }

    /**
     * Returns a headline that fits to the formatted strings.
     * @return a headline string
     */
    public static String mkHeadline() {
        String fn = "FILE", d="DURATION", w="WIDTH", h="HEIGHT",
                fps="FRAMESPSEC", cd="CREADATE";
        StringBuilder sb = new StringBuilder();
        alignLeft(sb,d,durationWidth);
        sb.append(colSpace);
        alignLeft(sb,w,widthWidth);
        sb.append(colSpace);
        alignLeft(sb,h,heightWidth);
        sb.append(colSpace);
        alignLeft(sb,fps,fpsWidth);
        sb.append(colSpace);
        alignLeft(sb,cd,cdWidth);
        sb.append(colSpace);
        sb.append(fn);
        return sb.toString();
    }

}
