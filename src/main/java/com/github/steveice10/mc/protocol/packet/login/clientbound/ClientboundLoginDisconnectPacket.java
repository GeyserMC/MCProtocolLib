package com.github.steveice10.mc.protocol.packet.login.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
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
public class ClientboundLoginDisconnectPacket implements MinecraftPacket {
    private final @NonNull Component reason;

    public ClientboundLoginDisconnectPacket(String text) {
        this(DefaultComponentSerializer.get().deserialize(text));
    }

    public ClientboundLoginDisconnectPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.reason = helper.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeComponent(out, this.reason);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
