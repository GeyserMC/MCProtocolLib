package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public class TcpServerSession extends TcpSession {
    private Server server;

    public TcpServerSession(String host, int port, PacketProtocol protocol, Server server) {
        super(host, port, protocol);
        this.server = server;
    }

    @Override
    public Map<String, Object> getFlags() {
        Map<String, Object> ret = super.getFlags();
        ret.putAll(this.server.getGlobalFlags());
        return ret;
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
