package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.world.WorldType;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.github.steveice10.mc.protocol.util.ReflectionToString;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;

public class ServerRespawnPacket implements Packet {

    private int dimension;
    private Difficulty difficulty;
    private GameMode gamemode;
    private WorldType worldType;

    @SuppressWarnings("unused")
    private ServerRespawnPacket() {
    }

    public ServerRespawnPacket(int dimension, Difficulty difficulty, GameMode gamemode, WorldType worldType) {
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.gamemode = gamemode;
        this.worldType = worldType;
    }

    public int getDimension() {
        return this.dimension;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public GameMode getGameMode() {
        return this.gamemode;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.dimension = in.readInt();
        this.difficulty = MagicValues.key(Difficulty.class, in.readUnsignedByte());
        this.gamemode = MagicValues.key(GameMode.class, in.readUnsignedByte());
        this.worldType = MagicValues.key(WorldType.class, in.readString().toLowerCase());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.dimension);
        out.writeByte(MagicValues.value(Integer.class, this.difficulty));
        out.writeByte(MagicValues.value(Integer.class, this.gamemode));
        out.writeString(MagicValues.value(String.class, this.worldType));
    }

    @Override
    public boolean isPriority() {
        return false;
    }

    @Override
    public String toString() {
        return ReflectionToString.toString(this);
    }
}
