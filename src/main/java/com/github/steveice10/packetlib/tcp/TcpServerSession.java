package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.codec.PacketCodecHelper;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class TcpServerSession extends TcpSession {
    private final TcpServer server;
    private final PacketCodecHelper codecHelper;

    public TcpServerSession(String host, int port, PacketProtocol protocol, TcpServer server) {
        super(host, port, protocol);
        this.server = server;
        this.codecHelper = protocol.createHelper();
    }

    @Override
    public PacketCodecHelper getCodecHelper() {
        return this.codecHelper;
    }

    @Override
    public Map<String, Object> getFlags() {
        Map<String, Object> ret = new HashMap<>();
        ret.putAll(this.server.getGlobalFlags());
        ret.putAll(super.getFlags());
        return ret;
    }

    @Override
    public boolean hasFlag(String key) {
        if(super.hasFlag(key)) {
            return true;
        }

        return this.server.hasGlobalFlag(key);
    }

    @Override
    public <T> T getFlag(String key, T def) {
        T ret = super.getFlag(key, null);
        if(ret != null) {
            return ret;
        }

        return this.server.getGlobalFlag(key, def);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        this.server.addSession(this);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        this.server.removeSession(this);
    }
}
