package com.github.steveice10.mc.protocol.packet.ingame.clientbound.title;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import net.kyori.adventure.text.Component;

import javax.annotation.Nullable;
import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundSetTitleTextPacket implements Packet {
    private final @Nullable Component text;

    public ClientboundSetTitleTextPacket(NetInput in) throws IOException {
        this.text = DefaultComponentSerializer.get().deserialize(in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(DefaultComponentSerializer.get().serializeOrNull(this.text));
    }
}
