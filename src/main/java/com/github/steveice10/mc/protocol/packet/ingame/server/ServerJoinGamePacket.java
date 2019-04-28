package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.github.steveice10.mc.protocol.data.game.world.WorldType;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ServerJoinGamePacket extends MinecraftPacket {
    private int entityId;
    private boolean hardcore;
    private GameMode gamemode;
    private int dimension;
    private int maxPlayers;
    private WorldType worldType;
    private int viewDistance;
    private boolean reducedDebugInfo;

    @SuppressWarnings("unused")
    private ServerJoinGamePacket() {
    }

    public ServerJoinGamePacket(int entityId, boolean hardcore, GameMode gamemode, int dimension, int maxPlayers, WorldType worldType, int viewDistance, boolean reducedDebugInfo) {
        this.entityId = entityId;
        this.hardcore = hardcore;
        this.gamemode = gamemode;
        this.dimension = dimension;
        this.maxPlayers = maxPlayers;
        this.worldType = worldType;
        this.viewDistance = viewDistance;
        this.reducedDebugInfo = reducedDebugInfo;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public boolean getHardcore() {
        return this.hardcore;
    }

    public GameMode getGameMode() {
        return this.gamemode;
    }

    public int getDimension() {
        return this.dimension;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public boolean getReducedDebugInfo() {
        return this.reducedDebugInfo;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readInt();
        int gamemode = in.readUnsignedByte();
        this.hardcore = (gamemode & 8) == 8;
        gamemode &= -9;
        this.gamemode = MagicValues.key(GameMode.class, gamemode);
        this.dimension = in.readInt();
        this.maxPlayers = in.readUnsignedByte();
        this.worldType = MagicValues.key(WorldType.class, in.readString().toLowerCase());
        this.viewDistance = in.readVarInt();
        this.reducedDebugInfo = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.entityId);
        int gamemode = MagicValues.value(Integer.class, this.gamemode);
        if(this.hardcore) {
            gamemode |= 8;
        }

        out.writeByte(gamemode);
        out.writeInt(this.dimension);
        out.writeByte(this.maxPlayers);
        out.writeString(MagicValues.value(String.class, this.worldType));
        out.writeVarInt(this.viewDistance);
        out.writeBoolean(this.reducedDebugInfo);
    }
}
