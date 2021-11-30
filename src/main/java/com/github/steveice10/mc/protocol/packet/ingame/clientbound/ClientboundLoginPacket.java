package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundLoginPacket implements Packet {
    private static final int GAMEMODE_MASK = 0x07;

    private final int entityId;
    private final boolean hardcore;
    private final @NonNull GameMode gameMode;
    private final GameMode previousGamemode;
    private final int worldCount;
    private final @NonNull String[] worldNames;
    private final @NonNull CompoundTag dimensionCodec;
    private final @NonNull CompoundTag dimension;
    private final @NonNull String worldName;
    private final long hashedSeed;
    private final int maxPlayers;
    private final int viewDistance;
    private final int simulationDistance;
    private final boolean reducedDebugInfo;
    private final boolean enableRespawnScreen;
    private final boolean debug;
    private final boolean flat;

    public ClientboundLoginPacket(NetInput in) throws IOException {
        this.entityId = in.readInt();

        this.hardcore = in.readBoolean();
        int gameMode = in.readUnsignedByte();
        this.gameMode = MagicValues.key(GameMode.class, gameMode & GAMEMODE_MASK);
        this.previousGamemode = MagicValues.key(GameMode.class, in.readUnsignedByte());
        this.worldCount = in.readVarInt();
        this.worldNames = new String[this.worldCount];
        for (int i = 0; i < this.worldCount; i++) {
            this.worldNames[i] = in.readString();
        }
        this.dimensionCodec = NBT.read(in);
        this.dimension = NBT.read(in);
        this.worldName = in.readString();
        this.hashedSeed = in.readLong();
        this.maxPlayers = in.readVarInt();
        this.viewDistance = in.readVarInt();
        this.simulationDistance = in.readVarInt();
        this.reducedDebugInfo = in.readBoolean();
        this.enableRespawnScreen = in.readBoolean();
        this.debug = in.readBoolean();
        this.flat = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.entityId);

        out.writeBoolean(this.hardcore);
        int gameMode = MagicValues.value(Integer.class, this.gameMode) & GAMEMODE_MASK;

        out.writeByte(gameMode);
        out.writeByte(MagicValues.value(Integer.class, this.previousGamemode));
        out.writeVarInt(this.worldCount);
        for (String worldName : this.worldNames) {
            out.writeString(worldName);
        }
        NBT.write(out, this.dimensionCodec);
        NBT.write(out, this.dimension);
        out.writeString(this.worldName);
        out.writeLong(this.hashedSeed);
        out.writeVarInt(this.maxPlayers);
        out.writeVarInt(this.viewDistance);
        out.writeVarInt(this.simulationDistance);
        out.writeBoolean(this.reducedDebugInfo);
        out.writeBoolean(this.enableRespawnScreen);
        out.writeBoolean(this.debug);
        out.writeBoolean(this.flat);
    }
}
