package org.spacehq.packetlib.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.handler.timeout.ReadTimeoutException;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.session.*;
import org.spacehq.packetlib.packet.Packet;
import org.spacehq.packetlib.packet.PacketProtocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TcpSession extends SimpleChannelInboundHandler<Packet> implements Session {

	private String host;
	private int port;
	private PacketProtocol protocol;
	private Bootstrap bootstrap;
	private EventLoopGroup group;
	private Channel channel;
	private boolean disconnected = false;
	private boolean writing = false;
	private int compressionThreshold = -1;
	private List<Packet> packets = new ArrayList<Packet>();

	private Map<String, Object> flags = new HashMap<String, Object>();
	private List<SessionListener> listeners = new ArrayList<SessionListener>();

	public TcpSession(String host, int port, PacketProtocol protocol, EventLoopGroup group, Bootstrap bootstrap) {
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.group = group;
		this.bootstrap = bootstrap;
	}

	@Override
	public void connect() {
		this.connect(true);
	}

	@Override
	public void connect(boolean wait) {
		if(this.bootstrap == null) {
			if(!this.disconnected) {
				return;
			} else {
				throw new IllegalStateException("Session disconnected.");
			}
		}

		ChannelFuture future = this.bootstrap.connect();
		this.bootstrap = null;
		if(wait) {
			future.syncUninterruptibly();
			while(this.channel == null && !this.disconnected) {
				try {
					Thread.sleep(5);
				} catch(InterruptedException e) {
				}
			}
		}
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
	public Map<String, Object> getFlags() {
		return new HashMap<String, Object>(this.flags);
	}

	@Override
	public boolean hasFlag(String key) {
		return this.getFlags().containsKey(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getFlag(String key) {
		Object value = this.getFlags().get(key);
		if(value == null) {
			return null;
		}

		try {
			return (T) value;
		} catch(ClassCastException e) {
			throw new IllegalStateException("Tried to get flag \"" + key + "\" as the wrong type. Actual type: " + value.getClass().getName());
		}
	}

	@Override
	public void setFlag(String key, Object value) {
		this.flags.put(key, value);
	}

	@Override
	public List<SessionListener> getListeners() {
		return new ArrayList<SessionListener>(this.listeners);
	}

	@Override
	public void addListener(SessionListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(SessionListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void callEvent(SessionEvent event) {
		for(SessionListener listener : this.listeners) {
			event.call(listener);
		}
	}

	@Override
	public int getCompressionThreshold() {
		return this.compressionThreshold;
	}

	@Override
	public void setCompressionThreshold(int threshold) {
		this.compressionThreshold = threshold;
		if(this.channel != null) {
			if(this.compressionThreshold >= 0) {
				if(this.channel.pipeline().get("compression") == null) {
					this.channel.pipeline().addBefore("codec", "compression", new TcpPacketCompression(this));
				}
			} else if(this.channel.pipeline().get("compression") != null) {
				this.channel.pipeline().remove("compression");
			}
		}
	}

	@Override
	public boolean isConnected() {
		return this.channel != null && this.channel.isOpen() && !this.disconnected;
	}

	@Override
	public void send(final Packet packet) {
		this.writing = true;
		if(this.channel == null) {
			this.writing = false;
			return;
		}

		this.channel.writeAndFlush(packet).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				writing = false;
				if(!future.isSuccess()) {
					exceptionCaught(null, future.cause());
				} else {
					callEvent(new PacketSentEvent(TcpSession.this, packet));
				}
			}
		});

		if(packet.isPriority()) {
			while(this.writing) {
				try {
					Thread.sleep(2);
				} catch(InterruptedException e) {
				}
			}
		}
	}

	@Override
	public void disconnect(String reason) {
		if(this.disconnected) {
			return;
		}

		this.disconnected = true;
		if(this.writing) {
			while(this.writing) {
				try {
					Thread.sleep(2);
				} catch(InterruptedException e) {
				}
			}

			try {
				Thread.sleep(250);
			} catch(InterruptedException e) {
			}
		}

		if(reason == null) {
			reason = "Connection closed.";
		}

		try {
			if(this.channel != null) {
				if(this.channel.isOpen()) {
					this.callEvent(new DisconnectingEvent(this, reason));
				}

				this.channel.close().syncUninterruptibly();
			}

			this.callEvent(new DisconnectedEvent(this, reason));
		} catch(Throwable t) {
			System.err.println("[WARNING] Throwable caught while firing disconnect events.");
			t.printStackTrace();
		}

		if(this.group != null) {
			try {
				this.group.shutdownGracefully();
			} catch(Exception e) {
			}
		}

		this.channel = null;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if(this.disconnected) {
			ctx.channel().close().syncUninterruptibly();
			return;
		}

		this.channel = ctx.channel();
		this.disconnected = false;
		this.callEvent(new ConnectedEvent(this));
		new PacketHandleThread().start();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		this.disconnect("Connection closed.");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		this.writing = false;
		if(!this.disconnected) {
			if(cause instanceof ReadTimeoutException) {
				this.disconnect("Connection timed out.");
			} else {
				this.disconnect("Internal network exception: " + cause.toString());
				cause.printStackTrace();
			}
		}

		this.disconnected = true;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {
		if(!packet.isPriority()) {
			this.packets.add(packet);
		}
	}

	private class PacketHandleThread extends Thread {
		@Override
		public void run() {
			try {
				while(!disconnected) {
					while(packets.size() > 0) {
						callEvent(new PacketReceivedEvent(TcpSession.this, packets.remove(0)));
					}

					try {
						Thread.sleep(5);
					} catch(InterruptedException e) {
					}
				}
			} catch(Throwable t) {
				try {
					exceptionCaught(null, t);
				} catch(Exception e) {
					System.err.println("Exception while handling exception!");
					e.printStackTrace();
				}
			}
		}
	}

}
