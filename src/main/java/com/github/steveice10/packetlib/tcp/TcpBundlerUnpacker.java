package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundBundlePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundDelimiterPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.ArrayList;
import java.util.List;

public class TcpBundlerUnpacker extends MessageToMessageDecoder<MinecraftPacket> {
    private List<MinecraftPacket> currentPackets;

    @Override
    protected void decode(ChannelHandlerContext ctx, MinecraftPacket packet, List<Object> out) throws Exception {
        if (currentPackets != null) {
            if (packet.getClass() == ClientboundDelimiterPacket.class) {
                out.add(new ClientboundBundlePacket(currentPackets));
                currentPackets = null;
            } else {
                currentPackets.add(packet);
            }
        } else {
            if (packet.getClass() == ClientboundDelimiterPacket.class) {
                currentPackets = new ArrayList<>(2);
            }
        }
    }
}
