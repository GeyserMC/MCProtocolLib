package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import lombok.Builder;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;

import java.util.List;

@Builder(toBuilder = true)
public record BlockTransformer(List<BlockTransformerData> transforms) {
    public BlockTransformer(List<BlockTransformerData> transforms) {
        this.transforms = List.copyOf(transforms);
    }

    public enum TransformParticle {
        NONE,
        SCRAPE,
        WAX_ON,
        WAX_OFF;

        private static final TransformParticle[] VALUES = values();

        public static TransformParticle from(int id) {
            return id >= 0 & id < VALUES.length ? VALUES[id] : VALUES[0];
        }
    }

    public enum DropStrategy {
        CLICKED_FACE,
        FROM_MIDDLE;

        private static final DropStrategy[] VALUES = values();

        public static DropStrategy from(int id) {
            return id >= 0 & id < VALUES.length ? VALUES[id] : VALUES[0];
        }
    }

    public enum TransformType {
        SINGLE_BLOCK,
        COPPER_CHEST;

        private static final TransformType[] VALUES = values();

        public static TransformType from(int id) {
            return id >= 0 & id < VALUES.length ? VALUES[id] : VALUES[0];
        }
    }

    @Builder(toBuilder = true)
    public record BlockTransformerData(Object blockStateProvider, Sound sound, TransformParticle particle,
                                       List<Direction> disallowedFaces, @Nullable Key loot, DropStrategy dropStrategy,
                                       boolean updateFromNeighbors, TransformType transformType, boolean consumeOnUse,
                                       int itemDamagePerUse) {
        public BlockTransformerData(Object blockStateProvider, Sound sound, TransformParticle particle,
                                    List<Direction> disallowedFaces, @Nullable Key loot, DropStrategy dropStrategy,
                                    boolean updateFromNeighbors, TransformType transformType, boolean consumeOnUse,
                                    int itemDamagePerUse) {
            this.blockStateProvider = blockStateProvider;
            this.sound = sound;
            this.particle = particle;
            this.disallowedFaces = List.copyOf(disallowedFaces);
            this.loot = loot;
            this.dropStrategy = dropStrategy;
            this.updateFromNeighbors = updateFromNeighbors;
            this.transformType = transformType;
            this.consumeOnUse = consumeOnUse;
            this.itemDamagePerUse = itemDamagePerUse;
        }
    }
}
