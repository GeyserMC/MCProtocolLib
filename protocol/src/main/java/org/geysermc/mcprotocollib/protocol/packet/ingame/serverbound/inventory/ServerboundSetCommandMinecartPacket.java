package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundSetCommandMinecartPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull String command;
    private final boolean doesTrackOutput;

    public ServerboundSetCommandMinecartPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.command = buf.readString();
        this.doesTrackOutput = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeString(this.command);
        buf.writeBoolean(this.doesTrackOutput);
    }
}
