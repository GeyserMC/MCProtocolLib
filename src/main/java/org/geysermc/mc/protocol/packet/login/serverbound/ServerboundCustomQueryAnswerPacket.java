package org.geysermc.mc.protocol.packet.login.serverbound;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@With
@AllArgsConstructor
public class ServerboundCustomQueryAnswerPacket implements MinecraftPacket {
    private final int transactionId;
    private final byte @Nullable[] data;

    public ServerboundCustomQueryAnswerPacket(int transactionId) {
        this(transactionId, new byte[0]);
    }

    public ServerboundCustomQueryAnswerPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.transactionId = helper.readVarInt(in);
        this.data = helper.readNullable(in, buf -> helper.readByteArray(buf, ByteBuf::readableBytes));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.transactionId);
        helper.writeNullable(out, this.data, ByteBuf::writeBytes);
    }
}
