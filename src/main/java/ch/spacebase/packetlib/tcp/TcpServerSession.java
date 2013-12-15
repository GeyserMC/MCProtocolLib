package ch.spacebase.packetlib.tcp;

import java.util.Map;

import ch.spacebase.packetlib.Server;
import ch.spacebase.packetlib.packet.PacketProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;

public class TcpServerSession extends TcpSession {

	private Server server;
	
	public TcpServerSession(String host, int port, PacketProtocol protocol, EventLoopGroup group, Bootstrap bootstrap, Server server) {
		super(host, port, protocol, group, bootstrap);
		this.server = server;
	}
	
	@Override
	public void connect() {
	}
	
	@Override
	public Map<String, Object> getFlags() {
		Map<String, Object> ret = super.getFlags();
		ret.putAll(this.server.getGlobalFlags());
		return ret;
	}
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	super.channelInactive(ctx);
    	this.server.removeSession(this);
    }

}
