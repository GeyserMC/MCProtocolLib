package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import javax.annotation.Nullable;
import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundRespawnPacket implements Packet {
    private final @NonNull String dimension;
    private final @NonNull String worldName;
    private final long hashedSeed;
    private final @NonNull GameMode gamemode;
    private final @NonNull GameMode previousGamemode;
    private final boolean debug;
    private final boolean flat;
    private final boolean copyMetadata;
    private final @Nullable String lastDeathDimension;
    private final @Nullable Position lastDeathPos;

    public ClientboundRespawnPacket(NetInput in) throws IOException {
        this.dimension = in.readString();
        this.worldName = in.readString();
        this.hashedSeed = in.readLong();
        this.gamemode = MagicValues.key(GameMode.class, in.readUnsignedByte());
        this.previousGamemode = MagicValues.key(GameMode.class, in.readUnsignedByte());
        this.debug = in.readBoolean();
        this.flat = in.readBoolean();
        this.copyMetadata = in.readBoolean();
        if (in.readBoolean()) {
            this.lastDeathDimension = in.readString();
            this.lastDeathPos = Position.read(in);
        } else {
            this.lastDeathDimension = null;
            this.lastDeathPos = null;
        }
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
        out.writeBoolean(this.lastDeathPos != null);
        if (this.lastDeathPos != null) {
            out.writeString(this.lastDeathDimension);
            Position.write(out, this.lastDeathPos);
        }
    }
}
