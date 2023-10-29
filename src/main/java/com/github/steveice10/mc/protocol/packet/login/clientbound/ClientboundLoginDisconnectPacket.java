package com.github.steveice10.mc.protocol.packet.login.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@Data
@With
@AllArgsConstructor
public class ClientboundLoginDisconnectPacket implements MinecraftPacket {
    private final @NotNull Component reason;

    public ClientboundLoginDisconnectPacket(String text) {
        this(DefaultComponentSerializer.get().deserialize(text));
    }

    public ClientboundLoginDisconnectPacket(ByteBuf in, MinecraftCodecHelper codecHelper) {
        this.reason = DefaultComponentSerializer.get().deserialize(codecHelper.readString(in));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeComponent(out, this.reason);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
