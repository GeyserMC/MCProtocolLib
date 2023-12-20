package com.github.steveice10.mc.protocol.data.game.level.sound;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.NonNull;

@Data
@AllArgsConstructor
public class CustomSound implements Sound {
    private final @NonNull String name;
    private final boolean newSystem;
    private final float range;
}
