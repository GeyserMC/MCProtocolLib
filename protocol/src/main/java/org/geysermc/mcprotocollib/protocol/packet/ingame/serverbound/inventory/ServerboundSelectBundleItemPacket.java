package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundSelectBundleItemPacket implements MinecraftPacket {
    private final int slotId;
    private final int selectedItemIndex;

    public ServerboundSelectBundleItemPacket(ByteBuf in) {
        this.slotId = MinecraftTypes.readVarInt(in);
        this.selectedItemIndex = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.slotId);
        MinecraftTypes.writeVarInt(out, this.selectedItemIndex);
    }
}
