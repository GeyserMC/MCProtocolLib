package ch.spacebase.packetlib.netty;

import java.nio.ByteBuffer;
import java.util.List;

import ch.spacebase.packetlib.packet.Packet;
import ch.spacebase.packetlib.packet.PacketProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

public class PacketCodec extends ByteToMessageCodec<Packet> {

	private PacketProtocol protocol;
	
	public PacketCodec(PacketProtocol protocol) {
		this.protocol = protocol;
	}
	
	@Override
	public void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf buf) throws Exception {
		try {
			ByteBuffer out = ByteBuffer.allocate(packet.getLength() + 1);
			out.put((byte) (packet.getId() & 0xFF));
			packet.write(out);
			out.flip();
			buf.writeBytes(this.protocol.encode(out.array()));
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		try {
			byte array[] = new byte[buf.readableBytes()];
			for(int index = 0; index < array.length; index++) {
				array[index] = buf.readByte();
			}
			
			ByteBuffer in = ByteBuffer.allocate(buf.capacity()).put(this.protocol.decode(array));
			in.flip();
			
			int id = in.get() & 0xFF;
			Packet packet = this.protocol.createPacket(id);
			packet.read(in);
			out.add(packet);
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
}
