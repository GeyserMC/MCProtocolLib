package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.TestInstanceBlockEntity;

@Data
@With
@AllArgsConstructor
public class ServerboundTestInstanceBlockActionPacket implements MinecraftPacket {
    private final Vector3i pos;
    private final int action;
    private final TestInstanceBlockEntity data;

    public ServerboundTestInstanceBlockActionPacket(ByteBuf in) {
        this.pos = MinecraftTypes.readPosition(in);
        this.action = MinecraftTypes.readVarInt(in);
        this.data = MinecraftTypes.readTestBlockEntity(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writePosition(out, this.pos);
        MinecraftTypes.writeVarInt(out, this.action);
        MinecraftTypes.writeTestBlockEntity(out, this.data);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
