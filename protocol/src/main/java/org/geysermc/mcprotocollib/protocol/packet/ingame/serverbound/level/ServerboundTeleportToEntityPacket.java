package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundTeleportToEntityPacket implements MinecraftPacket {
    private final @NonNull UUID target;

    public ServerboundTeleportToEntityPacket(MinecraftByteBuf buf) {
        this.target = buf.readUUID();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeUUID(this.target);
    }
}
