package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundCodeOfConductPacket implements MinecraftPacket {
    private final String codeOfConduct;

    public ClientboundCodeOfConductPacket(ByteBuf in) {
        this.codeOfConduct = MinecraftTypes.readString(in);
    }

    @Override
    public void serialize(ByteBuf buf) {
        MinecraftTypes.writeString(buf, codeOfConduct);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
