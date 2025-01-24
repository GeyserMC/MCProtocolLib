package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.List;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerInfoRemovePacket implements MinecraftPacket {
    private final List<UUID> profileIds;

    public ClientboundPlayerInfoRemovePacket(ByteBuf in) {
        this.profileIds = MinecraftTypes.readList(in, MinecraftTypes::readUUID);
    }

    public void serialize(ByteBuf out) {
        MinecraftTypes.writeList(out, this.profileIds, MinecraftTypes::writeUUID);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
