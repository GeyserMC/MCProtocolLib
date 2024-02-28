package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundRegistryDataPacket implements MinecraftPacket {
    private final CompoundTag registry;

    public ClientboundRegistryDataPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.registry = helper.readAnyTag(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeAnyTag(out, this.registry);
    }
}
