package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

@Data
@With
@AllArgsConstructor
public class ClientboundContainerSetContentPacket implements MinecraftPacket {
    private final int containerId;
    private final int stateId;
    private final @Nullable ItemStack @NonNull [] items;
    private final @Nullable ItemStack carriedItem;

    public ClientboundContainerSetContentPacket(MinecraftByteBuf buf) {
        this.containerId = buf.readUnsignedByte();
        this.stateId = buf.readVarInt();
        this.items = new ItemStack[buf.readVarInt()];
        for (int index = 0; index < this.items.length; index++) {
            this.items[index] = buf.readOptionalItemStack();
        }
        this.carriedItem = buf.readOptionalItemStack();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeByte(this.containerId);
        buf.writeVarInt(this.stateId);
        buf.writeVarInt(this.items.length);
        for (ItemStack item : this.items) {
            buf.writeOptionalItemStack(item);
        }
        buf.writeOptionalItemStack(this.carriedItem);
    }
}
