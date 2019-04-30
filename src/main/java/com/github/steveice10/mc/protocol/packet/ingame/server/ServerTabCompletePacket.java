package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ServerTabCompletePacket extends MinecraftPacket {
    private int transactionId;
    private int start;
    private int length;
    private String matches[];
    private Message tooltips[];

    @SuppressWarnings("unused")
    private ServerTabCompletePacket() {
    }

    public ServerTabCompletePacket(int transactionId, int start, int length, String matches[], Message tooltips[]) {
        if (tooltips.length != matches.length) {
            throw new IllegalArgumentException("Length of matches and tooltips must be equal.");
        }
        this.transactionId = transactionId;
        this.start = start;
        this.length = length;
        this.matches = matches;
        this.tooltips = tooltips;
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public int getStart() {
        return this.start;
    }

    public int getLength() {
        return this.length;
    }

    public String[] getMatches() {
        return this.matches;
    }

    public Message[] getTooltips() {
        return this.tooltips;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.transactionId = in.readVarInt();
        this.start = in.readVarInt();
        this.length = in.readVarInt();
        this.matches = new String[in.readVarInt()];
        this.tooltips = new TextMessage[this.matches.length];
        for(int index = 0; index < this.matches.length; index++) {
            this.matches[index] = in.readString();
            if (in.readBoolean()) {
                this.tooltips[index] = Message.fromString(in.readString());
            }
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.transactionId);
        out.writeVarInt(this.start);
        out.writeVarInt(this.length);
        out.writeVarInt(this.matches.length);
        for(int index = 0; index < this.matches.length; index++) {
            out.writeString(this.matches[index]);
            Message tooltip = this.tooltips[index];
            if (tooltip != null) {
                out.writeBoolean(true);
                out.writeString(tooltip.toJsonString());
            } else {
                out.writeBoolean(false);
            }
        }
    }
}
