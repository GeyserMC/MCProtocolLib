package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.GlobalPos;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.jetbrains.annotations.Nullable;

@Data
@With
@AllArgsConstructor
public class ClientboundRespawnPacket implements MinecraftPacket {
    private static final byte KEEP_ATTRIBUTES = 1;
    private static final byte KEEP_ENTITY_DATA = 2;

    private final @NonNull String dimension;
    private final @NonNull String worldName;
    private final long hashedSeed;
    private final @NonNull GameMode gamemode;
    private final @Nullable GameMode previousGamemode;
    private final boolean debug;
    private final boolean flat;
    // The following two are the dataToKeep byte
    private final boolean keepMetadata;
    private final boolean keepAttributes;
    private final @Nullable GlobalPos lastDeathPos;
    private final int portalCooldown;

    public ClientboundRespawnPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.dimension = helper.readString(in);
        this.worldName = helper.readString(in);
        this.hashedSeed = in.readLong();
        this.gamemode = GameMode.byId(in.readUnsignedByte()); // Intentionally unsigned as of 1.19.3
        this.previousGamemode = GameMode.byNullableId(in.readByte());
        this.debug = in.readBoolean();
        this.flat = in.readBoolean();
        byte dataToKeep = in.readByte();
        this.keepAttributes = (dataToKeep & KEEP_ATTRIBUTES) != 0;
        this.keepMetadata = (dataToKeep & KEEP_ENTITY_DATA) != 0;
        this.lastDeathPos = helper.readNullable(in, helper::readGlobalPos);
        this.portalCooldown = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeString(out, this.dimension);
        helper.writeString(out, this.worldName);
        out.writeLong(this.hashedSeed);
        out.writeByte(this.gamemode.ordinal());
        out.writeByte(GameMode.toNullableId(this.previousGamemode));
        out.writeBoolean(this.debug);
        out.writeBoolean(this.flat);
        byte dataToKeep = 0;
        if (this.keepMetadata) {
            dataToKeep += KEEP_ENTITY_DATA;
        }
        if (this.keepAttributes) {
            dataToKeep += KEEP_ATTRIBUTES;
        }
        out.writeByte(dataToKeep);
        helper.writeNullable(out, this.lastDeathPos, helper::writeGlobalPos);
        helper.writeVarInt(out, this.portalCooldown);
    }
}
