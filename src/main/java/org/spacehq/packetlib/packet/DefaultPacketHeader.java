package org.spacehq.packetlib.packet;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;

import java.io.IOException;

public class DefaultPacketHeader implements PacketHeader {

	@Override
	public boolean isLengthVariable() {
		return true;
	}

	@Override
	public int getLengthSize() {
		return 5;
	}

	@Override
	public int getLengthSize(int length) {
		return varintLength(length);
	}

	@Override
	public int readLength(NetInput in, int available) throws IOException {
		return in.readVarInt();
	}

	@Override
	public void writeLength(NetOutput out, int length) throws IOException {
		out.writeVarInt(length);
	}

	@Override
	public int readPacketId(NetInput in) throws IOException {
		return in.readVarInt();
	}

	@Override
	public void writePacketId(NetOutput out, int packetId) throws IOException {
		out.writeVarInt(packetId);
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
