package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import dev.deletejava.fileio.after.Reports;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileIoTest {

    @TempDir
    Path dir;

    private final String report = "Città: Milano\n\ntotal: 42 €\n";

    @Test
    // Compares file-reading and writing results between implementations.
    void sameResults() throws Exception {
        Path before = dir.resolve("before/2026/report.txt");
        Path after = dir.resolve("after/2026/report.txt");
        dev.deletejava.fileio.before.Reports.write(before, report);
        Reports.write(after, report);

        assertEquals(dev.deletejava.fileio.before.Reports.read(before), Reports.read(after));
        assertEquals(dev.deletejava.fileio.before.Reports.nonBlankLines(before), Reports.nonBlankLines(after));
        assertTrue(dev.deletejava.fileio.before.Reports.sameContent(before, after));
        assertTrue(Reports.sameContent(before, after));

        byte[] bytes = report.getBytes(StandardCharsets.UTF_8);
        assertEquals(dev.deletejava.fileio.before.Reports.toString(new ByteArrayInputStream(bytes)),
                Reports.toString(new ByteArrayInputStream(bytes)));
        var out = new ByteArrayOutputStream();
        assertEquals(bytes.length, Reports.copy(new ByteArrayInputStream(bytes), out));
        assertArrayEquals(bytes, out.toByteArray());
    }

    @Test
    // Verifies that writing a string does not create missing parent directories.
    void filesWriteStringAloneDoesNotCreateParentDirectories() {
        // The one-line replacement for FileUtils.writeStringToFile isn't quite one line
        assertThrows(NoSuchFileException.class, () -> Files.writeString(dir.resolve("missing/report.txt"), report));
    }
}
