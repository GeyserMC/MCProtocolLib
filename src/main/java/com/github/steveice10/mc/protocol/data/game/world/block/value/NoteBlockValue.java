package com.github.steveice10.mc.protocol.data.game.world.block.value;

import lombok.Data;

@Data
public class NoteBlockValue implements BlockValue {
    private final int pitch;

    public NoteBlockValue(int pitch) {
        if (pitch < 0 || pitch > 24) {
            throw new IllegalArgumentException("Pitch must be between 0 and 24.");
        }

        this.pitch = pitch;
    }
}
