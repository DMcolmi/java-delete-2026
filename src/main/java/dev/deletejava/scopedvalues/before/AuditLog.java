package dev.deletejava.scopedvalues.before;

import dev.deletejava.scopedvalues.RequestContext;
import java.util.function.Supplier;

/**
 * ThreadLocal: mutable, unbounded lifetime, and a forgotten remove() leaks one request's
 * context into the next request served by the same pooled thread.
 */
public final class AuditLog {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    // Runs a request with its context available to downstream calls.
    public static <T> T handle(RequestContext ctx, Supplier<T> request) {
        CONTEXT.set(ctx);
        try {
            return request.get();
        } finally {
            CONTEXT.remove();
        }
    }

    // Formats an audit entry using the current request context, if present.
    public static String entry(String action) {
        RequestContext ctx = CONTEXT.get();
        if (ctx == null) {
            return "anonymous: " + action;
        }
        return ctx.tenantId() + "/" + ctx.userId() + ": " + action;
    }

    private AuditLog() {}
}
