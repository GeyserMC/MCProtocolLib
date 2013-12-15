package ch.spacebase.packetlib.tcp;

import java.util.List;

import ch.spacebase.packetlib.Session;
import ch.spacebase.packetlib.event.session.PacketReceivedEvent;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;
import ch.spacebase.packetlib.tcp.io.ByteBufNetInput;
import ch.spacebase.packetlib.tcp.io.ByteBufNetOutput;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

public class TcpPacketCodec extends ByteToMessageCodec<Packet> {

	private Session session;
	
	public TcpPacketCodec(Session session) {
		this.session = session;
	}
	
	@Override
	public void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf buf) throws Exception {
		NetOutput out = new ByteBufNetOutput(buf);
		out.writeVarInt(this.session.getPacketProtocol().getOutgoingId(packet.getClass()));
		packet.write(out);
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		NetInput in = new ByteBufNetInput(buf);
		int id = in.readVarInt();
		Packet packet = this.session.getPacketProtocol().createIncomingPacket(id);
		System.out.println(packet.getClass());
		packet.read(in);
		if(packet.isPriority()) {
			this.session.callEvent(new PacketReceivedEvent(this.session, packet));
		}
		
		out.add(packet);
	}
	
}
