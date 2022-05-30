package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VillagerData {
    private final int type;
    private final int profession;
    private final int level;
}
