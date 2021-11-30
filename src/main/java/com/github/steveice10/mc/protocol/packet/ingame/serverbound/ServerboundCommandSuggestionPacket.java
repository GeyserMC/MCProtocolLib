package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundCommandSuggestionPacket implements Packet {
    private final int transactionId;
    private final @NonNull String text;

    public ServerboundCommandSuggestionPacket(NetInput in) throws IOException {
        this.transactionId = in.readVarInt();
        this.text = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.transactionId);
        out.writeString(this.text);
    }
}
