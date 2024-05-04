package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.border;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundInitializeBorderPacket implements MinecraftPacket {
    private final double newCenterX;
    private final double newCenterZ;
    private final double oldSize;
    private final double newSize;
    private final long lerpTime;
    private final int newAbsoluteMaxSize;
    private final int warningBlocks;
    private final int warningTime;

    public ClientboundInitializeBorderPacket(MinecraftByteBuf buf) {
        this.newCenterX = buf.readDouble();
        this.newCenterZ = buf.readDouble();
        this.oldSize = buf.readDouble();
        this.newSize = buf.readDouble();
        this.lerpTime = buf.readVarLong();
        this.newAbsoluteMaxSize = buf.readVarInt();
        this.warningBlocks = buf.readVarInt();
        this.warningTime = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeDouble(this.newCenterX);
        buf.writeDouble(this.newCenterZ);
        buf.writeDouble(this.oldSize);
        buf.writeDouble(this.newSize);
        buf.writeVarLong(this.lerpTime);
        buf.writeVarInt(this.newAbsoluteMaxSize);
        buf.writeVarInt(this.warningBlocks);
        buf.writeVarInt(this.warningTime);
    }
}
