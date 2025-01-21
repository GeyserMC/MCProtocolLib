package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

@Data
@With
@AllArgsConstructor
public class ClientboundSetPlayerInventoryPacket implements MinecraftPacket {
    private final int slot;
    private final ItemStack contents;

    public ClientboundSetPlayerInventoryPacket(ByteBuf in) {
        this.slot = MinecraftTypes.readVarInt(in);
        this.contents = MinecraftTypes.readOptionalItemStack(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.slot);
        MinecraftTypes.writeOptionalItemStack(out, this.contents);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
