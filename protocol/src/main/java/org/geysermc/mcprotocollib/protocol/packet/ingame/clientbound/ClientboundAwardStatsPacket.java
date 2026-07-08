package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;
import org.geysermc.mcprotocollib.protocol.data.game.statistic.BreakBlockStatistic;
import org.geysermc.mcprotocollib.protocol.data.game.statistic.BreakItemStatistic;
import org.geysermc.mcprotocollib.protocol.data.game.statistic.CraftItemStatistic;
import org.geysermc.mcprotocollib.protocol.data.game.statistic.CustomStatistic;
import org.geysermc.mcprotocollib.protocol.data.game.statistic.DropItemStatistic;
import org.geysermc.mcprotocollib.protocol.data.game.statistic.KillEntityStatistic;
import org.geysermc.mcprotocollib.protocol.data.game.statistic.KilledByEntityStatistic;
import org.geysermc.mcprotocollib.protocol.data.game.statistic.PickupItemStatistic;
import org.geysermc.mcprotocollib.protocol.data.game.statistic.Statistic;
import org.geysermc.mcprotocollib.protocol.data.game.statistic.StatisticCategory;
import org.geysermc.mcprotocollib.protocol.data.game.statistic.UseItemStatistic;

@Data
@With
@AllArgsConstructor
public class ClientboundAwardStatsPacket implements MinecraftPacket {
    private final @NonNull Object2IntMap<Statistic> statistics = new Object2IntOpenHashMap<>();

    public ClientboundAwardStatsPacket(ByteBuf in) {
        int length = MinecraftTypes.readVarInt(in);
        for (int index = 0; index < length; index++) {
            StatisticCategory category = MinecraftTypes.readStatisticCategory(in);
            int statisticId = MinecraftTypes.readVarInt(in);
            Statistic statistic = switch (category) {
                case BREAK_BLOCK -> new BreakBlockStatistic(statisticId);
                case CRAFT_ITEM -> new CraftItemStatistic(statisticId);
                case USE_ITEM -> new UseItemStatistic(statisticId);
                case BREAK_ITEM -> new BreakItemStatistic(statisticId);
                case PICKED_UP_ITEM -> new PickupItemStatistic(statisticId);
                case DROP_ITEM -> new DropItemStatistic(statisticId);
                case KILL_ENTITY -> new KillEntityStatistic(EntityType.from(statisticId));
                case KILLED_BY_ENTITY -> new KilledByEntityStatistic(EntityType.from(statisticId));
                case CUSTOM -> CustomStatistic.from(statisticId);
            };
            this.statistics.put(statistic, MinecraftTypes.readVarInt(in));
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.statistics.size());
        for (Object2IntMap.Entry<Statistic> entry : statistics.object2IntEntrySet()) {
            Statistic statistic = entry.getKey();

            StatisticCategory category;
            int statisticId;
            if (statistic instanceof BreakBlockStatistic) {
                category = StatisticCategory.BREAK_BLOCK;
                statisticId = ((BreakBlockStatistic) statistic).getId();
            } else if (statistic instanceof CraftItemStatistic) {
                category = StatisticCategory.CRAFT_ITEM;
                statisticId = ((CraftItemStatistic) statistic).getId();
            } else if (statistic instanceof UseItemStatistic) {
                category = StatisticCategory.USE_ITEM;
                statisticId = ((UseItemStatistic) statistic).getId();
            } else if (statistic instanceof BreakItemStatistic) {
                category = StatisticCategory.BREAK_ITEM;
                statisticId = ((BreakItemStatistic) statistic).getId();
            } else if (statistic instanceof PickupItemStatistic) {
                category = StatisticCategory.PICKED_UP_ITEM;
                statisticId = ((PickupItemStatistic) statistic).getId();
            } else if (statistic instanceof DropItemStatistic) {
                category = StatisticCategory.DROP_ITEM;
                statisticId = ((DropItemStatistic) statistic).getId();
            } else if (statistic instanceof KillEntityStatistic) {
                category = StatisticCategory.KILL_ENTITY;
                statisticId = ((KillEntityStatistic) statistic).getEntity().id();
            } else if (statistic instanceof KilledByEntityStatistic) {
                category = StatisticCategory.KILLED_BY_ENTITY;
                statisticId = ((KilledByEntityStatistic) statistic).getEntity().id();
            } else if (statistic instanceof CustomStatistic) {
                category = StatisticCategory.CUSTOM;
                statisticId = ((CustomStatistic) statistic).ordinal();
            } else {
                throw new IllegalStateException();
            }
            MinecraftTypes.writeStatisticCategory(out, category);
            MinecraftTypes.writeVarInt(out, statisticId);
            MinecraftTypes.writeVarInt(out, entry.getIntValue());
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
