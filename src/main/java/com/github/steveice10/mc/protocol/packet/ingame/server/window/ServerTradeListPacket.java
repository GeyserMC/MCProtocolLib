package com.github.steveice10.mc.protocol.packet.ingame.server.window;

import java.io.IOException;

import com.github.steveice10.mc.protocol.data.game.window.VillagerTrade;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

public class ServerTradeListPacket extends MinecraftPacket {

    private int windowId;
    private VillagerTrade[] trades;
    private int villagerLevel;
    private int experience;
    private boolean isRegularVillager;

    public ServerTradeListPacket() {
    }

    public ServerTradeListPacket(int windowId, VillagerTrade[] trades, int villagerLevel, int experience, boolean isRegularVillager) {
        this.windowId = windowId;
        this.trades = trades;
        this.villagerLevel = villagerLevel;
        this.experience = experience;
        this.isRegularVillager = isRegularVillager;
    }

    public int getWindowId() {
        return windowId;
    }

    public VillagerTrade[] getTrades() {
        return trades;
    }

    public int getVillagerLevel() {
        return villagerLevel;
    }

    public int getExperience() {
        return experience;
    }

    public boolean isRegularVillager() {
        return isRegularVillager;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readVarInt();
        byte size = in.readByte();
        this.trades = new VillagerTrade[size];
        for (int i = 0; i < trades.length; i++) {
            trades[i] = new VillagerTrade();
            trades[i].read(in);
        }
        this.villagerLevel = in.readVarInt();
        this.experience = in.readVarInt();
        this.isRegularVillager = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.windowId);
        out.writeByte(this.trades.length);
        for (int i = 0; i < trades.length; i++) {
            this.trades[i].write(out);
        }
        out.writeVarInt(this.villagerLevel);
        out.writeVarInt(this.experience);
        out.writeBoolean(this.isRegularVillager);
    }
}
