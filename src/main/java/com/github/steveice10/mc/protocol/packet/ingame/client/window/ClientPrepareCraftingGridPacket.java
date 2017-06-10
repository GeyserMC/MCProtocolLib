package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.window.PrepareCraftingGridEntry;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.mc.protocol.util.ReflectionToString;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientPrepareCraftingGridPacket implements Packet {

    private int windowId;
    private int actionId;
    private List<PrepareCraftingGridEntry> returnEntries;
    private List<PrepareCraftingGridEntry> prepareEntries;

    @SuppressWarnings("unused")
    private ClientPrepareCraftingGridPacket() {
    }

    public ClientPrepareCraftingGridPacket(int windowId, int actionId, List<PrepareCraftingGridEntry> returnEntries,
                                           List<PrepareCraftingGridEntry> prepareEntries) {
        this.windowId = windowId;
        this.actionId = actionId;
        this.returnEntries = returnEntries;
        this.prepareEntries = prepareEntries;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getActionId() {
        return this.actionId;
    }

    public List<PrepareCraftingGridEntry> getReturnEntries() {
        return this.returnEntries;
    }

    public List<PrepareCraftingGridEntry> getPrepareEntries() {
        return this.prepareEntries;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readByte();
        this.actionId = in.readShort();
        this.returnEntries = readEntries(in);
        this.prepareEntries = readEntries(in);
    }

    private static List<PrepareCraftingGridEntry> readEntries(NetInput in) throws IOException {
        List<PrepareCraftingGridEntry> entries = new ArrayList<>();
        for (int i = in.readShort(); i > 0; i--) {
            ItemStack item = NetUtil.readItem(in);
            byte craftingGridSlot = in.readByte();
            byte playerInventorySlot = in.readByte();
            entries.add(new PrepareCraftingGridEntry(item, craftingGridSlot, playerInventorySlot));
        }
        return entries;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(actionId);
        writeEntries(out, this.returnEntries);
        writeEntries(out, this.prepareEntries);
    }

    private static void writeEntries(NetOutput out, List<PrepareCraftingGridEntry> entries) throws IOException {
        out.writeShort(entries.size());
        for (PrepareCraftingGridEntry entry : entries) {
            NetUtil.writeItem(out, entry.getItem());
            out.writeByte(entry.getCraftingGridSlot());
            out.writeByte(entry.getPlayerInventorySlot());
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }

    @Override
    public String toString() {
        return ReflectionToString.toString(this);
    }
}
