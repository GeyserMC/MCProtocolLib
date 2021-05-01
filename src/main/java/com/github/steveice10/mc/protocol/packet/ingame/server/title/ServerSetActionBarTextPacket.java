package com.github.steveice10.mc.protocol.packet.ingame.server.title;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.*;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerSetActionBarTextPacket implements Packet {
    private Component text;

    @Override
    public void read(NetInput in) throws IOException {
        this.text = DefaultComponentSerializer.get().deserialize(in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(DefaultComponentSerializer.get().serialize(this.text));
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
