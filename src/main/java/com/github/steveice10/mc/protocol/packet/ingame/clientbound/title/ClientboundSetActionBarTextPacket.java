package com.github.steveice10.mc.protocol.packet.ingame.clientbound.title;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;

@Data
@With
@AllArgsConstructor
public class ClientboundSetActionBarTextPacket implements MinecraftPacket {
    private final Component text;

    public ClientboundSetActionBarTextPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.text = helper.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeComponent(out, this.text);
    }
}
