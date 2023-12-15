package com.github.steveice10.mc.protocol.packet.ingame.clientbound.title;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundSetSubtitleTextPacket implements MinecraftPacket {
    private final Component text;

    public ClientboundSetSubtitleTextPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.text = helper.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeComponent(out, this.text);
    }
}
