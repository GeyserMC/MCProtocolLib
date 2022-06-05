package com.github.steveice10.mc.protocol.packet.ingame.serverbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

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

    public ServerboundPlayerInputPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.sideways = in.readFloat();
        this.forward = in.readFloat();

        int flags = in.readUnsignedByte();
        this.jump = (flags & FLAG_JUMP) != 0;
        this.dismount = (flags & FLAG_DISMOUNT) != 0;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeFloat(this.sideways);
        out.writeFloat(this.forward);

        int flags = 0;
        if (this.jump) {
            flags |= FLAG_JUMP;
        }

        if (this.dismount) {
            flags |= FLAG_DISMOUNT;
        }

        out.writeByte(flags);
    }
}
