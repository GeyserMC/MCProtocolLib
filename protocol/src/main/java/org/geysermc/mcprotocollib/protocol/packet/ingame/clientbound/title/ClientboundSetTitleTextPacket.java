package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.title;

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
public class ClientboundSetTitleTextPacket implements MinecraftPacket {
    private final @NonNull Component text;

    public ClientboundSetTitleTextPacket(ByteBuf in) {
        this.text = MinecraftTypes.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeComponent(out, this.text);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
