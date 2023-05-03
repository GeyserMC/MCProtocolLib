package com.github.steveice10.mc.protocol.data.game.level.event;

public final class UnknownLevelEvent implements LevelEvent {
    private final int id;

    public UnknownLevelEvent(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}
