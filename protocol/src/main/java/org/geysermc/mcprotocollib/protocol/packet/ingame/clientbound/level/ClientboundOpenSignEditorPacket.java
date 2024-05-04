package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundOpenSignEditorPacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final boolean isFrontText;

    public ClientboundOpenSignEditorPacket(MinecraftByteBuf buf) {
        this.position = buf.readPosition();
        this.isFrontText = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writePosition(this.position);
        buf.writeBoolean(this.isFrontText);
    }
}
