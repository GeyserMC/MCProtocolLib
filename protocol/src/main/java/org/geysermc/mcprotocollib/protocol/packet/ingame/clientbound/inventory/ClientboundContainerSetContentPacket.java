package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

@Data
@With
@AllArgsConstructor
public class ClientboundContainerSetContentPacket implements MinecraftPacket {
    private final int containerId;
    private final int stateId;
    private final @Nullable ItemStack @NonNull [] items;
    private final @Nullable ItemStack carriedItem;

    public ClientboundContainerSetContentPacket(ByteBuf in) {
        this.containerId = MinecraftTypes.readVarInt(in);
        this.stateId = MinecraftTypes.readVarInt(in);
        this.items = new ItemStack[MinecraftTypes.readVarInt(in)];
        for (int index = 0; index < this.items.length; index++) {
            this.items[index] = MinecraftTypes.readOptionalItemStack(in);
        }
        this.carriedItem = MinecraftTypes.readOptionalItemStack(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.containerId);
        MinecraftTypes.writeVarInt(out, this.stateId);
        MinecraftTypes.writeVarInt(out, this.items.length);
        for (ItemStack item : this.items) {
            MinecraftTypes.writeOptionalItemStack(out, item);
        }
        MinecraftTypes.writeOptionalItemStack(out, this.carriedItem);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
