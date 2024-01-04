package org.geysermc.mc.protocol.packet.ingame.clientbound;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundSystemChatPacket implements MinecraftPacket {
    private final Component content;
    private final boolean overlay;

    public ClientboundSystemChatPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.content = helper.readComponent(in);
        this.overlay = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeComponent(out, this.content);
        out.writeBoolean(this.overlay);
    }
}
