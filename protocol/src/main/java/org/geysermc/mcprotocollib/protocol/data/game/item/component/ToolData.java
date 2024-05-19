package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
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
        private final @NonNull HolderSet blocks;
        private final @Nullable Float speed;
        private final @Nullable Boolean correctForDrops;
    }
}
