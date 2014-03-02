package org.spacehq.packetlib.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;
import org.spacehq.packetlib.tcp.io.ByteBufNetInput;
import org.spacehq.packetlib.tcp.io.ByteBufNetOutput;

import java.util.List;

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
		packet.read(in);
		if(packet.isPriority()) {
			this.session.callEvent(new PacketReceivedEvent(this.session, packet));
		}

		out.add(packet);
	}

}
