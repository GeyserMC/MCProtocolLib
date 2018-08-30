package com.github.steveice10.mc.protocol.packet.ingame.client;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ClientTabCompletePacket extends MinecraftPacket {
    private int transactionId;
    private String text;

    @SuppressWarnings("unused")
    private ClientTabCompletePacket() {
    }

    public ClientTabCompletePacket(int transactionId, String text) {
        this.transactionId = transactionId;
        this.text = text;
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.transactionId = in.readVarInt();
        this.text = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.transactionId);
        out.writeString(this.text);
    }
}
