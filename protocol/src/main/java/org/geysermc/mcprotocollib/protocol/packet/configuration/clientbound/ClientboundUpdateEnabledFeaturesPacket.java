package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateEnabledFeaturesPacket implements MinecraftPacket {
    private final String[] features;

    public ClientboundUpdateEnabledFeaturesPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.features = new String[helper.readVarInt(in)];
        for (int i = 0; i < this.features.length; i++) {
            this.features[i] = helper.readString(in);
        }
    }

    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.features.length);
        for (String feature : this.features) {
            helper.writeString(out, feature);
        }
    }
}
