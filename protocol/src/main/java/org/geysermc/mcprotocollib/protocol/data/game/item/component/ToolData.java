package com.github.steveice10.mc.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
public class ToolData {
    private final List<Rule> rules;
    private final float defaultMiningSpeed;
    private final int damagePerBlock;

    @Data
    @AllArgsConstructor
    public static class Rule {
        private final @Nullable String location;
        private final int @Nullable[] holders;
        private final @Nullable Float speed;
        private final @Nullable Boolean correctForDrops;
    }
}
