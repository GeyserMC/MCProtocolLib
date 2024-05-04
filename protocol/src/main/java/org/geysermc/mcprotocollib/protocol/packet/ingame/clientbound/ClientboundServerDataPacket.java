package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundServerDataPacket implements MinecraftPacket {
    private final Component motd;
    private final byte @Nullable [] iconBytes;

    public ClientboundServerDataPacket(MinecraftByteBuf buf) {
        this.motd = buf.readComponent();
        this.iconBytes = buf.readNullable(buf::readByteArray);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeComponent(this.motd);
        buf.writeNullable(this.iconBytes, buf::writeByteArray);
    }
}
