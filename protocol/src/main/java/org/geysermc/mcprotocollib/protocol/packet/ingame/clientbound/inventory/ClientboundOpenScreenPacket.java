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
    /**
     * The menu type. {@code null} when the server sent a modded id outside
     * the vanilla {@link ContainerType} range; {@link #rawTypeId} still
     * carries the exact id that was sent.
     */
    private final ContainerType type;
    /**
     * The raw menu type id as sent over the wire. Always valid, even when
     * {@link #type} is {@code null} (modded servers), so logging callers can
     * report which menu type was sent and re-encoding round-trips exactly.
     */
    private final int rawTypeId;
    private final @NonNull Component title;

    public ClientboundOpenScreenPacket(ByteBuf in) {
        this.containerId = MinecraftTypes.readVarInt(in);
        this.rawTypeId = MinecraftTypes.readVarInt(in);
        this.type = ContainerType.from(this.rawTypeId);
        this.title = MinecraftTypes.readComponent(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.containerId);
        MinecraftTypes.writeVarInt(out, this.rawTypeId);
        MinecraftTypes.writeComponent(out, this.title);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
