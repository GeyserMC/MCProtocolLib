package com.github.steveice10.mc.protocol.packet.ingame.client.window;

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
public class ClientClickWindowButtonPacket implements Packet {
    private int windowId;
    private int buttonId;

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

    @Override
    public boolean isPriority() {
        return false;
    }
}
