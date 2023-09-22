package com.github.steveice10.mc.protocol.packet.configuration.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateEnabledFeaturesPacket implements MinecraftPacket {
    private final String[] features;

    public ClientboundUpdateEnabledFeaturesPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.features = new String[helper.readVarInt(in)];
        for (int i = 0; i < this.features.length; i++) {
            this.features[i] = helper.readString(in);
        }
    }

    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.features.length);
        for (String feature : this.features) {
            helper.writeString(out, feature);
        }
    }
}
