package ch.spacebase.packetlib.netty;

import ch.spacebase.packetlib.Server;
import ch.spacebase.packetlib.packet.PacketProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;

public class NettyServerSession extends NettySession {

	private Server server;
	
	public NettyServerSession(String host, int port, PacketProtocol protocol, EventLoopGroup group, Server server) {
		super(host, port, protocol, group);
		this.server = server;
	}
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	super.channelInactive(ctx);
    	this.server.removeSession(this);
    }

}
