package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.RegistryEntry;

import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundRegistryDataPacket implements MinecraftPacket {
    private final Key registry;
    private final List<RegistryEntry> entries;

    public ClientboundRegistryDataPacket(ByteBuf in) {
        this.registry = MinecraftTypes.readResourceLocation(in);
        this.entries = MinecraftTypes.readList(in, buf -> new RegistryEntry(MinecraftTypes.readResourceLocation(buf), MinecraftTypes.readNullable(buf, MinecraftTypes::readCompoundTag)));
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeResourceLocation(out, this.registry);
        MinecraftTypes.writeList(out, this.entries, (buf, entry) -> {
            MinecraftTypes.writeResourceLocation(buf, entry.getId());
            MinecraftTypes.writeNullable(buf, entry.getData(), MinecraftTypes::writeAnyTag);
        });
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
