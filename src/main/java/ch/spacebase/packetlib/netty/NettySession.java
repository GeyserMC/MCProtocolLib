package ch.spacebase.packetlib.netty;

import java.util.ArrayList;
import java.util.List;

import ch.spacebase.packetlib.Session;
import ch.spacebase.packetlib.event.session.ConnectedEvent;
import ch.spacebase.packetlib.event.session.DisconnectedEvent;
import ch.spacebase.packetlib.event.session.DisconnectingEvent;
import ch.spacebase.packetlib.event.session.PacketReceivedEvent;
import ch.spacebase.packetlib.event.session.PacketSentEvent;
import ch.spacebase.packetlib.event.session.SessionEvent;
import ch.spacebase.packetlib.event.session.SessionListener;
import ch.spacebase.packetlib.packet.Packet;
import ch.spacebase.packetlib.packet.PacketProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettySession extends SimpleChannelInboundHandler<Packet> implements Session {
	
	private String host;
	private int port;
	private PacketProtocol protocol;
	private EventLoopGroup group;
	private Channel channel;
	private boolean disconnected;
	
	private List<SessionListener> listeners = new ArrayList<SessionListener>();
	
	public NettySession(String host, int port, PacketProtocol protocol, EventLoopGroup group) {
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.group = group;
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
	public boolean isConnected() {
		return this.channel != null && this.channel.isOpen();
	}
	
	@Override
	public void send(Packet packet) {
		if(this.channel == null) {
			return;
		}
		
		this.channel.writeAndFlush(packet);
		this.callEvent(new PacketSentEvent(this, packet));
	}
	
	@Override
	public void disconnect(String reason) {
		if(reason == null) {
			reason = "Connection closed.";
		}
		
		if(this.channel != null) {
			this.callEvent(new DisconnectingEvent(this, reason));
			this.channel.close().syncUninterruptibly();
			this.callEvent(new DisconnectedEvent(this, reason));
		}
		
		if(this.group != null) {
			this.group.shutdownGracefully();
		}
		
		this.disconnected = true;
	}
	
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	if(this.disconnected) {
    		ctx.channel().close().syncUninterruptibly();
    		return;
    	}
    	
    	this.channel = ctx.channel();
    	this.callEvent(new ConnectedEvent(this));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	if(!this.disconnected) {
    		this.disconnect("Connection closed.");
    	}
    	
    	this.channel = null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	if(!this.disconnected) {
    		this.disconnect("Internal exception: " + cause);
    		this.channel = null;
    		System.err.println("Networking exception occured: " + cause);
    		cause.printStackTrace();
    	}
    }

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {
		this.callEvent(new PacketReceivedEvent(this, packet));
	}

}
