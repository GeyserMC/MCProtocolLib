package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.KnownPack;

import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundSelectKnownPacks implements MinecraftPacket {
    private final List<KnownPack> knownPacks;

    public ClientboundSelectKnownPacks(ByteBuf in) {
        this.knownPacks = MinecraftTypes.readList(in, buf -> new KnownPack(MinecraftTypes.readString(buf), MinecraftTypes.readString(buf), MinecraftTypes.readString(buf)));
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeList(out, this.knownPacks, (buf, entry) -> {
            MinecraftTypes.writeString(buf, entry.getNamespace());
            MinecraftTypes.writeString(buf, entry.getId());
            MinecraftTypes.writeString(buf, entry.getVersion());
        });
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
