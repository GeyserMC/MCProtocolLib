package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundTabListPacket implements MinecraftPacket {
    private final @NonNull Component header;
    private final @NonNull Component footer;

    public ClientboundTabListPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.header = helper.readComponent(in);
        this.footer = helper.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeComponent(out, this.header);
        helper.writeComponent(out, this.footer);
    }
}
