package de.csmath.QT;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static de.csmath.QT.VideoMetaStringMaker.*;

/**
 * This class provides the <i>main</i> function and the instance
 * function <i>perfrom</i> to either just list the meta data of
 * all h.264 video files of a directory, or to arrange them into
 * subfolders according to their horizontal and vertical definition
 * and their fps rate.
 */
public class QTVidArrange {

    /**
     * The program to gather the meta data from the QT files.
     */
    private final List<QTCommand> cmds = Arrays.asList(
        new QTCommand(QTOpCode.STEPIN, QTAtom.MOOV),
        new QTCommand(QTOpCode.READ, QTAtom.MVHD),
        new QTCommand(QTOpCode.STEPIN, QTAtom.TRAK),
        new QTCommand(QTOpCode.STEPIN, QTAtom.MDIA),
        new QTCommand(QTOpCode.STEPIN, QTAtom.MINF),
        new QTCommand(QTOpCode.STEPIN, QTAtom.STBL),
        new QTCommand(QTOpCode.READ, QTAtom.STSD));

    /**
     * A filter to capture only *.mov or *.MOV files.
     */
    private final DirectoryStream.Filter<Path> isMovie = path ->
            Pattern.compile(".*[Mm][Oo][Vv]$")
                    .matcher(path.getFileName().toString())
                    .matches();

    /**
     * Main program to execute QTVidArrange as a process.
     * It shows or arranges h.264 movie files.
     * @param args The array of arguments provided by the command line.
     */
    public static void main(String[] args) {

        String[] checkedArgs = null;

        try {
            checkedArgs = checkedArgs(args);
            String mode = checkedArgs[0];
            Path sourceDir = FileSystems.getDefault().getPath(checkedArgs[1]);
            Path targetDir = FileSystems.getDefault().getPath(checkedArgs[2]);
            new QTVidArrange().perform(mode, sourceDir, targetDir);
        } catch (Exception e) {
            usage();
            System.exit(1);
        }

    }

