package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateEnabledFeaturesPacket implements MinecraftPacket {
    private final Key[] features;

    public ClientboundUpdateEnabledFeaturesPacket(ByteBuf in) {
        this.features = new Key[MinecraftTypes.readVarInt(in)];
        for (int i = 0; i < this.features.length; i++) {
            this.features[i] = MinecraftTypes.readResourceLocation(in);
        }
    }

    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.features.length);
        for (Key feature : this.features) {
            MinecraftTypes.writeResourceLocation(out, feature);
        }
    }
}
