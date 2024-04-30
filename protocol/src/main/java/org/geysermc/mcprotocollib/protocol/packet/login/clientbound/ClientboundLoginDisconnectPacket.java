package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.DefaultComponentSerializer;

@Data
@With
@AllArgsConstructor
public class ClientboundLoginDisconnectPacket implements MinecraftPacket {

    private static final int MAX_COMPONENT_STRING_LENGTH = 262144;

    private final @NonNull Component reason;

    public ClientboundLoginDisconnectPacket(String text) {
        this(DefaultComponentSerializer.get().deserialize(text));
    }

    public ClientboundLoginDisconnectPacket(ByteBuf in, MinecraftCodecHelper helper) {
        // uses the old json serialization rather than the 1.20.3 NBT serialization
        this.reason = DefaultComponentSerializer.get().deserialize(helper.readString(in, MAX_COMPONENT_STRING_LENGTH));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeString(out, DefaultComponentSerializer.get().serialize(reason));
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
