package dev.deletejava;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.deletejava.scopedvalues.RequestContext;
import dev.deletejava.scopedvalues.after.AuditLog;
import org.junit.jupiter.api.Test;

class ScopedValuesTest {

    @Test
    // Compares audit entries inside and outside a request context.
    void sameEntriesInsideAndOutsideARequest() {
        var ctx = new RequestContext("acme", "ada");
        assertEquals("acme/ada: login",
                dev.deletejava.scopedvalues.before.AuditLog.handle(ctx, () -> dev.deletejava.scopedvalues.before.AuditLog.entry("login")));
        assertEquals("acme/ada: login", AuditLog.handle(ctx, () -> AuditLog.entry("login")));

        // After the request the context is gone in both versions (the ThreadLocal one only thanks to remove())
        assertEquals("anonymous: ping", dev.deletejava.scopedvalues.before.AuditLog.entry("ping"));
        assertEquals("anonymous: ping", AuditLog.entry("ping"));
    }
}
