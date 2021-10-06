package com.github.steveice10.mc.protocol.data.game.level.sound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class CustomSound implements Sound {
    private final @NonNull String name;
}
