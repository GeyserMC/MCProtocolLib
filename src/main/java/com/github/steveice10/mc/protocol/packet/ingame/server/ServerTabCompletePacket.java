package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.util.Arrays;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerTabCompletePacket implements Packet {
    private int transactionId;
    private int start;
    private int length;
    private @NonNull String[] matches;
    private @NonNull Message[] tooltips;

    public ServerTabCompletePacket(int transactionId, int start, int length, @NonNull String[] matches, @NonNull Message[] tooltips) {
        if(tooltips.length != matches.length) {
            throw new IllegalArgumentException("Length of matches and tooltips must be equal.");
        }

        this.transactionId = transactionId;
        this.start = start;
        this.length = length;
        this.matches = Arrays.copyOf(matches, matches.length);
        this.tooltips = Arrays.copyOf(tooltips, tooltips.length);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.transactionId = in.readVarInt();
        this.start = in.readVarInt();
        this.length = in.readVarInt();
        this.matches = new String[in.readVarInt()];
        this.tooltips = new Message[this.matches.length];
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

    @Override
    public boolean isPriority() {
        return false;
    }
}
