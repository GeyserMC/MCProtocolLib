package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;

public class PacketCollectItem extends Packet {

	public int collected;
	public int collector;

	public PacketCollectItem() {
	}

	public PacketCollectItem(int collected, int collector) {
		this.collected = collected;
		this.collector = collector;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.collected = in.readInt();
		this.collector = in.readInt();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeInt(this.collected);
		out.writeInt(this.collector);
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 22;
	}
        
        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
