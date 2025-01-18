package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundOpenSignEditorPacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final boolean isFrontText;

    public ClientboundOpenSignEditorPacket(ByteBuf in) {
        this.position = MinecraftTypes.readPosition(in);
        this.isFrontText = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writePosition(out, this.position);
        out.writeBoolean(this.isFrontText);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
