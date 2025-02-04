package org.geysermc.mcprotocollib.protocol.packet.configuration.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.KnownPack;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ServerboundSelectKnownPacks implements MinecraftPacket {
    private final List<KnownPack> knownPacks;

    public ServerboundSelectKnownPacks(ByteBuf in) {
        this.knownPacks = new ArrayList<>();

        int entryCount = Math.min(MinecraftTypes.readVarInt(in), 64);
        for (int i = 0; i < entryCount; i++) {
            this.knownPacks.add(new KnownPack(MinecraftTypes.readString(in), MinecraftTypes.readString(in), MinecraftTypes.readString(in)));
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        if (this.knownPacks.size() > 64) {
            throw new IllegalArgumentException("KnownPacks is longer than maximum allowed length");
        }

        MinecraftTypes.writeVarInt(out, this.knownPacks.size());
        for (KnownPack entry : this.knownPacks) {
            MinecraftTypes.writeString(out, entry.getNamespace());
            MinecraftTypes.writeString(out, entry.getId());
            MinecraftTypes.writeString(out, entry.getVersion());
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
