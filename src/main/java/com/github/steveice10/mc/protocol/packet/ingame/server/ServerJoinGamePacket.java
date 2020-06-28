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
    private static final int GAMEMODE_FLAG_HARDCORE = 0x08;

    private int entityId;
    private boolean hardcore;
    private @NonNull GameMode gameMode;
    private @NonNull GameMode previousGamemode;
    private int worldCount;
    private String[] worldNames;
    private @NonNull CompoundTag dimensionCodec;
    private @NonNull String dimension;
    private String worldName;
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

        int gameMode = in.readUnsignedByte();
        this.hardcore = (gameMode & GAMEMODE_FLAG_HARDCORE) != 0;
        this.gameMode = MagicValues.key(GameMode.class, gameMode & GAMEMODE_MASK);
        this.previousGamemode = MagicValues.key(GameMode.class, in.readUnsignedByte());
        this.worldCount = in.readVarInt();
        this.worldNames = new String[this.worldCount];
        for (int i = 0; i < this.worldCount; i++) {
            this.worldNames[i] = in.readString();
        }
        this.dimensionCodec = NBT.read(in);
        this.dimension = in.readString();
        this.worldName = in.readString();
        this.hashedSeed = in.readLong();
        this.maxPlayers = in.readUnsignedByte();
        this.viewDistance = in.readVarInt();
        this.reducedDebugInfo = in.readBoolean();
        this.enableRespawnScreen = in.readBoolean();
        this.debug = in.readBoolean();
        this.flat = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.entityId);

        int gameMode = MagicValues.value(Integer.class, this.gameMode) & GAMEMODE_MASK;
        if (this.hardcore) {
            gameMode |= GAMEMODE_FLAG_HARDCORE;
        }

        out.writeByte(gameMode);
        out.writeByte(MagicValues.value(Integer.class, this.previousGamemode));
        out.writeVarInt(this.worldCount);
        for (String worldName : this.worldNames) {
            out.writeString(worldName);
        }
        NBT.write(out, this.dimensionCodec);
        out.writeString(this.dimension);
        out.writeString(this.worldName);
        out.writeLong(this.hashedSeed);
        out.writeByte(this.maxPlayers);
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
