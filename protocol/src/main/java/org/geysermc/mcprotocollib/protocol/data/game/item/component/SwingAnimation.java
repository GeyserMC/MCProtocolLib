package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;

@Builder(toBuilder = true)
public record SwingAnimation(Type type, int duration) {

    public enum Type {
        NONE,
        WHACK,
        STAB;

        private static final Type[] VALUES = values();

        public static Type from(int id) {
            return id >= 0 && id < VALUES.length ? VALUES[id] : VALUES[0];
        }
    }
}
