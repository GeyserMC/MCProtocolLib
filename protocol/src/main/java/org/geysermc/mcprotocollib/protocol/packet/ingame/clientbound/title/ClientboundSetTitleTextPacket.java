package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.title;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetTitleTextPacket implements MinecraftPacket {
    private final @NonNull Component text;

    public ClientboundSetTitleTextPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.text = helper.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeComponent(out, this.text);
    }
}
