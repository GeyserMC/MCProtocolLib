package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

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
public class ClientboundSelectAdvancementsTabPacket implements MinecraftPacket {
    private final String tabId;

    public ClientboundSelectAdvancementsTabPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        if (in.readBoolean()) {
            this.tabId = helper.readString(in);
        } else {
            this.tabId = null;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        if (this.tabId != null) {
            out.writeBoolean(true);
            helper.writeString(out, this.tabId);
        } else {
            out.writeBoolean(false);
        }
    }
}
