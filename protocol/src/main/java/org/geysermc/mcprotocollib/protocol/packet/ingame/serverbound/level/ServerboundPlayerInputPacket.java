package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundPlayerInputPacket implements MinecraftPacket {
    private static final byte FLAG_FORWARD = 0x01;
    private static final byte FLAG_BACKWARD = 0x02;
    private static final byte FLAG_LEFT = 0x04;
    private static final byte FLAG_RIGHT = 0x08;
    private static final byte FLAG_JUMP = 0x10;
    private static final byte FLAG_SHIFT = 0x20;
    private static final byte FLAG_SPRINT = 0x40;

    private final boolean forward;
    private final boolean backward;
    private final boolean left;
    private final boolean right;
    private final boolean jump;
    private final boolean shift;
    private final boolean sprint;

    public ServerboundPlayerInputPacket(ByteBuf in, MinecraftCodecHelper helper) {
        byte flags = in.readByte();
        this.forward = (flags & FLAG_FORWARD) != 0;
        this.backward = (flags & FLAG_BACKWARD) != 0;
        this.left = (flags & FLAG_LEFT) != 0;
        this.right = (flags & FLAG_RIGHT) != 0;
        this.jump = (flags & FLAG_JUMP) != 0;
        this.shift = (flags & FLAG_SHIFT) != 0;
        this.sprint = (flags & FLAG_SPRINT) != 0;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        byte flags = 0;
        if (this.forward) {
            flags |= FLAG_FORWARD;
        }

        if (this.backward) {
            flags |= FLAG_BACKWARD;
        }

        if (this.left) {
            flags |= FLAG_LEFT;
        }

        if (this.right) {
            flags |= FLAG_RIGHT;
        }

        if (this.jump) {
            flags |= FLAG_JUMP;
        }

        if (this.shift) {
            flags |= FLAG_SHIFT;
        }

        if (this.sprint) {
            flags |= FLAG_SPRINT;
        }

        out.writeByte(flags);
    }
}
