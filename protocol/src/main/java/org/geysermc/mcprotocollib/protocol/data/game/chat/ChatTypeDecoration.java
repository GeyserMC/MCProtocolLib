package org.geysermc.mcprotocollib.protocol.data.game.chat;

import org.cloudburstmc.nbt.NbtMap;

import java.util.List;

// Here for implementation if one wants to cache the Style tag while we don't have DFU Codecs.
public interface ChatTypeDecoration {
    String translationKey();

    List<Parameter> parameters();

    NbtMap style();

    enum Parameter {
        CONTENT(2),
        SENDER(0),
        TARGET(1);

        private final int id;

        Parameter(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public static Parameter fromId(int id) {
            return switch (id) {
                case 1 -> TARGET;
                case 2 -> CONTENT;
                default -> SENDER;
            };
        }

        public static final Parameter[] VALUES = values();
    }
}
