package com.github.steveice10.mc.protocol.packet.ingame.server.window;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
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
public class ServerOpenWindowPacket implements Packet {
    private int windowId;
    private @NonNull WindowType type;
    private @NonNull String name;

    @Override
    public void read(NetInput in) throws IOException {
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

    @Override
    public boolean isPriority() {
        return false;
    }
}
