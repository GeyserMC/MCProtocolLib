package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
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

    public ServerboundChatCommandSignedPacket(ByteBuf in) {
        this.command = MinecraftTypes.readString(in);
        this.timeStamp = in.readLong();
        this.salt = in.readLong();
        this.signatures = new ArrayList<>();
        int signatureCount = Math.min(MinecraftTypes.readVarInt(in), 8);
        for (int i = 0; i < signatureCount; i++) {
            byte[] signature = new byte[256];
            signatures.add(new ArgumentSignature(MinecraftTypes.readString(in, 16), signature));
            in.readBytes(signature);
        }

        this.offset = MinecraftTypes.readVarInt(in);
        this.acknowledgedMessages = MinecraftTypes.readFixedBitSet(in, 20);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeString(out, this.command);
        out.writeLong(this.timeStamp);
        out.writeLong(this.salt);
        MinecraftTypes.writeVarInt(out, this.signatures.size());
        for (ArgumentSignature signature : this.signatures) {
            MinecraftTypes.writeString(out, signature.getName());
            out.writeBytes(signature.getSignature());
        }

        MinecraftTypes.writeVarInt(out, this.offset);
        MinecraftTypes.writeFixedBitSet(out, this.acknowledgedMessages, 20);
    }
}
