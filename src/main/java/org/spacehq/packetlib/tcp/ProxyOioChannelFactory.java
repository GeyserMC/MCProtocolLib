package org.spacehq.packetlib.tcp;

import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.oio.OioSocketChannel;

import java.net.Proxy;
import java.net.Socket;

public class ProxyOioChannelFactory implements ChannelFactory<OioSocketChannel> {

	private Proxy proxy;

	public ProxyOioChannelFactory(Proxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public OioSocketChannel newChannel(EventLoop eventLoop) {
		return new OioSocketChannel(eventLoop, new Socket(this.proxy));
	}

}
