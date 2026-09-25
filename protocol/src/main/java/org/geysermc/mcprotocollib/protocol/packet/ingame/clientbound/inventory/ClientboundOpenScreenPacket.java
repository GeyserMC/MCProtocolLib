package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.ContainerType;

@Data
@With
@AllArgsConstructor
public class ClientboundOpenScreenPacket implements MinecraftPacket {
    private final int containerId;
    private final @NonNull ContainerType type;
    private final @NonNull Component title;

    public ClientboundOpenScreenPacket(ByteBuf in) {
        this.containerId = MinecraftTypes.readVarInt(in);
        this.type = ContainerType.from(MinecraftTypes.readVarInt(in));
        this.title = MinecraftTypes.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.containerId);
        MinecraftTypes.writeVarInt(out, this.type.ordinal());
        MinecraftTypes.writeComponent(out, this.title);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
