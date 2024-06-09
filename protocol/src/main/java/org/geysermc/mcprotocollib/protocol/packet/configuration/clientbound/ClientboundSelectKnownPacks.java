package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.KnownPack;

import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundSelectKnownPacks implements MinecraftPacket {
    private final List<KnownPack> knownPacks;

    public ClientboundSelectKnownPacks(ByteBuf in, MinecraftCodecHelper helper) {
        this.knownPacks = helper.readList(in, buf -> new KnownPack(helper.readString(buf), helper.readString(buf), helper.readString(buf)));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeList(out, this.knownPacks, (buf, entry) -> {
            helper.writeString(buf, entry.getNamespace());
            helper.writeString(buf, entry.getId());
            helper.writeString(buf, entry.getVersion());
        });
    }
}
