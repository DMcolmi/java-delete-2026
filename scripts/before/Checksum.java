import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;

/** A tiny CLI tool still needs a class, a static main, String[] args and explicit imports. */
public class Checksum {
    // Calculates and prints the checksum for each input file.
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("usage: java Checksum.java <file>...");
            return;
        }
        for (String arg : args) {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(Path.of(arg)));
            System.out.println(HexFormat.of().formatHex(hash) + "  " + arg);
        }
    }
}
