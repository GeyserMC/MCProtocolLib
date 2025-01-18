package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundTabListPacket implements MinecraftPacket {
    private final @NonNull Component header;
    private final @NonNull Component footer;

    public ClientboundTabListPacket(ByteBuf in) {
        this.header = MinecraftTypes.readComponent(in);
        this.footer = MinecraftTypes.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeComponent(out, this.header);
        MinecraftTypes.writeComponent(out, this.footer);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
