package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundSystemChatPacket implements MinecraftPacket {
    private final Component content;
    private final boolean overlay;

    public ClientboundSystemChatPacket(ByteBuf in) {
        this.content = MinecraftTypes.readComponent(in);
        this.overlay = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeComponent(out, this.content);
        out.writeBoolean(this.overlay);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
