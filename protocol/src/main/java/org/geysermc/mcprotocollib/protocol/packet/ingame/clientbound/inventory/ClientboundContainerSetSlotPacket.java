package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

@Data
@With
@AllArgsConstructor
public class ClientboundContainerSetSlotPacket implements MinecraftPacket {
    private final int containerId;
    private final int stateId;
    private final int slot;
    private final @Nullable ItemStack item;

    public ClientboundContainerSetSlotPacket(ByteBuf in) {
        this.containerId = MinecraftTypes.readVarInt(in);
        this.stateId = MinecraftTypes.readVarInt(in);
        this.slot = in.readShort();
        this.item = MinecraftTypes.readOptionalItemStack(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.containerId);
        MinecraftTypes.writeVarInt(out, this.stateId);
        out.writeShort(this.slot);
        MinecraftTypes.writeOptionalItemStack(out, this.item);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
