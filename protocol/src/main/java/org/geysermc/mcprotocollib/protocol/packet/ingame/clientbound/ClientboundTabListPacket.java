package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundTabListPacket implements MinecraftPacket {
    private final @NonNull Component header;
    private final @NonNull Component footer;

    public ClientboundTabListPacket(MinecraftByteBuf buf) {
        this.header = buf.readComponent();
        this.footer = buf.readComponent();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeComponent(this.header);
        buf.writeComponent(this.footer);
    }
}
