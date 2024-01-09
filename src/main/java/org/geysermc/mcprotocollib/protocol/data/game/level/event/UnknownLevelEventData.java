package org.geysermc.mcprotocollib.protocol.data.game.level.event;

public final class UnknownLevelEventData implements LevelEventData {
    private final int data;

    public UnknownLevelEventData(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }
}
