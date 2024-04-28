package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.ResourcePackStatus;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundResourcePackPacket implements MinecraftPacket {

    private final @NonNull UUID id;
    private final @NonNull ResourcePackStatus status;

    public ServerboundResourcePackPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.id = helper.readUUID(in);
        this.status = ResourcePackStatus.from(helper.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeUUID(out, id);
        helper.writeVarInt(out, this.status.ordinal());
    }
}
