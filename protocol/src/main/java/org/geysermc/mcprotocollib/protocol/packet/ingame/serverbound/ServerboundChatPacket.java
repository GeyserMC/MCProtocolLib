package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.BitSet;

@Data
@With
@AllArgsConstructor
public class ServerboundChatPacket implements MinecraftPacket {
    private final @NonNull String message;
    private final long timeStamp;
    private final long salt;
    private final byte @Nullable [] signature;
    private final int offset;
    private final BitSet acknowledgedMessages;

    public ServerboundChatPacket(MinecraftByteBuf buf) {
        this.message = buf.readString();
        this.timeStamp = buf.readLong();
        this.salt = buf.readLong();
        if (buf.readBoolean()) {
            this.signature = new byte[256];
            buf.readBytes(this.signature);
        } else {
            this.signature = null;
        }

        this.offset = buf.readVarInt();
        this.acknowledgedMessages = buf.readFixedBitSet(20);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(this.message);
        buf.writeLong(this.timeStamp);
        buf.writeLong(this.salt);
        buf.writeBoolean(this.signature != null);
        if (this.signature != null) {
            buf.writeBytes(this.signature);
        }

        buf.writeVarInt(this.offset);
        buf.writeFixedBitSet(this.acknowledgedMessages, 20);
    }
}
