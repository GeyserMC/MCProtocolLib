package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@Data
@With
@AllArgsConstructor
public class ClientboundTabListPacket implements MinecraftPacket {
    private final @NotNull Component header;
    private final @NotNull Component footer;

    public ClientboundTabListPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.header = helper.readComponent(in);
        this.footer = helper.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeComponent(out, this.header);
        helper.writeComponent(out, this.footer);
    }
}
