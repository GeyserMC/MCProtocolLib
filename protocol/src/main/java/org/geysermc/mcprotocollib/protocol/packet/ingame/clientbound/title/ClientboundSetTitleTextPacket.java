package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.title;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.DefaultComponentSerializer;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@With
@AllArgsConstructor
public class ClientboundSetTitleTextPacket implements MinecraftPacket {
    private final @Nullable Component text;

    public ClientboundSetTitleTextPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.text = helper.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeString(out, DefaultComponentSerializer.get().serializeOr(this.text, "null"));
    }
}
