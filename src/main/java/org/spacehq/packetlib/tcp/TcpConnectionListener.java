package org.spacehq.packetlib.tcp;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import org.spacehq.packetlib.ConnectionListener;

public class TcpConnectionListener implements ConnectionListener {

	private String host;
	private int port;
	private EventLoopGroup group;
	private Channel channel;

	public TcpConnectionListener(String host, int port, EventLoopGroup group, Channel channel) {
		this.host = host;
		this.port = port;
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
