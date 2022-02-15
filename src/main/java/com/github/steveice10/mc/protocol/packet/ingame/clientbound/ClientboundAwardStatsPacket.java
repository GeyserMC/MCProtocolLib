package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.game.entity.type.EntityType;
import com.github.steveice10.mc.protocol.data.game.statistic.BreakBlockStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.BreakItemStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.CraftItemStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.CustomStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.DropItemStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.KillEntityStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.KilledByEntityStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.PickupItemStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.Statistic;
import com.github.steveice10.mc.protocol.data.game.statistic.StatisticCategory;
import com.github.steveice10.mc.protocol.data.game.statistic.UseItemStatistic;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
@With
@AllArgsConstructor
public class ClientboundAwardStatsPacket implements Packet {
    private final @NonNull Map<Statistic, Integer> statistics = new HashMap<>(); //TODO Fastutil

    public ClientboundAwardStatsPacket(NetInput in) throws IOException {
        int length = in.readVarInt();
        for (int index = 0; index < length; index++) {
            StatisticCategory category = StatisticCategory.read(in);
            int statisticId = in.readVarInt();
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
                    statistic = new KillEntityStatistic(EntityType.fromId(statisticId));
                    break;
                case KILLED_BY_ENTITY:
                    statistic = new KilledByEntityStatistic(EntityType.fromId(statisticId));
                    break;
                case CUSTOM:
                    statistic = CustomStatistic.fromId(statisticId);
                    break;
                default:
                    throw new IllegalStateException();
            }
            this.statistics.put(statistic, in.readVarInt());
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.statistics.size());
        for (Map.Entry<Statistic, Integer> entry : statistics.entrySet()) {
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
            out.writeVarInt(category.ordinal());
            out.writeVarInt(statisticId);
            out.writeVarInt(entry.getValue());
        }
    }
}
