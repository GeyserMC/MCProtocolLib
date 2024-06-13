package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundHorseScreenOpenPacket implements MinecraftPacket {
    private final int containerId;
    private final int inventoryColumns;
    private final int entityId;

    public ClientboundHorseScreenOpenPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.containerId = in.readByte();
        this.inventoryColumns = helper.readVarInt(in);
        this.entityId = in.readInt();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeByte(this.containerId);
        helper.writeVarInt(out, this.inventoryColumns);
        out.writeInt(this.entityId);
    }
}
