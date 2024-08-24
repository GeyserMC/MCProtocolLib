package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

@Data
@With
@AllArgsConstructor
public class ClientboundSetPlayerInventoryPacket implements MinecraftPacket {
    private final int slot;
    private final ItemStack contents;

    public ClientboundSetPlayerInventoryPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.slot = helper.readVarInt(in);
        this.contents = helper.readOptionalItemStack(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.slot);
        helper.writeOptionalItemStack(out, this.contents);
    }
}
