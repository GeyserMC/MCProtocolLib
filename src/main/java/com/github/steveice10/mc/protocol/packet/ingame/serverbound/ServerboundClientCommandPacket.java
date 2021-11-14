package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.ClientCommand;
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
public class ServerboundClientCommandPacket implements Packet {
    private final @NonNull ClientCommand request;

    public ServerboundClientCommandPacket(NetInput in) throws IOException {
        this.request = MagicValues.key(ClientCommand.class, in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.request));
    }
}
