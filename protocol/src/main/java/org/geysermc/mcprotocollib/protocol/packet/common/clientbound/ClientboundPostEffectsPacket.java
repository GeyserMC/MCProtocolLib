package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundPostEffectsPacket implements MinecraftPacket {
    private final List<Key> postEffects;

    public ClientboundPostEffectsPacket(ByteBuf in) {
        this.postEffects = MinecraftTypes.readList(in, MinecraftTypes::readResourceLocation);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeList(out, this.postEffects, MinecraftTypes::writeResourceLocation);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
