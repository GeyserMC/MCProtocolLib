package com.github.steveice10.mc.protocol.packet.ingame.client;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientTabCompletePacket implements Packet {
    private int transactionId;
    private @NonNull String text;

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

    @Override
    public boolean isPriority() {
        return false;
    }
}
