package dev.deletejava.fileio.after;

import static java.util.function.Predicate.not;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/// The same helpers with `java.nio.file.Files` and `InputStream`. `Files` reads and writes UTF-8 unless told otherwise.
public final class Reports {

    // Reads the entire file as a UTF-8 string.
    public static String read(Path file) throws IOException {
        return Files.readString(file);                                     // Java 11
    }

    // Creates parent directories and writes the content to a file.
    public static void write(Path file, String content) throws IOException {
        Files.createDirectories(file.toAbsolutePath().getParent());       // Files.writeString doesn't do this for you
        Files.writeString(file, content);                                  // Java 11
    }

    // Reads the file and returns only its non-blank lines.
    public static List<String> nonBlankLines(Path file) throws IOException {
        return Files.readAllLines(file).stream().filter(not(String::isBlank)).toList();
    }

    // Checks whether two files contain the same text.
    public static boolean sameContent(Path a, Path b) throws IOException {
        return Files.mismatch(a, b) == -1;                                 // Java 12
    }

    // Reads an input stream fully and decodes it as UTF-8.
    public static String toString(InputStream in) throws IOException {
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);      // Java 9
    }

    // Copies all bytes from one stream to another and returns the byte count.
    public static long copy(InputStream in, OutputStream out) throws IOException {
        return in.transferTo(out);                                         // Java 9
    }

    private Reports() {}
}