    /**
     * Shows or arranges h.264 video files according to its parameters.
     * @param mode "show" | "link" | "move" | "copy"
     * @param sourceDir The directory which contains the video files.
     * @param targetDir The target directory to which the files are transfered.
     * @throws IOException
     */
    public void perform(String mode, Path sourceDir, Path targetDir) throws IOException {
        try(DirectoryStream<Path> ds = Files.newDirectoryStream(sourceDir,isMovie)) {
            Consumer<Path> action = null;
            switch (mode) {
                case "show":
                    System.out.println(mkHeadline());
                    action = file -> show(file);
                    break;
                case "link":
                    FileAction link = linkAction();
                    action = file -> transFile(file,targetDir,link);
                    break;
                case "copy":
                    FileAction copy = (source,target) ->
                            Files.copy(source,target);
                    action = file -> transFile(file,targetDir,copy);
                    break;
                case "move":
                    FileAction move = (source, target) ->
                            Files.move(source,target);
                    action = file -> transFile(file,targetDir,move);
                    break;
                default:
                    break;
            }
            ds.forEach(action);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Returns a lambda to create symbolic links of given files.
     * @return a lambda to create symbolic links
     */
    private FileAction linkAction() {
        return (source,target) -> {
            Path s = source.toAbsolutePath().normalize();
            Path t = target.toAbsolutePath().normalize().getParent();
            Path linkPath = t.relativize(s);
            Files.createSymbolicLink(target,linkPath);
        };
    }

    /**
     * Prints the meta data of a video file to stdout.
     * @param file the video file
     */
    private void show(Path file) {
        QTReader reader = new QTReader();

        String fileName = file.getFileName().toString();
        try (InputStream fis = new FileInputStream(file.toFile())) {
            QTVideoMeta meta = readMeta(fileName, fis, reader);
            System.out.println(mkString(file.getFileName().toString(), meta));
        } catch (Exception e) {
            System.err.println("Skipped unsupported file: "
                    + file.getFileName());
        }
    }

    /**
     * The procedure to transfer (link, copy or move) a file to a
     * target directory according to its meta data.
     * @param file the video file
     * @param target the target directory
     * @param fa the lambda which performs the transfer
     */
    private void transFile(Path file, Path target, FileAction fa) {
        QTReader reader = new QTReader();

        String fileName = file.getFileName().toString();
        try (InputStream fis = new FileInputStream(file.toFile())) {
            QTVideoMeta meta = readMeta(fileName, fis, reader);
            Path dir = mkDirPath(target, meta);
            mkDir(dir);
            Path link = Paths.get(dir.toString(),meta.getFileName());
            fa.action(file,link);
            System.out.println(link + " created.");
        } catch (IOException ie) {
            System.err.println("IO-Error with file " + file);
        } catch (Exception e) {
            System.err.println("Skipped unsupported file: "
                    + file.getFileName());
        }
    }

    /**
     * Returns a directory path object with a subdirectory named
     * <i>h</i>x<i>v</i>@<i>f</i> where <i>h</i> is the horizontal
     * resulution, <i>v</i> the vertical resolution and <i>f</i> the
     * frames per second with which the video was shot.
     * @param target the target parent directory
     * @param meta the meta data of the video file
     * @return the target directory path
     */
    private Path mkDirPath(Path target, QTVideoMeta meta) {
        String dir = new StringBuilder()
                .append(meta.getWidth())
                .append("x")
                .append(meta.getHeigth())
                .append("@")
                .append(Math.round(meta.getFps()))
                .toString();
        return Paths.get(target.toString(), dir);
    }

    /**
     * Creates a directory on the file system if it doesn't exist yet.
     * @param dir the directory path
     * @throws IOException
     */
    private void mkDir(Path dir) throws IOException {
        if (!Files.isDirectory(dir))
            Files.createDirectory(dir);
    }

    /**
     * Returns the meta data of the given video file by using a QTReader.
     * @param fileName the name of the video file
     * @param is the input stream of the file
     * @param reader the QTReader which traverses the atoms of the file
     * @return the meta data of the video file
     * @throws IOException
     */
    private QTVideoMeta readMeta(String fileName, InputStream is, QTReader reader) throws IOException {
        return QTVideoMeta.newFromAtoms(fileName, reader.readStream(is, cmds));
    }

    /**
     * Prints the usage information to stderr.
     */
    private static void usage() {
        System.err.println(
                "Usage: java QTVidArrange show [dir]\n"
        +       "       java QTVidArrange link [source_dir [target_dir]]\n"
        +       "       java QTVidArrange move [source_dir [target_dir]]\n"
        +       "       java QTVidArrange copy [source_dir [target_dir]]");
    }

    /**
     * Checks the given arguments and returns the appropriate
     * arguments, needed to show or arrange the video files.
     * @param args the arguments to check
     * @return the appropriate arguments
     * @throws IllegalArgumentException
     */
    static String[] checkedArgs(String[] args) throws IllegalArgumentException {
        if (args.length == 0 || args.length > 3)
            throw new IllegalArgumentException("usage");
        String[] cas = new String[3];
        switch (args[0]) {
            case "show":
                if (args.length == 3)
                    throw new IllegalArgumentException("usage");
                cas[0] = "show";
                break;
            case "copy":
            case "link":
            case "move":
                cas[0] = args[0];
                break;
            default:
                throw new IllegalArgumentException("usage");
        }
        if (args.length > 1)
            cas[1] = args[1];
        else
            cas[1] = ".";
        if (args.length == 3)
            cas[2] = args[2];
        else
            cas[2] = cas[1];
        return cas;
    }

    /**
     * The functional interface to create the appropriate
     * action to show or transfer files.
     */
    private interface FileAction {
        void action(Path source, Path target) throws IOException;
    }
}
