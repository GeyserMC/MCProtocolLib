package org.geysermc.mcprotocollib.protocol.data.game.setting;

public enum ParticleStatus {
    ALL,
    DECREASED,
    MINIMAL;

    public static final ParticleStatus[] VALUES = values();

    public static ParticleStatus from(int id) {
        return VALUES[id];
    }
}
