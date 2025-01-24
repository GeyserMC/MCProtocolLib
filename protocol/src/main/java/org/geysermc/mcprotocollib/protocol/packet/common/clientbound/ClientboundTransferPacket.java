package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundTransferPacket implements MinecraftPacket {
    private final String host;
    private final int port;

    public ClientboundTransferPacket(ByteBuf in) {
        this.host = MinecraftTypes.readString(in);
        this.port = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeString(out, this.host);
        MinecraftTypes.writeVarInt(out, this.port);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        // GAME THREAD DETAIL: Code runs before packet is made async.
        return false; // False, you need to handle making it async yourself
    }
}
