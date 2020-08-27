package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerJoinGamePacket implements Packet {
    private static final int GAMEMODE_MASK = 0x07;

    private int entityId;
    private boolean hardcore;
    private @NonNull GameMode gameMode;
    private GameMode previousGamemode;
    private int worldCount;
    private @NonNull String[] worldNames;
    private @NonNull CompoundTag dimensionCodec;
    private @NonNull CompoundTag dimension;
    private @NonNull String worldName;
    private long hashedSeed;
    private int maxPlayers;
    private int viewDistance;
    private boolean reducedDebugInfo;
    private boolean enableRespawnScreen;
    private boolean debug;
    private boolean flat;

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readInt();

        this.hardcore = in.readBoolean();
        int gameMode = in.readUnsignedByte();
        this.gameMode = MagicValues.key(GameMode.class, gameMode & GAMEMODE_MASK);
        this.previousGamemode = GameMode.readPreviousGameMode(in.readUnsignedByte());
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
        GameMode.writePreviousGameMode(out, this.previousGamemode);
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
        out.writeBoolean(this.reducedDebugInfo);
        out.writeBoolean(this.enableRespawnScreen);
        out.writeBoolean(this.debug);
        out.writeBoolean(this.flat);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
