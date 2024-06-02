package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.ServerLink;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

@Data
@With
@AllArgsConstructor
public class ClientboundServerLinksPacket implements MinecraftPacket {
    private final List<ServerLink> links;

    public ClientboundServerLinksPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.links = new ArrayList<>();

        int length = helper.readVarInt(in);
        for (int i = 0; i < length; i++) {
            OptionalInt knownType = OptionalInt.empty();
            Component unknownType = null;
            if (in.readBoolean()) {
                knownType = OptionalInt.of(helper.readVarInt(in));
            } else {
                unknownType = helper.readComponent(in);
            }

            String url = helper.readString(in);
            this.links.add(new ServerLink(knownType, unknownType, url));
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.links.size());
        for (ServerLink link : this.links) {
            out.writeBoolean(link.knownType().isPresent());
            if (link.knownType().isPresent()) {
                helper.writeVarInt(out, link.knownType().getAsInt());
            } else {
                helper.writeComponent(out, link.unknownType());
            }

            helper.writeString(out, link.url());
        }
    }
}
