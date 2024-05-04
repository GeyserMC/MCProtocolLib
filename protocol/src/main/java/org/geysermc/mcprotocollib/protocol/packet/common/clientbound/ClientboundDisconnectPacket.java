package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

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
public class ClientboundDisconnectPacket implements MinecraftPacket {
    private final @NonNull Component reason;

    public ClientboundDisconnectPacket(@NonNull String reason) {
        this(DefaultComponentSerializer.get().deserialize(reason));
    }

    public ClientboundDisconnectPacket(MinecraftByteBuf buf) {
        this.reason = buf.readComponent();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeComponent(this.reason);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
