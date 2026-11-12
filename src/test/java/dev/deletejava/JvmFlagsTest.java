package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/// Starts the JVM that runs this test with flags that are still in many JAVA_OPTS,
/// and checks what happens. Most of them don't warn anymore: the JVM refuses to start.
class JvmFlagsTest {

    private static final int FEATURE = Runtime.version().feature();

    record Result(int exitCode, String output) {}

    // Runs the current Java executable with the supplied JVM options.
    private static Result java(String... options) throws IOException, InterruptedException {
        var command = new ArrayList<>(List.of(ProcessHandle.current().info().command().orElseThrow()));
        command.addAll(List.of(options));
        command.add("-version");
        var process = new ProcessBuilder(command).redirectErrorStream(true).start();
        String output = new String(process.getInputStream().readAllBytes());
        return new Result(process.waitFor(), output);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-XX:+UseConcMarkSweepGC",       // CMS, removed in 14
            "-XX:+CMSClassUnloadingEnabled",
            "-XX:MaxPermSize=256m",          // PermGen, gone since 8
            "-XX:+UseParallelOldGC",         // removed in 15
            "-XX:+UseBiasedLocking",         // removed in 18
            "-XX:+AggressiveOpts",           // removed in 12
            "-XX:+UseFastAccessorMethods",
            "-XX:+UseCGroupMemoryLimitForHeap", // the Java 8u131 container workaround
            "-Djava.security.manager=allow"  // permanently disabled in 24
    })
    // Checks whether an obsolete JVM flag prevents startup on this Java version.
    void theseStopTheJvmFromStarting(String flag) throws Exception {
        var result = java(flag);
        assertNotEquals(0, result.exitCode(), result.output());
    }

    @Test
    // Checks the ZGC generational mode warning and removal across Java releases.
    void zGenerationalWarnsOn25AndBreaksOn27() throws Exception {
        var result = java("-XX:+ZGenerational");
        if (FEATURE >= 27) {
            assertNotEquals(0, result.exitCode(), result.output());
            assertTrue(result.output().contains("Unrecognized VM option 'ZGenerational'"), result.output());
        } else if (FEATURE == 25) {
            assertEquals(0, result.exitCode(), result.output());
            assertTrue(result.output().contains("Ignoring option ZGenerational"), result.output());
        }
    }

    @Test
    // Checks the deprecation status of compressed class pointers.
    void compressedClassPointersIsOnItsWayOut() throws Exception {
        var result = java("-XX:+UseCompressedClassPointers");
        assertEquals(0, result.exitCode(), result.output());
        if (FEATURE >= 27) {
            assertTrue(result.output().contains("support was removed in 27.0"), result.output());
        } else if (FEATURE == 25) {
            assertTrue(result.output().contains("was deprecated in version 25.0"), result.output());
        }
    }

    @Test
    // Checks when compact object headers become the default.
    void compactObjectHeadersAreTheDefaultFrom27() throws Exception {
        var result = java("-XX:+PrintFlagsFinal");
        boolean on = result.output().lines().anyMatch(l -> l.matches("\\s*bool UseCompactObjectHeaders\\s+= true.*"));
        if (FEATURE >= 27) {
            assertTrue(on);
        } else if (FEATURE == 25) {
            assertFalse(on); // on 25 it's a product flag you have to turn on: -XX:+UseCompactObjectHeaders
        }
    }
}
