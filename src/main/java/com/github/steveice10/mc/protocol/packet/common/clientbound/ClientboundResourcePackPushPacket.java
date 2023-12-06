package com.github.steveice10.mc.protocol.packet.common.clientbound;

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
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundResourcePackPushPacket implements MinecraftPacket {
    private final @NonNull UUID id;
    private final @NonNull String url;
    private final @NonNull String hash;
    private final boolean required;
    private final @Nullable Component prompt;

    public ClientboundResourcePackPushPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.id = helper.readUUID(in);
        this.url = helper.readString(in);
        this.hash = helper.readString(in);
        this.required = in.readBoolean();
        this.prompt = helper.readNullable(in, helper::readComponent);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeUUID(out, this.id);
        helper.writeString(out, this.url);
        helper.writeString(out, this.hash);
        out.writeBoolean(this.required);
        helper.writeNullable(out, this.prompt, helper::writeComponent);
    }
}
