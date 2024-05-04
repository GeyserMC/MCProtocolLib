package org.geysermc.mcprotocollib.protocol.packet.configuration.serverbound;

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
public class ServerboundSelectKnownPacks implements MinecraftPacket {
    private final List<KnownPack> knownPacks;

    public ServerboundSelectKnownPacks(MinecraftByteBuf buf) {
        this.knownPacks = new ArrayList<>();

        int entryCount = Math.min(buf.readVarInt(), 64);
        for (int i = 0; i < entryCount; i++) {
            this.knownPacks.add(new KnownPack(buf.readString(), buf.readString(), buf.readString()));
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        if (this.knownPacks.size() > 64) {
            throw new IllegalArgumentException("KnownPacks is longer than maximum allowed length");
        }

        buf.writeVarInt(this.knownPacks.size());
        for (KnownPack entry : this.knownPacks) {
            buf.writeString(entry.getNamespace());
            buf.writeString(entry.getId());
            buf.writeString(entry.getVersion());
        }
    }
}
