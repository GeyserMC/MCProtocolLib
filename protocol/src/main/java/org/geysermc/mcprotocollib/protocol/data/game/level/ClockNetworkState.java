package org.geysermc.mcprotocollib.protocol.data.game.level;

public record ClockNetworkState(long totalTicks, float partialTick, float rate) {
}
