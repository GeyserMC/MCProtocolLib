package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
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

    public ClientboundContainerSetSlotPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.containerId = in.readUnsignedByte();
        this.stateId = helper.readVarInt(in);
        this.slot = in.readShort();
        this.item = helper.readOptionalItemStack(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeByte(this.containerId);
        helper.writeVarInt(out, this.stateId);
        out.writeShort(this.slot);
        helper.writeOptionalItemStack(out, this.item);
    }
}
