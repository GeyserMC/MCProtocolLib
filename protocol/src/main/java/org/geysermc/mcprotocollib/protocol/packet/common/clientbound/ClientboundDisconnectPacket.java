package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

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
public class ClientboundDisconnectPacket implements MinecraftPacket {
    private final @NonNull Component reason;

    public ClientboundDisconnectPacket(@NonNull String reason) {
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
