package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.world.WorldType;
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
    private int dimension;
    private long hashedSeed;
    private int maxPlayers;
    private @NonNull WorldType worldType;
    private int viewDistance;
    private boolean reducedDebugInfo;
    private boolean enableRespawnScreen;

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readInt();

        int gameMode = in.readUnsignedByte();
        this.hardcore = (gameMode & GAMEMODE_FLAG_HARDCORE) != 0;
        this.gameMode = MagicValues.key(GameMode.class, gameMode & GAMEMODE_MASK);

        this.dimension = in.readInt();
        this.hashedSeed = in.readLong();
        this.maxPlayers = in.readUnsignedByte();
        this.worldType = MagicValues.key(WorldType.class, in.readString().toLowerCase());
        this.viewDistance = in.readVarInt();
        this.reducedDebugInfo = in.readBoolean();
        this.enableRespawnScreen = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.entityId);

        int gameMode = MagicValues.value(Integer.class, this.gameMode) & GAMEMODE_MASK;
        if(this.hardcore) {
            gameMode |= GAMEMODE_FLAG_HARDCORE;
        }

        out.writeByte(gameMode);

        out.writeInt(this.dimension);
        out.writeLong(this.hashedSeed);
        out.writeByte(this.maxPlayers);
        out.writeString(MagicValues.value(String.class, this.worldType));
        out.writeVarInt(this.viewDistance);
        out.writeBoolean(this.reducedDebugInfo);
        out.writeBoolean(this.enableRespawnScreen);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
