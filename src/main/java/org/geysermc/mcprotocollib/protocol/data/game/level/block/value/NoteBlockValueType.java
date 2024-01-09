package org.geysermc.mcprotocollib.protocol.data.game.level.block.value;

public enum NoteBlockValueType implements BlockValueType {
    PLAY_NOTE;

    private static final NoteBlockValueType[] VALUES = values();

    public static NoteBlockValueType from(int id) {
        return VALUES[id];
    }
}
