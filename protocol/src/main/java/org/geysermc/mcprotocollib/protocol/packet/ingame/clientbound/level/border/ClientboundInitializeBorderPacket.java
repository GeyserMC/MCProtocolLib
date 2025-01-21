package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.border;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

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

    public ClientboundInitializeBorderPacket(ByteBuf in) {
        this.newCenterX = in.readDouble();
        this.newCenterZ = in.readDouble();
        this.oldSize = in.readDouble();
        this.newSize = in.readDouble();
        this.lerpTime = MinecraftTypes.readVarLong(in);
        this.newAbsoluteMaxSize = MinecraftTypes.readVarInt(in);
        this.warningBlocks = MinecraftTypes.readVarInt(in);
        this.warningTime = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeDouble(this.newCenterX);
        out.writeDouble(this.newCenterZ);
        out.writeDouble(this.oldSize);
        out.writeDouble(this.newSize);
        MinecraftTypes.writeVarLong(out, this.lerpTime);
        MinecraftTypes.writeVarInt(out, this.newAbsoluteMaxSize);
        MinecraftTypes.writeVarInt(out, this.warningBlocks);
        MinecraftTypes.writeVarInt(out, this.warningTime);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
