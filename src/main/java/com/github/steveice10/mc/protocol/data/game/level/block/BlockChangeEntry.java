package com.github.steveice10.mc.protocol.data.game.level.block;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class BlockChangeEntry {
    private final @NonNull Position position;
    private final @NonNull int block;
}
