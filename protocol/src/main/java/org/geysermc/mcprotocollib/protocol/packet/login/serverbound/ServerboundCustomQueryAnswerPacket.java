package org.geysermc.mcprotocollib.protocol.packet.login.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundCustomQueryAnswerPacket implements MinecraftPacket {
    private final int transactionId;
    private final byte @Nullable [] data;

    public ServerboundCustomQueryAnswerPacket(int transactionId) {
        this(transactionId, new byte[0]);
    }

    public ServerboundCustomQueryAnswerPacket(ByteBuf in) {
        this.transactionId = MinecraftTypes.readVarInt(in);
        this.data = MinecraftTypes.readNullable(in, buf -> MinecraftTypes.readByteArray(buf, ByteBuf::readableBytes));
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.transactionId);
        MinecraftTypes.writeNullable(out, this.data, ByteBuf::writeBytes);
    }
}
