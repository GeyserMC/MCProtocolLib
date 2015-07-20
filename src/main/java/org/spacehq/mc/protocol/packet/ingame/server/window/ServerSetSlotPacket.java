package org.spacehq.mc.protocol.packet.ingame.server.window;

import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerSetSlotPacket implements Packet {

    private int windowId;
    private int slot;
    private ItemStack item;

    @SuppressWarnings("unused")
    private ServerSetSlotPacket() {
    }

    public ServerSetSlotPacket(int windowId, int slot, ItemStack item) {
        this.windowId = windowId;
        this.slot = slot;
        this.item = item;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readUnsignedByte();
        this.slot = in.readShort();
        this.item = NetUtil.readItem(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.slot);
        NetUtil.writeItem(out, this.item);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
