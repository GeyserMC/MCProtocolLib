package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Data
@With
@Builder(toBuilder = true)
public class ToolData {
    private final List<Rule> rules;
    private final float defaultMiningSpeed;
    private final int damagePerBlock;

    public ToolData(List<Rule> rules, float defaultMiningSpeed, int damagePerBlock) {
        this.rules = List.copyOf(rules);
        this.defaultMiningSpeed = defaultMiningSpeed;
        this.damagePerBlock = damagePerBlock;
    }

    @Data
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Rule {
        private final @NonNull HolderSet blocks;
        private final @Nullable Float speed;
        private final @Nullable Boolean correctForDrops;
    }
}
