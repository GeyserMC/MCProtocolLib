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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerDisconnectPacket implements Packet {
    private @NonNull Component reason;

    public ServerDisconnectPacket(@NonNull String reason) {
        this(GsonComponentSerializer.gson().deserialize(reason));
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.reason = GsonComponentSerializer.gson().deserialize(in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(GsonComponentSerializer.gson().serialize(this.reason));
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
