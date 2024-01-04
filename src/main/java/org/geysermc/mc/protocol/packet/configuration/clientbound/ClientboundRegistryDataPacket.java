package org.geysermc.mc.protocol.packet.configuration.clientbound;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundRegistryDataPacket implements MinecraftPacket {
    private final CompoundTag registry;

    public ClientboundRegistryDataPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.registry = helper.readAnyTag(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeAnyTag(out, this.registry);
    }
}
