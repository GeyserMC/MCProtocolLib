package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerAdvancementTabPacket implements Packet {
    private String tabId;

    @Override
    public void read(NetInput in) throws IOException {
        if (in.readBoolean()) {
            this.tabId = in.readString();
        } else {
            this.tabId = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        if (this.tabId != null) {
            out.writeBoolean(true);
            out.writeString(this.tabId);
        } else {
            out.writeBoolean(false);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
