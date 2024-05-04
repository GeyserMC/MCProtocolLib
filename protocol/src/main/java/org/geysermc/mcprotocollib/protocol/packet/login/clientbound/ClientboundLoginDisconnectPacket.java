package org.geysermc.mcprotocollib.protocol.packet.login.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
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

    public ClientboundLoginDisconnectPacket(MinecraftByteBuf buf) {
        // uses the old json serialization rather than the 1.20.3 NBT serialization
        this.reason = DefaultComponentSerializer.get().deserialize(buf.readString(MAX_COMPONENT_STRING_LENGTH));
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(DefaultComponentSerializer.get().serialize(reason));
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
