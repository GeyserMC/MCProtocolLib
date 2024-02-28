package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@With
@AllArgsConstructor
public class ClientboundContainerSetContentPacket implements MinecraftPacket {
    private final int containerId;
    private final int stateId;
    private final @Nullable ItemStack @NonNull [] items;
    private final @Nullable ItemStack carriedItem;

    public ClientboundContainerSetContentPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.containerId = in.readUnsignedByte();
        this.stateId = helper.readVarInt(in);
        this.items = new ItemStack[helper.readVarInt(in)];
        for (int index = 0; index < this.items.length; index++) {
            this.items[index] = helper.readOptionalItemStack(in);
        }
        this.carriedItem = helper.readOptionalItemStack(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeByte(this.containerId);
        helper.writeVarInt(out, this.stateId);
        helper.writeVarInt(out, this.items.length);
        for (ItemStack item : this.items) {
            helper.writeOptionalItemStack(out, item);
        }
        helper.writeOptionalItemStack(out, this.carriedItem);
    }
}
