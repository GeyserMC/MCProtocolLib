package com.github.steveice10.mc.protocol.data.game.level.sound;

import org.jetbrains.annotations.NotNull;

public record CustomSound(@NotNull String name, boolean newSystem, float range) implements Sound {
}
