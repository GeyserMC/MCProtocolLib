package ch.spacebase.mc.protocol.packet.ingame.server.window;

import java.io.IOException;

import ch.spacebase.mc.protocol.data.game.ItemStack;
import ch.spacebase.mc.util.NetUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerWindowItemsPacket implements Packet {
	
	private int windowId;
	private ItemStack items[];
	
	@SuppressWarnings("unused")
	private ServerWindowItemsPacket() {
	}
	
	public ServerWindowItemsPacket(int windowId, ItemStack items[]) {
		this.windowId = windowId;
		this.items = items;
	}
	
	public int getWindowId() {
		return this.windowId;
	}
	
	public ItemStack[] getItems() {
		return this.items;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.windowId = in.readUnsignedByte();
		this.items = new ItemStack[in.readShort()];
		for(int index = 0; index < this.items.length; index++) {
			this.items[index] = NetUtil.readItem(in);
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(this.windowId);
		out.writeShort(this.items.length);
		for(ItemStack item : this.items) {
			NetUtil.writeItem(out, item);
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}

}
