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
public class ClientSetBeaconEffectPacket implements Packet {
    private int primaryEffect;
    private int secondaryEffect;

    @Override
    public void read(NetInput in) throws IOException {
        this.primaryEffect = in.readVarInt();
        this.secondaryEffect = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.primaryEffect);
        out.writeVarInt(this.secondaryEffect);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
