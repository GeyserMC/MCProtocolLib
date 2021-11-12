package com.github.steveice10.mc.protocol.packet.ingame.clientbound.window;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
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
public class ClientboundOpenScreenPacket implements Packet {
    private final int windowId;
    private final @NonNull WindowType type;
    private final @NonNull String name;

    public ClientboundOpenScreenPacket(NetInput in) throws IOException {
        this.windowId = in.readVarInt();
        this.type = MagicValues.key(WindowType.class, in.readVarInt());
        this.name = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.windowId);
        out.writeVarInt(MagicValues.value(Integer.class, this.type));
        out.writeString(this.name);
    }
}
