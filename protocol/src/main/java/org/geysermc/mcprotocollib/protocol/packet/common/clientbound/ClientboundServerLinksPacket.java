package org.geysermc.mcprotocollib.protocol.packet.common.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.ServerLink;
import org.geysermc.mcprotocollib.protocol.data.game.ServerLinkType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@With
@AllArgsConstructor
public class ClientboundServerLinksPacket implements MinecraftPacket {
    private final List<ServerLink> links;

    public ClientboundServerLinksPacket(ByteBuf in) {
        this.links = new ArrayList<>();

        int length = MinecraftTypes.readVarInt(in);
        for (int i = 0; i < length; i++) {
            ServerLinkType knownType = null;
            Component unknownType = null;
            if (in.readBoolean()) {
                knownType = ServerLinkType.from(MinecraftTypes.readVarInt(in));
            } else {
                unknownType = MinecraftTypes.readComponent(in);
            }

            String link = MinecraftTypes.readString(in);
            this.links.add(new ServerLink(knownType, unknownType, link));
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.links.size());
        for (ServerLink link : this.links) {
            out.writeBoolean(link.knownType() != null);
            if (link.knownType() != null) {
                MinecraftTypes.writeVarInt(out, link.knownType().ordinal());
            } else {
                MinecraftTypes.writeComponent(out, Objects.requireNonNull(link.unknownType(), "server link must specify unknown type component if known type is null"));
            }

            MinecraftTypes.writeString(out, link.link());
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
