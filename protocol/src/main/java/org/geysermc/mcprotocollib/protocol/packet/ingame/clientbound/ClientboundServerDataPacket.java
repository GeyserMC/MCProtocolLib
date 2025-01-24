package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundServerDataPacket implements MinecraftPacket {
    private final Component motd;
    private final byte @Nullable [] iconBytes;

    public ClientboundServerDataPacket(ByteBuf in) {
        this.motd = MinecraftTypes.readComponent(in);
        this.iconBytes = MinecraftTypes.readNullable(in, MinecraftTypes::readByteArray);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeComponent(out, this.motd);
        MinecraftTypes.writeNullable(out, this.iconBytes, MinecraftTypes::writeByteArray);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
