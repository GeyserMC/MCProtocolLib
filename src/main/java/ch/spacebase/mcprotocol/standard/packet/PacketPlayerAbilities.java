package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.Util;

public class PacketPlayerAbilities extends Packet {

	public boolean god;
	public boolean flying;
	public boolean canFly;
	public boolean creative;
	public float flySpeed;
	public float walkSpeed;

	public PacketPlayerAbilities() {
	}

	public PacketPlayerAbilities(boolean god, boolean flying, boolean canFly, boolean creative, float flySpeed, float walkSpeed) {
		this.god = god;
		this.flying = flying;
		this.canFly = canFly;
		this.creative = creative;
		this.flySpeed = flySpeed;
		this.walkSpeed = walkSpeed;
	}

	@Override
	public void read(NetInput in) throws IOException {
		byte flags = in.readByte();
		this.god = Util.getBit(flags, 0x1);
		this.flying = Util.getBit(flags, 0x2);
		this.canFly = Util.getBit(flags, 0x4);
		this.creative = Util.getBit(flags, 0x8);
		this.flySpeed = in.readFloat();
		this.walkSpeed = in.readFloat();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		byte flags = 0;
		flags = Util.setBit(flags, 0x1, this.god);
		flags = Util.setBit(flags, 0x2, this.flying);
		flags = Util.setBit(flags, 0x4, this.canFly);
		flags = Util.setBit(flags, 0x8, this.creative);
		out.writeByte(flags);
		out.writeFloat(this.flySpeed);
		out.writeFloat(this.walkSpeed);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 202;
	}

}
