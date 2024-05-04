package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateEnabledFeaturesPacket implements MinecraftPacket {
    private final String[] features;

    public ClientboundUpdateEnabledFeaturesPacket(MinecraftByteBuf buf) {
        this.features = new String[buf.readVarInt()];
        for (int i = 0; i < this.features.length; i++) {
            this.features[i] = buf.readString();
        }
    }

    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.features.length);
        for (String feature : this.features) {
            buf.writeString(feature);
        }
    }
}
