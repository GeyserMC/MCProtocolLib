package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.GlobalPos;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundLoginPacket implements MinecraftPacket {
    private final int entityId;
    private final boolean hardcore;
    private final @NonNull GameMode gameMode;
    private final @Nullable GameMode previousGamemode;
    private final @NonNull String[] worldNames;
    private final @NonNull CompoundTag registry;
    private final @NonNull String dimension;
    private final @NonNull String worldName;
    private final long hashedSeed;
    private final int maxPlayers;
    private final int viewDistance;
    private final int simulationDistance;
    private final boolean reducedDebugInfo;
    private final boolean enableRespawnScreen;
    private final boolean debug;
    private final boolean flat;
    private final @Nullable GlobalPos lastDeathPos;
    private final int portalCooldown;

    public ClientboundLoginPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.entityId = in.readInt();
        this.hardcore = in.readBoolean();
        this.gameMode = GameMode.byId(in.readByte());
        this.previousGamemode = GameMode.byNullableId(in.readByte());
        int worldCount = helper.readVarInt(in);
        this.worldNames = new String[worldCount];
        for (int i = 0; i < worldCount; i++) {
            this.worldNames[i] = helper.readString(in);
        }
        this.registry = helper.readTag(in);
        this.dimension = helper.readString(in);
        this.worldName = helper.readString(in);
        this.hashedSeed = in.readLong();
        this.maxPlayers = helper.readVarInt(in);
        this.viewDistance = helper.readVarInt(in);
        this.simulationDistance = helper.readVarInt(in);
        this.reducedDebugInfo = in.readBoolean();
        this.enableRespawnScreen = in.readBoolean();
        this.debug = in.readBoolean();
        this.flat = in.readBoolean();
        if (in.readBoolean()) {
            this.lastDeathPos = helper.readGlobalPos(in);
        } else {
            this.lastDeathPos = null;
        }
        this.portalCooldown = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeInt(this.entityId);
        out.writeBoolean(this.hardcore);
        out.writeByte(this.gameMode.ordinal());
        out.writeByte(GameMode.toNullableId(this.previousGamemode));
        helper.writeVarInt(out, this.worldNames.length);
        for (String worldName : this.worldNames) {
            helper.writeString(out, worldName);
        }
        helper.writeTag(out, this.registry);
        helper.writeString(out, this.dimension);
        helper.writeString(out, this.worldName);
        out.writeLong(this.hashedSeed);
        helper.writeVarInt(out, this.maxPlayers);
        helper.writeVarInt(out, this.viewDistance);
        helper.writeVarInt(out, this.simulationDistance);
        out.writeBoolean(this.reducedDebugInfo);
        out.writeBoolean(this.enableRespawnScreen);
        out.writeBoolean(this.debug);
        out.writeBoolean(this.flat);
        out.writeBoolean(this.lastDeathPos != null);
        if (this.lastDeathPos != null) {
            helper.writeGlobalPos(out, this.lastDeathPos);
        }
        helper.writeVarInt(out, this.portalCooldown);
    }
}
