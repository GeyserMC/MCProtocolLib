package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.jetbrains.annotations.Nullable;

@Data
@With
@AllArgsConstructor
public class ServerboundCustomClickActionPacket implements MinecraftPacket {
    private final Key id;
    private final @Nullable String payload;

    public ServerboundCustomClickActionPacket(ByteBuf in) {
        this.id = MinecraftTypes.readResourceLocation(in);
        this.payload = MinecraftTypes.readNullable(in, MinecraftTypes::readString);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeResourceLocation(out, this.id);
        MinecraftTypes.writeNullable(out, this.payload, MinecraftTypes::writeString);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
