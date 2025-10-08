package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

public enum WeatheringCopperState {
    UNAFFECTED,
    EXPOSED,
    WEATHERED,
    OXIDIZED;

    private static final WeatheringCopperState[] VALUES = values();

    public static WeatheringCopperState from(int id) {
        return VALUES[id];
    }
}
