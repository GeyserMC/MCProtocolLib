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

    private static final int MAX_COMPONENT_STRING_LENGTH = 262144;

    private final @NonNull Component reason;

    public ClientboundLoginDisconnectPacket(String text) {
        this(DefaultComponentSerializer.get().deserialize(text));
    }

    public ClientboundLoginDisconnectPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        // uses the old json serialization rather than the 1.20.3 NBT serialization
        this.reason = DefaultComponentSerializer.get().deserialize(helper.readString(in, MAX_COMPONENT_STRING_LENGTH));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeString(out, DefaultComponentSerializer.get().serialize(reason));
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
