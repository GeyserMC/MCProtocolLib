package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.statistic.Achievement;
import org.spacehq.mc.protocol.data.game.values.statistic.BreakBlockStatistic;
import org.spacehq.mc.protocol.data.game.values.statistic.BreakItemStatistic;
import org.spacehq.mc.protocol.data.game.values.statistic.CraftItemStatistic;
import org.spacehq.mc.protocol.data.game.values.statistic.GenericStatistic;
import org.spacehq.mc.protocol.data.game.values.statistic.Statistic;
import org.spacehq.mc.protocol.data.game.values.statistic.UseItemStatistic;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerStatisticsPacket implements Packet {

    private static final String CRAFT_ITEM_PREFIX = "stats.craftItem.";
    private static final String BREAK_BLOCK_PREFIX = "stats.mineBlock.";
    private static final String USE_ITEM_PREFIX = "stats.useItem.";
    private static final String BREAK_ITEM_PREFIX = "stats.breakItem.";

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
            if(value.startsWith("achievement.")) {
                statistic = MagicValues.key(Achievement.class, value);
            } else if(value.startsWith(CRAFT_ITEM_PREFIX)) {
                statistic = new CraftItemStatistic(Integer.parseInt(value.substring(value.lastIndexOf(".") + 1)));
            } else if(value.startsWith(BREAK_BLOCK_PREFIX)) {
                statistic = new BreakBlockStatistic(Integer.parseInt(value.substring(value.lastIndexOf(".") + 1)));
            } else if(value.startsWith(USE_ITEM_PREFIX)) {
                statistic = new UseItemStatistic(Integer.parseInt(value.substring(value.lastIndexOf(".") + 1)));
            } else if(value.startsWith(BREAK_ITEM_PREFIX)) {
                statistic = new BreakItemStatistic(Integer.parseInt(value.substring(value.lastIndexOf(".") + 1)));
            } else {
                statistic = MagicValues.key(GenericStatistic.class, value);
            }

            this.statistics.put(statistic, in.readVarInt());
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.statistics.size());
        for(Statistic statistic : this.statistics.keySet()) {
            String value = "";
            if(statistic instanceof Achievement) {
                value = MagicValues.value(String.class, (Achievement) statistic);
            } else if(statistic instanceof CraftItemStatistic) {
                value = CRAFT_ITEM_PREFIX + ((CraftItemStatistic) statistic).getId();
            } else if(statistic instanceof BreakBlockStatistic) {
                value = BREAK_BLOCK_PREFIX + ((CraftItemStatistic) statistic).getId();
            } else if(statistic instanceof UseItemStatistic) {
                value = USE_ITEM_PREFIX + ((CraftItemStatistic) statistic).getId();
            } else if(statistic instanceof BreakItemStatistic) {
                value = BREAK_ITEM_PREFIX + ((CraftItemStatistic) statistic).getId();
            } else if(statistic instanceof GenericStatistic) {
                value = MagicValues.value(String.class, (GenericStatistic) statistic);
            }

            out.writeString(value);
            out.writeVarInt(this.statistics.get(statistic));
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
