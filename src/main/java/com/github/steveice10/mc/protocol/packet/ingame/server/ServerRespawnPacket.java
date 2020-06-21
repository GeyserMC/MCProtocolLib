package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
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
    private String dimension;
    private String worldName;
    private long hashedSeed;
    private @NonNull GameMode gamemode;
    private @NonNull GameMode previousGamemode;
    private boolean debug;
    private boolean flat;
    private boolean copyMetadata;

    @Override
    public void read(NetInput in) throws IOException {
        this.dimension = in.readString();
        this.worldName = in.readString();
        this.hashedSeed = in.readLong();
        this.gamemode = MagicValues.key(GameMode.class, in.readUnsignedByte());
        this.previousGamemode = MagicValues.key(GameMode.class, in.readUnsignedByte());
        this.debug = in.readBoolean();
        this.flat = in.readBoolean();
        this.copyMetadata = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.dimension);
        out.writeString(this.worldName);
        out.writeLong(this.hashedSeed);
        out.writeByte(MagicValues.value(Integer.class, this.gamemode));
        out.writeByte(MagicValues.value(Integer.class, this.previousGamemode));
        out.writeBoolean(this.debug);
        out.writeBoolean(this.flat);
        out.writeBoolean(this.copyMetadata);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
