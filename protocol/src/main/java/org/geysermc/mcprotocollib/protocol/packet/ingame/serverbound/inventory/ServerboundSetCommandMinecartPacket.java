package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundSetCommandMinecartPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull String command;
    private final boolean doesTrackOutput;

    public ServerboundSetCommandMinecartPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entityId = helper.readVarInt(in);
        this.command = helper.readString(in);
        this.doesTrackOutput = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.entityId);
        helper.writeString(out, this.command);
        out.writeBoolean(this.doesTrackOutput);
    }
}
