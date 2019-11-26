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
public class ServerRespawnPacket implements Packet {
    private int dimension;
    private long hashedSeed;
    private @NonNull GameMode gamemode;
    private @NonNull WorldType worldType;

    @Override
    public void read(NetInput in) throws IOException {
        this.dimension = in.readInt();
        this.hashedSeed = in.readLong();
        this.gamemode = MagicValues.key(GameMode.class, in.readUnsignedByte());
        this.worldType = MagicValues.key(WorldType.class, in.readString().toLowerCase());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.dimension);
        out.writeLong(this.hashedSeed);
        out.writeByte(MagicValues.value(Integer.class, this.gamemode));
        out.writeString(MagicValues.value(String.class, this.worldType));
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
