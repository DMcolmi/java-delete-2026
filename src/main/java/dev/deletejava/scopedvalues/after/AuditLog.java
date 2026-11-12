package dev.deletejava.scopedvalues.after;

import dev.deletejava.scopedvalues.RequestContext;
import java.util.function.Supplier;

/// ScopedValue (final in Java 25): immutable, bound only for the duration of the call,
/// nothing to clean up, and cheap to inherit in virtual and structured-concurrency threads.
public final class AuditLog {

    private static final ScopedValue<RequestContext> CONTEXT = ScopedValue.newInstance();

    // Runs a request with its context bound for scoped-value lookups.
    public static <T> T handle(RequestContext ctx, Supplier<T> request) {
        return ScopedValue.where(CONTEXT, ctx).call(request::get);
    }

    // Formats an audit entry using the current request context, if present.
    public static String entry(String action) {
        if (!CONTEXT.isBound()) {
            return "anonymous: " + action;
        }
        RequestContext ctx = CONTEXT.get();
        return ctx.tenantId() + "/" + ctx.userId() + ": " + action;
    }

    private AuditLog() {}
}
