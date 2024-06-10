package org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateEnabledFeaturesPacket implements MinecraftPacket {
    private final Key[] features;

    public ClientboundUpdateEnabledFeaturesPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.features = new Key[helper.readVarInt(in)];
        for (int i = 0; i < this.features.length; i++) {
            this.features[i] = helper.readResourceLocation(in);
        }
    }

    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.features.length);
        for (Key feature : this.features) {
            helper.writeResourceLocation(out, feature);
        }
    }
}
