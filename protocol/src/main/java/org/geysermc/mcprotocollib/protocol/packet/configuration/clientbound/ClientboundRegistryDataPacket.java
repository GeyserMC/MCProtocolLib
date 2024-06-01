package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.RegistryEntry;

import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundRegistryDataPacket implements MinecraftPacket {
    private final String registry;
    private final List<RegistryEntry> entries;

    public ClientboundRegistryDataPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.registry = helper.readResourceLocation(in);
        this.entries = helper.readList(in, buf -> new RegistryEntry(helper.readResourceLocation(buf), helper.readNullable(buf, helper::readCompoundTag)));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeResourceLocation(out, this.registry);
        helper.writeList(out, this.entries, (buf, entry) -> {
            helper.writeResourceLocation(buf, entry.getId());
            helper.writeNullable(buf, entry.getData(), helper::writeAnyTag);
        });
    }
}
