package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerInfoRemovePacket implements MinecraftPacket {
    private final List<UUID> profileIds;

    public ClientboundPlayerInfoRemovePacket(MinecraftByteBuf buf) {
        this.profileIds = new ArrayList<>();
        int numIds = buf.readVarInt();
        for (int i = 0; i < numIds; i++) {
            this.profileIds.add(buf.readUUID());
        }
    }

    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.profileIds.size());
        for (UUID id : this.profileIds) {
            buf.writeUUID(id);
        }
    }
}
