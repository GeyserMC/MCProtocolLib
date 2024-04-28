package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundTeleportToEntityPacket implements MinecraftPacket {
    private final @NonNull UUID target;

    public ServerboundTeleportToEntityPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.target = helper.readUUID(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeUUID(out, this.target);
    }
}
