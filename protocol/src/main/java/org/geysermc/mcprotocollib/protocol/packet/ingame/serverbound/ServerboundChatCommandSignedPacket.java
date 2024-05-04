package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.ArgumentSignature;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ServerboundChatCommandSignedPacket implements MinecraftPacket {
    private final String command;
    private final long timeStamp;
    private final long salt;
    private final List<ArgumentSignature> signatures;
    private final int offset;
    private final BitSet acknowledgedMessages;

    public ServerboundChatCommandSignedPacket(MinecraftByteBuf buf) {
        this.command = buf.readString();
        this.timeStamp = buf.readLong();
        this.salt = buf.readLong();
        this.signatures = new ArrayList<>();
        int signatureCount = Math.min(buf.readVarInt(), 8);
        for (int i = 0; i < signatureCount; i++) {
            byte[] signature = new byte[256];
            signatures.add(new ArgumentSignature(buf.readString(16), signature));
            buf.readBytes(signature);
        }

        this.offset = buf.readVarInt();
        this.acknowledgedMessages = buf.readFixedBitSet(20);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(this.command);
        buf.writeLong(this.timeStamp);
        buf.writeLong(this.salt);
        buf.writeVarInt(this.signatures.size());
        for (ArgumentSignature signature : this.signatures) {
            buf.writeString(signature.getName());
            buf.writeBytes(signature.getSignature());
        }

        buf.writeVarInt(this.offset);
        buf.writeFixedBitSet(this.acknowledgedMessages, 20);
    }
}
