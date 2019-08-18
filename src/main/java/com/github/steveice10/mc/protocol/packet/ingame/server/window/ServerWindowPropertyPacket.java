package com.github.steveice10.mc.protocol.packet.ingame.server.window;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.window.property.WindowProperty;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerWindowPropertyPacket implements Packet {
    private int windowId;
    private int rawProperty;
    private int value;

    public ServerWindowPropertyPacket(int windowId, WindowProperty rawProperty, int value) {
        this(windowId, MagicValues.value(Integer.class, rawProperty), value);
    }

    public <T extends WindowProperty> T getProperty(Class<T> type) {
        return MagicValues.key(type, this.value);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readUnsignedByte();
        this.rawProperty = in.readShort();
        this.value = in.readShort();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.rawProperty);
        out.writeShort(this.value);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
