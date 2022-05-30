package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.GlobalPos;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import javax.annotation.Nullable;
import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundRespawnPacket implements MinecraftPacket {
    private final @NonNull String dimension;
    private final @NonNull String worldName;
    private final long hashedSeed;
    private final @NonNull GameMode gamemode;
    private final @NonNull GameMode previousGamemode;
    private final boolean debug;
    private final boolean flat;
    private final boolean copyMetadata;
    private final @Nullable GlobalPos lastDeathPos;

    public ClientboundRespawnPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.dimension = helper.readString(in);
        this.worldName = helper.readString(in);
        this.hashedSeed = in.readLong();
        this.gamemode = MagicValues.key(GameMode.class, in.readUnsignedByte());
        this.previousGamemode = MagicValues.key(GameMode.class, in.readUnsignedByte());
        this.debug = in.readBoolean();
        this.flat = in.readBoolean();
        this.copyMetadata = in.readBoolean();
        if (in.readBoolean()) {
            this.lastDeathPos = helper.readGlobalPos(in);
        } else {
            this.lastDeathPos = null;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeString(out, this.dimension);
        helper.writeString(out, this.worldName);
        out.writeLong(this.hashedSeed);
        out.writeByte(MagicValues.value(Integer.class, this.gamemode));
        out.writeByte(MagicValues.value(Integer.class, this.previousGamemode));
        out.writeBoolean(this.debug);
        out.writeBoolean(this.flat);
        out.writeBoolean(this.copyMetadata);
        out.writeBoolean(this.lastDeathPos != null);
        if (this.lastDeathPos != null) {
            helper.writeGlobalPos(out, this.lastDeathPos);
        }
    }
}
