package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundOpenSignEditorPacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final boolean isFrontText;

    public ClientboundOpenSignEditorPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.position = helper.readPosition(in);
        this.isFrontText = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writePosition(out, this.position);
        out.writeBoolean(this.isFrontText);
    }
}
