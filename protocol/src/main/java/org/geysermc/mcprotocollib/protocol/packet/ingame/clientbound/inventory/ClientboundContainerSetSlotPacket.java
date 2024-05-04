package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

@Data
@With
@AllArgsConstructor
public class ClientboundContainerSetSlotPacket implements MinecraftPacket {
    private final int containerId;
    private final int stateId;
    private final int slot;
    private final @Nullable ItemStack item;

    public ClientboundContainerSetSlotPacket(MinecraftByteBuf buf) {
        this.containerId = buf.readUnsignedByte();
        this.stateId = buf.readVarInt();
        this.slot = buf.readShort();
        this.item = buf.readOptionalItemStack();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeByte(this.containerId);
        buf.writeVarInt(this.stateId);
        buf.writeShort(this.slot);
        buf.writeOptionalItemStack(this.item);
    }
}
