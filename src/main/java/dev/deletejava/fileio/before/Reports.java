package dev.deletejava.fileio.before;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/** Everyday file and stream helpers from Commons IO. */
public final class Reports {

    // Reads the entire file as a UTF-8 string.
    public static String read(Path file) throws IOException {
        return FileUtils.readFileToString(file.toFile(), StandardCharsets.UTF_8);
    }

    // Creates parent directories and writes the content to a file.
    public static void write(Path file, String content) throws IOException {
        FileUtils.writeStringToFile(file.toFile(), content, StandardCharsets.UTF_8); // creates missing parent dirs
    }

    // Reads the file and returns only its non-blank lines.
    public static List<String> nonBlankLines(Path file) throws IOException {
        return FileUtils.readLines(file.toFile(), StandardCharsets.UTF_8).stream()
                .filter(line -> !line.isBlank())
                .toList();
    }

    // Checks whether two files contain the same text.
    public static boolean sameContent(Path a, Path b) throws IOException {
        return FileUtils.contentEquals(a.toFile(), b.toFile());
    }

    // Reads an input stream fully and decodes it as UTF-8.
    public static String toString(InputStream in) throws IOException {
        return IOUtils.toString(in, StandardCharsets.UTF_8);
    }

    // Copies all bytes from one stream to another and returns the byte count.
    public static long copy(InputStream in, OutputStream out) throws IOException {
        return IOUtils.copyLarge(in, out);
    }

    private Reports() {}
}
