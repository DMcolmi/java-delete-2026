package dev.deletejava.instanceofcast.before;

import java.util.List;
import java.util.Map;

/** A chain of instanceof checks, each followed by a cast to the type we just checked. */
public final class JsonWriter {

    public static String write(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            String s = (String) value;
            return quote(s);
        }
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(write(list.get(i)));
            }
            return sb.append(']').toString();
        }
        if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) {
                    sb.append(',');
                }
                first = false;
                sb.append(quote(String.valueOf(entry.getKey()))).append(':').append(write(entry.getValue()));
            }
            return sb.append('}').toString();
        }
        throw new IllegalArgumentException("unsupported type: " + value.getClass().getName());
    }

    private static String quote(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private JsonWriter() {}
}
