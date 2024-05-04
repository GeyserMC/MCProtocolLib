package org.geysermc.mcprotocollib.protocol.packet.login.serverbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundCustomQueryAnswerPacket implements MinecraftPacket {
    private final int transactionId;
    private final byte @Nullable [] data;

    public ServerboundCustomQueryAnswerPacket(int transactionId) {
        this(transactionId, new byte[0]);
    }

    public ServerboundCustomQueryAnswerPacket(MinecraftByteBuf buf) {
        this.transactionId = buf.readVarInt();
        this.data = buf.readNullable(() -> buf.readByteArray(buf::readableBytes));
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.transactionId);
        buf.writeNullable(this.data, buf::writeBytes);
    }
}
