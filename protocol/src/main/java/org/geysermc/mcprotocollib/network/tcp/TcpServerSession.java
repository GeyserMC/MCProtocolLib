package org.geysermc.mcprotocollib.network.tcp;

import io.netty.channel.ChannelHandlerContext;
import org.geysermc.mcprotocollib.network.Flag;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class TcpServerSession extends TcpSession {
    private final TcpServer server;
    private final PacketCodecHelper codecHelper;

    public TcpServerSession(String host, int port, PacketProtocol protocol, TcpServer server, Executor packetHandlerExecutor) {
        super(host, port, protocol, packetHandlerExecutor);
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
    public boolean hasFlag(Flag<?> flag) {
        if (super.hasFlag(flag)) {
            return true;
        }

        return this.server.hasGlobalFlag(flag);
    }

    @Override
    public <T> T getFlag(Flag<T> flag, T def) {
        T ret = super.getFlag(flag, null);
        if (ret != null) {
            return ret;
        }

        return this.server.getGlobalFlag(flag, def);
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
