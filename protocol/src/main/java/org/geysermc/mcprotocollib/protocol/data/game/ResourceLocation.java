package org.geysermc.mcprotocollib.protocol.data.game;

public record ResourceLocation(String namespace, String path) {
    public static ResourceLocation fromString(String identifier) {
        int index = identifier.indexOf(':');
        if (index == -1) {
            return new ResourceLocation("minecraft", identifier);
        } else {
            return new ResourceLocation(
                    identifier.substring(0, index),
                    identifier.substring(index + 1)
            );
        }
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }
}
