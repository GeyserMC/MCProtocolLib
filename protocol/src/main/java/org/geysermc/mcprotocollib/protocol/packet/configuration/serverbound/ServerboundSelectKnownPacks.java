package org.geysermc.mcprotocollib.protocol.packet.configuration.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.KnownPack;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ServerboundSelectKnownPacks implements MinecraftPacket {
    private final List<KnownPack> knownPacks;

    public ServerboundSelectKnownPacks(ByteBuf in, MinecraftCodecHelper helper) {
        this.knownPacks = new ArrayList<>();

        int entryCount = Math.min(helper.readVarInt(in), 64);
        for (int i = 0; i < entryCount; i++) {
            this.knownPacks.add(new KnownPack(helper.readString(in), helper.readString(in), helper.readString(in)));
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        if (this.knownPacks.size() > 64) {
            throw new IllegalArgumentException("KnownPacks is longer than maximum allowed length");
        }

        helper.writeVarInt(out, this.knownPacks.size());
        for (KnownPack entry : this.knownPacks) {
            helper.writeString(out, entry.getNamespace());
            helper.writeString(out, entry.getId());
            helper.writeString(out, entry.getVersion());
        }
    }
}
