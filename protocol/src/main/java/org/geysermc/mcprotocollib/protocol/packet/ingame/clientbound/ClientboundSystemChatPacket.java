package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSystemChatPacket implements MinecraftPacket {
    private final Component content;
    private final boolean overlay;

    public ClientboundSystemChatPacket(MinecraftByteBuf buf) {
        this.content = buf.readComponent();
        this.overlay = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeComponent(this.content);
        buf.writeBoolean(this.overlay);
    }
}
