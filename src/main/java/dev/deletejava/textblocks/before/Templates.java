package dev.deletejava.textblocks.before;

/** Multi-line strings glued together with + and \n, and quotes escaped by hand. */
public final class Templates {

    // Returns a SQL query that selects overdue invoices.
    public static String overdueInvoicesSql() {
        return "SELECT i.id, i.total, c.email\n"
                + "FROM invoices i\n"
                + "JOIN customers c ON c.id = i.customer_id\n"
                + "WHERE i.due_date < CURRENT_DATE\n"
                + "  AND i.paid = FALSE\n"
                + "ORDER BY i.due_date\n";
    }

    // Builds a JSON welcome message for a user.
    public static String welcomeJson(String name, String email) {
        return "{\n"
                + "  \"name\": \"" + name + "\",\n"
                + "  \"email\": \"" + email + "\",\n"
                + "  \"tags\": [\"new\"]\n"
                + "}";
    }

    private Templates() {}
}
