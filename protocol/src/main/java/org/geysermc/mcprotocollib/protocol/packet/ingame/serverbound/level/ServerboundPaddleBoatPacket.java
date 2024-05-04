package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundPaddleBoatPacket implements MinecraftPacket {
    private final boolean rightPaddleTurning;
    private final boolean leftPaddleTurning;

    public ServerboundPaddleBoatPacket(MinecraftByteBuf buf) {
        this.rightPaddleTurning = buf.readBoolean();
        this.leftPaddleTurning = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeBoolean(this.rightPaddleTurning);
        buf.writeBoolean(this.leftPaddleTurning);
    }
}
