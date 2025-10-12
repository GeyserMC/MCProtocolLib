package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.debug.DebugSubscriptions;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ServerboundDebugSubscriptionRequestPacket implements MinecraftPacket {
    private final List<DebugSubscriptions> subscriptions;

    public ServerboundDebugSubscriptionRequestPacket(ByteBuf in) {
        this.subscriptions = new ArrayList<>();
        int length = MinecraftTypes.readVarInt(in);
        for (int i = 0; i < length; i++) {
            this.subscriptions.add(DebugSubscriptions.from(MinecraftTypes.readVarInt(in)));
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.subscriptions.size());
        for (DebugSubscriptions subscription : this.subscriptions) {
            MinecraftTypes.writeVarInt(out, subscription.ordinal());
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
