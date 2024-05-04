package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.RegistryEntry;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundRegistryDataPacket implements MinecraftPacket {
    private final String registry;
    private final List<RegistryEntry> entries;

    public ClientboundRegistryDataPacket(MinecraftByteBuf buf) {
        this.registry = buf.readResourceLocation();
        this.entries = new ArrayList<>();

        int entryCount = buf.readVarInt();
        for (int i = 0; i < entryCount; i++) {
            this.entries.add(new RegistryEntry(buf.readResourceLocation(), buf.readNullable(buf::readCompoundTag)));
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeResourceLocation(this.registry);

        buf.writeVarInt(this.entries.size());
        for (RegistryEntry entry : this.entries) {
            buf.writeResourceLocation(entry.getId());
            buf.writeNullable(entry.getData(), buf::writeAnyTag);
        }
    }
}
