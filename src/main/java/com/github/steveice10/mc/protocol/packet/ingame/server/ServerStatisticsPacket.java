package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.statistic.BreakBlockStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.BreakItemStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.CraftItemStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.CustomStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.DropItemStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.GenericStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.KillEntityStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.KilledByEntityStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.PickupItemStatistic;
import com.github.steveice10.mc.protocol.data.game.statistic.Statistic;
import com.github.steveice10.mc.protocol.data.game.statistic.UseItemStatistic;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerStatisticsPacket extends MinecraftPacket {
    private static final String CRAFT_ITEM_PREFIX = "stat.craftItem.";
    private static final String BREAK_BLOCK_PREFIX = "stat.mineBlock.";
    private static final String USE_ITEM_PREFIX = "stat.useItem.";
    private static final String BREAK_ITEM_PREFIX = "stat.breakItem.";
    private static final String KILL_ENTITY_PREFIX = "stat.killEntity.";
    private static final String KILLED_BY_ENTITY_PREFIX = "stat.entityKilledBy.";
    private static final String DROP_ITEM_PREFIX = "stat.drop.";
    private static final String PICKUP_ITEM_PREFIX = "stat.pickup.";

    private Map<Statistic, Integer> statistics = new HashMap<Statistic, Integer>();

    @SuppressWarnings("unused")
    private ServerStatisticsPacket() {
    }

    public ServerStatisticsPacket(Map<Statistic, Integer> statistics) {
        this.statistics = statistics;
    }

    public Map<Statistic, Integer> getStatistics() {
        return this.statistics;
    }

    @Override
    public void read(NetInput in) throws IOException {
        int length = in.readVarInt();
        for(int index = 0; index < length; index++) {
            String value = in.readString();
            Statistic statistic = null;
            if(value.startsWith(CRAFT_ITEM_PREFIX)) {
                statistic = new CraftItemStatistic(value.substring(CRAFT_ITEM_PREFIX.length()));
            } else if(value.startsWith(BREAK_BLOCK_PREFIX)) {
                statistic = new BreakBlockStatistic(value.substring(BREAK_BLOCK_PREFIX.length()));
            } else if(value.startsWith(USE_ITEM_PREFIX)) {
                statistic = new UseItemStatistic(value.substring(USE_ITEM_PREFIX.length()));
            } else if(value.startsWith(BREAK_ITEM_PREFIX)) {
                statistic = new BreakItemStatistic(value.substring(BREAK_ITEM_PREFIX.length()));
            } else if(value.startsWith(KILL_ENTITY_PREFIX)) {
                statistic = new KillEntityStatistic(value.substring(KILL_ENTITY_PREFIX.length()));
            } else if(value.startsWith(KILLED_BY_ENTITY_PREFIX)) {
                statistic = new KilledByEntityStatistic(value.substring(KILLED_BY_ENTITY_PREFIX.length()));
            } else if(value.startsWith(DROP_ITEM_PREFIX)) {
                statistic = new DropItemStatistic(value.substring(DROP_ITEM_PREFIX.length()));
            } else if(value.startsWith(PICKUP_ITEM_PREFIX)) {
                statistic = new PickupItemStatistic(value.substring(PICKUP_ITEM_PREFIX.length()));
            } else {
                try {
                    statistic = MagicValues.key(GenericStatistic.class, value);
                } catch(IllegalArgumentException e) {
                    statistic = new CustomStatistic(value);
                }
            }

            this.statistics.put(statistic, in.readVarInt());
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.statistics.size());
        for(Statistic statistic : this.statistics.keySet()) {
            String value = "";
            if(statistic instanceof CraftItemStatistic) {
                value = CRAFT_ITEM_PREFIX + ((CraftItemStatistic) statistic).getId();
            } else if(statistic instanceof BreakBlockStatistic) {
                value = BREAK_BLOCK_PREFIX + ((BreakBlockStatistic) statistic).getId();
            } else if(statistic instanceof UseItemStatistic) {
                value = USE_ITEM_PREFIX + ((UseItemStatistic) statistic).getId();
            } else if(statistic instanceof BreakItemStatistic) {
                value = BREAK_ITEM_PREFIX + ((BreakItemStatistic) statistic).getId();
            } else if(statistic instanceof KillEntityStatistic) {
                value = KILL_ENTITY_PREFIX + ((KillEntityStatistic) statistic).getId();
            } else if(statistic instanceof KilledByEntityStatistic) {
                value = KILLED_BY_ENTITY_PREFIX + ((KilledByEntityStatistic) statistic).getId();
            } else if(statistic instanceof DropItemStatistic) {
                value = DROP_ITEM_PREFIX + ((DropItemStatistic) statistic).getId();
            } else if(statistic instanceof PickupItemStatistic) {
                value = PICKUP_ITEM_PREFIX + ((PickupItemStatistic) statistic).getId();
            } else if(statistic instanceof GenericStatistic) {
                value = MagicValues.value(String.class, (GenericStatistic) statistic);
            }

            out.writeString(value);
            out.writeVarInt(this.statistics.get(statistic));
        }
    }
}
