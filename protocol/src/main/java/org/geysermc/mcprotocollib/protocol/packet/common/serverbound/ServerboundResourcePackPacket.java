package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.ResourcePackStatus;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundResourcePackPacket implements MinecraftPacket {

    private final @NonNull UUID id;
    private final @NonNull ResourcePackStatus status;

    public ServerboundResourcePackPacket(ByteBuf in) {
        this.id = MinecraftTypes.readUUID(in);
        this.status = ResourcePackStatus.from(MinecraftTypes.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeUUID(out, id);
        MinecraftTypes.writeVarInt(out, this.status.ordinal());
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
