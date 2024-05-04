package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.ResourcePackStatus;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundResourcePackPacket implements MinecraftPacket {

    private final @NonNull UUID id;
    private final @NonNull ResourcePackStatus status;

    public ServerboundResourcePackPacket(MinecraftByteBuf buf) {
        this.id = buf.readUUID();
        this.status = ResourcePackStatus.from(buf.readVarInt());
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeUUID(id);
        buf.writeVarInt(this.status.ordinal());
    }
}
