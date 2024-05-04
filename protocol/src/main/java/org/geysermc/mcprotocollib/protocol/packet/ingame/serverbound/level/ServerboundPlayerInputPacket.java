package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundPlayerInputPacket implements MinecraftPacket {
    private static final int FLAG_JUMP = 0x01;
    private static final int FLAG_DISMOUNT = 0x02;

    private final float sideways;
    private final float forward;
    private final boolean jump;
    private final boolean dismount;

    public ServerboundPlayerInputPacket(MinecraftByteBuf buf) {
        this.sideways = buf.readFloat();
        this.forward = buf.readFloat();

        int flags = buf.readUnsignedByte();
        this.jump = (flags & FLAG_JUMP) != 0;
        this.dismount = (flags & FLAG_DISMOUNT) != 0;
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeFloat(this.sideways);
        buf.writeFloat(this.forward);

        int flags = 0;
        if (this.jump) {
            flags |= FLAG_JUMP;
        }

        if (this.dismount) {
            flags |= FLAG_DISMOUNT;
        }

        buf.writeByte(flags);
    }
}
