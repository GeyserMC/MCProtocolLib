package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.border;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
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

    public ClientboundInitializeBorderPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.newCenterX = in.readDouble();
        this.newCenterZ = in.readDouble();
        this.oldSize = in.readDouble();
        this.newSize = in.readDouble();
        this.lerpTime = helper.readVarLong(in);
        this.newAbsoluteMaxSize = helper.readVarInt(in);
        this.warningBlocks = helper.readVarInt(in);
        this.warningTime = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeDouble(this.newCenterX);
        out.writeDouble(this.newCenterZ);
        out.writeDouble(this.oldSize);
        out.writeDouble(this.newSize);
        helper.writeVarLong(out, this.lerpTime);
        helper.writeVarInt(out, this.newAbsoluteMaxSize);
        helper.writeVarInt(out, this.warningBlocks);
        helper.writeVarInt(out, this.warningTime);
    }
}
