package ch.spacebase.packetlib.tcp;

import java.net.InetSocketAddress;

import ch.spacebase.packetlib.Client;
import ch.spacebase.packetlib.ConnectionListener;
import ch.spacebase.packetlib.Server;
import ch.spacebase.packetlib.Session;
import ch.spacebase.packetlib.SessionFactory;
import ch.spacebase.packetlib.packet.PacketProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class TcpSessionFactory implements SessionFactory {

	@Override
	public Session createClientSession(final Client client) {
		final EventLoopGroup group = new NioEventLoopGroup();
		final Bootstrap bootstrap = new Bootstrap().channel(NioSocketChannel.class);
		final TcpSession session = new TcpSession(client.getHost(), client.getPort(), client.getPacketProtocol(), group, bootstrap);
		bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            public void initChannel(Channel ch) throws Exception {
        		session.getPacketProtocol().newClientSession(client, session);
				ch.config().setOption(ChannelOption.IP_TOS, 0x18);
				ch.config().setOption(ChannelOption.TCP_NODELAY, false);
            	ch.pipeline()
                        .addLast("timer", new ReadTimeoutHandler(30))
                        .addLast("encryption", new TcpPacketEncryptor(session))
                        .addLast("sizer", new TcpPacketSizer())
                        .addLast("codec", new TcpPacketCodec(session))
                        .addLast("manager", session);
            }
        }).group(group).remoteAddress(client.getHost(), client.getPort());
		return session;
		//.connect().syncUninterruptibly().channel().pipeline().get("manager");
	}

	@Override
	public ConnectionListener createServerListener(final Server server) {
		final EventLoopGroup group = new NioEventLoopGroup();
		return new TcpConnectionListener(server.getHost(), server.getPort(), group, new ServerBootstrap().channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<Channel>() {
            @Override
            public void initChannel(Channel ch) throws Exception {
            	InetSocketAddress address = (InetSocketAddress) ch.remoteAddress();
            	PacketProtocol protocol = server.createPacketProtocol();
            	TcpSession session = new TcpServerSession(address.getHostName(), address.getPort(), protocol, null, null, server);
            	session.getPacketProtocol().newServerSession(server, session);
				ch.config().setOption(ChannelOption.IP_TOS, 0x18);
				ch.config().setOption(ChannelOption.TCP_NODELAY, false);
            	ch.pipeline()
                        .addLast("timer", new ReadTimeoutHandler(30))
                        .addLast("encryption", new TcpPacketEncryptor(session))
                        .addLast("sizer", new TcpPacketSizer())
                        .addLast("codec", new TcpPacketCodec(session))
                        .addLast("manager", session);
                server.addSession(session);
            }
        }).group(group).localAddress(server.getHost(), server.getPort()).bind().syncUninterruptibly().channel());
	}

}
