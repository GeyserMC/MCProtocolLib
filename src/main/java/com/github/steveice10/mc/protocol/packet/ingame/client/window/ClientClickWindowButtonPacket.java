package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ClientClickWindowButtonPacket extends MinecraftPacket {

    private int windowId;
    private int buttonId;

    @SuppressWarnings("unused")
    private ClientClickWindowButtonPacket() {
    }

    public ClientClickWindowButtonPacket(int windowId, int enchantment) {
        this.windowId = windowId;
        this.buttonId = enchantment;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getEnchantment() {
        return this.buttonId;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readByte();
        this.buttonId = in.readByte();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeByte(this.buttonId);
    }
}
