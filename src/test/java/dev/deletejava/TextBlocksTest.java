package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import dev.deletejava.textblocks.after.Templates;
import org.junit.jupiter.api.Test;

class TextBlocksTest {

    @Test
    // Verifies that both template implementations generate identical strings.
    void identicalStrings() {
        assertEquals(dev.deletejava.textblocks.before.Templates.overdueInvoicesSql(), Templates.overdueInvoicesSql());
        assertEquals(dev.deletejava.textblocks.before.Templates.welcomeJson("Ada", "ada@example.com"),
                Templates.welcomeJson("Ada", "ada@example.com"));
    }

    @Test
    // Checks that text blocks strip incidental indentation without losing relative indentation.
    void indentationIsStrippedButRelativeIndentKept() {
        assertTrue(Templates.overdueInvoicesSql().startsWith("SELECT"));
        assertTrue(Templates.overdueInvoicesSql().contains("\n  AND i.paid = FALSE\n"));
    }
}
