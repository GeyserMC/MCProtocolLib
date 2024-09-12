package org.geysermc.mcprotocollib.network.helper;

public class PipelineHelper {
    public static boolean containsCause(Throwable t, Class<?> c) {
        do {
            if (c.isAssignableFrom(t.getClass())) {
                return true;
            }

            t = t.getCause();
        } while (t != null);

        return false;
    }
}
