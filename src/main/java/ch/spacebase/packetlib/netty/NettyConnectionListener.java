package ch.spacebase.packetlib.netty;

import ch.spacebase.packetlib.ConnectionListener;
import ch.spacebase.packetlib.packet.PacketProtocol;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

public class NettyConnectionListener implements ConnectionListener {
	
	private String host;
	private int port;
	private PacketProtocol protocol;
	private EventLoopGroup group;
	private Channel channel;
	
	public NettyConnectionListener(String host, int port, PacketProtocol protocol, EventLoopGroup group, Channel channel) {
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.group = group;
		this.channel = channel;
	}
	
	@Override
	public String getHost() {
		return this.host;
	}
	
	@Override
	public int getPort() {
		return this.port;
	}
	
	@Override
	public PacketProtocol getPacketProtocol() {
		return this.protocol;
	}
	
	@Override
	public boolean isListening() {
		return this.channel.isOpen();
	}
	
	@Override
	public void close() {
		if(this.channel.isOpen()) {
			this.channel.close().syncUninterruptibly();
		}
		
		try {
			this.group.shutdownGracefully().sync();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

}
