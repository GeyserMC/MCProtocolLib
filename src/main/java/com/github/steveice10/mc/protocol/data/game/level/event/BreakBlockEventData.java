package com.github.steveice10.mc.protocol.data.game.level.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class BreakBlockEventData implements LevelEventData {
    private final @NonNull int blockState;
}
