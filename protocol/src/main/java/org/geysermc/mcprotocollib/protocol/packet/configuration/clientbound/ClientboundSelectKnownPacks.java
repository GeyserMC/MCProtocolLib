package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.KnownPack;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundSelectKnownPacks implements MinecraftPacket {
    private final List<KnownPack> knownPacks;

    public ClientboundSelectKnownPacks(MinecraftByteBuf buf) {
        this.knownPacks = new ArrayList<>();
        int entryCount = buf.readVarInt();
        for (int i = 0; i < entryCount; i++) {
            this.knownPacks.add(new KnownPack(buf.readString(), buf.readString(), buf.readString()));
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.knownPacks.size());
        for (KnownPack entry : this.knownPacks) {
            buf.writeString(entry.getNamespace());
            buf.writeString(entry.getId());
            buf.writeString(entry.getVersion());
        }
    }
}
