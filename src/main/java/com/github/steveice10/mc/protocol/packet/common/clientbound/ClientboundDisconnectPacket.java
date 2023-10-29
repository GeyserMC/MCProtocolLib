package com.github.steveice10.mc.protocol.packet.common.clientbound;

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
public class ClientboundDisconnectPacket implements MinecraftPacket {
    private final @NotNull Component reason;

    public ClientboundDisconnectPacket(@NotNull String reason) {
        this(DefaultComponentSerializer.get().deserialize(reason));
    }

    public ClientboundDisconnectPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.reason = helper.readComponent(in);
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
