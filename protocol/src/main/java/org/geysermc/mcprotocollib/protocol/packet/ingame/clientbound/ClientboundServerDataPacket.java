package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundServerDataPacket implements MinecraftPacket {
    private final Component motd;
    private final byte @Nullable [] iconBytes;

    public ClientboundServerDataPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.motd = helper.readComponent(in);
        this.iconBytes = helper.readNullable(in, helper::readByteArray);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeComponent(out, this.motd);
        helper.writeNullable(out, this.iconBytes, helper::writeByteArray);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
