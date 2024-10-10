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
        this.knownPacks = helper.readList(in, buf -> KnownPack.NETWORK_CODEC.read(buf, helper));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeList(out, this.knownPacks, (buf, entry) -> KnownPack.NETWORK_CODEC.write(entry, buf, helper));
    }
}
