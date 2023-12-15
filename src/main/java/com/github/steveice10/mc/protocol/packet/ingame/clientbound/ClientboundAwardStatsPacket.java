package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.type.EntityType;
import com.github.steveice10.mc.protocol.data.game.statistic.*;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundAwardStatsPacket implements MinecraftPacket {
    private final @NonNull Object2IntMap<Statistic> statistics = new Object2IntOpenHashMap<>();

    public ClientboundAwardStatsPacket(ByteBuf in, MinecraftCodecHelper helper) {
        int length = helper.readVarInt(in);
        for (int index = 0; index < length; index++) {
            StatisticCategory category = helper.readStatisticCategory(in);
            int statisticId = helper.readVarInt(in);
            Statistic statistic;
            switch (category) {
                case BREAK_BLOCK:
                    statistic = new BreakBlockStatistic(statisticId);
                    break;
                case CRAFT_ITEM:
                    statistic = new CraftItemStatistic(statisticId);
                    break;
                case USE_ITEM:
                    statistic = new UseItemStatistic(statisticId);
                    break;
                case BREAK_ITEM:
                    statistic = new BreakItemStatistic(statisticId);
                    break;
                case PICKED_UP_ITEM:
                    statistic = new PickupItemStatistic(statisticId);
                    break;
                case DROP_ITEM:
                    statistic = new DropItemStatistic(statisticId);
                    break;
                case KILL_ENTITY:
                    statistic = new KillEntityStatistic(EntityType.from(statisticId));
                    break;
                case KILLED_BY_ENTITY:
                    statistic = new KilledByEntityStatistic(EntityType.from(statisticId));
                    break;
                case CUSTOM:
                    statistic = CustomStatistic.from(statisticId);
                    break;
                default:
                    throw new IllegalStateException();
            }
            this.statistics.put(statistic, helper.readVarInt(in));
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.statistics.size());
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
                statisticId = ((KillEntityStatistic) statistic).getEntity().ordinal();
            } else if (statistic instanceof KilledByEntityStatistic) {
                category = StatisticCategory.KILLED_BY_ENTITY;
                statisticId = ((KilledByEntityStatistic) statistic).getEntity().ordinal();
            } else if (statistic instanceof CustomStatistic) {
                category = StatisticCategory.CUSTOM;
                statisticId = ((CustomStatistic) statistic).ordinal();
            } else {
                throw new IllegalStateException();
            }
            helper.writeStatisticCategory(out, category);
            helper.writeVarInt(out, statisticId);
            helper.writeVarInt(out, entry.getIntValue());
        }
    }
}
