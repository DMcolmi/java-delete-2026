package dev.deletejava.textblocks.after;

/// Text blocks (final in Java 15): the string looks like what it produces.
/// Indentation is stripped relative to the closing `"""`, and `formatted()` fills in the values.
public final class Templates {

    // Returns a SQL query that selects overdue invoices.
    public static String overdueInvoicesSql() {
        return """
                SELECT i.id, i.total, c.email
                FROM invoices i
                JOIN customers c ON c.id = i.customer_id
                WHERE i.due_date < CURRENT_DATE
                  AND i.paid = FALSE
                ORDER BY i.due_date
                """;
    }

    // Builds a JSON welcome message for a user.
    public static String welcomeJson(String name, String email) {
        return """
                {
                  "name": "%s",
                  "email": "%s",
                  "tags": ["new"]
                }""".formatted(name, email);
    }

    private Templates() {}
}
