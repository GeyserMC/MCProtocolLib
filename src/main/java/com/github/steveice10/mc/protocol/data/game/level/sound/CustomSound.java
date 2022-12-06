package com.github.steveice10.mc.protocol.data.game.level.sound;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class CustomSound implements Sound {
    private final @NotNull String name;
    private final boolean newSystem;
    private final float range;
}
