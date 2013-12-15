package ch.spacebase.packetlib.tcp;

import java.util.List;

import ch.spacebase.packetlib.tcp.io.ByteBufNetInput;
import ch.spacebase.packetlib.tcp.io.ByteBufNetOutput;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.CorruptedFrameException;

public class TcpPacketSizer extends ByteToMessageCodec<ByteBuf> {

	@Override
	public void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
		int length = in.readableBytes();
		out.ensureWritable(varintLength(length) + length);
		new ByteBufNetOutput(out).writeVarInt(length);
		out.writeBytes(in);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		buf.markReaderIndex();
		byte[] lengthBytes = new byte[5];
		for(int index = 0; index < lengthBytes.length; index++) {
			if(!buf.isReadable()) {
				buf.resetReaderIndex();
				return;
			}

			lengthBytes[index] = buf.readByte();
			if(lengthBytes[index] >= 0) {
				int length = new ByteBufNetInput(Unpooled.wrappedBuffer(lengthBytes)).readVarInt();
				if(buf.readableBytes() < length) {
					buf.resetReaderIndex();
					return;
				}

				out.add(buf.readBytes(length));
				return;
			}
		}

		throw new CorruptedFrameException("Length is too long.");
	}
	
	private static int varintLength(int i) {
		if((i & -128) == 0) {
			return 1;
		} else if((i & -16384) == 0) {
			return 2;
		} else if((i & -2097152) == 0) {
			return 3;
		} else if((i & -268435456) == 0) {
			return 4;
		} else {
			return 5;
		}
	}

}
