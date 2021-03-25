package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerResourcePackSendPacket implements Packet {
    private @NonNull String url;
    private @NonNull String hash;
    private boolean mustAcceptPack;

    @Override
    public void read(NetInput in) throws IOException {
        this.url = in.readString();
        this.hash = in.readString();
        this.mustAcceptPack = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.url);
        out.writeString(this.hash);
        out.writeBoolean(this.mustAcceptPack);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
