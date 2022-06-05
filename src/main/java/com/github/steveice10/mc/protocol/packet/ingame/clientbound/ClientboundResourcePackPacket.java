package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundResourcePackPacket implements MinecraftPacket {
    private final @NonNull String url;
    private final @NonNull String hash;
    private final boolean required;
    private final @Nullable Component prompt;

    public ClientboundResourcePackPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.url = helper.readString(in);
        this.hash = helper.readString(in);
        this.required = in.readBoolean();
        if (in.readBoolean()) {
            this.prompt = helper.readComponent(in);
        } else {
            this.prompt = null;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeString(out, this.url);
        helper.writeString(out, this.hash);
        out.writeBoolean(this.required);
        out.writeBoolean(this.prompt != null);
        if (this.prompt != null) {
            helper.writeComponent(out, this.prompt);
        }
    }
}
