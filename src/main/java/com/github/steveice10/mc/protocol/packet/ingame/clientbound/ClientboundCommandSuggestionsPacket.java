package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.Arrays;

@Data
@With
public class ClientboundCommandSuggestionsPacket implements MinecraftPacket {
    private final int transactionId;
    private final int start;
    private final int length;
    private final @NonNull String[] matches;
    private final @NonNull Component[] tooltips;

    public ClientboundCommandSuggestionsPacket(int transactionId, int start, int length, @NonNull String[] matches, @NonNull Component[] tooltips) {
        if (tooltips.length != matches.length) {
            throw new IllegalArgumentException("Length of matches and tooltips must be equal.");
        }

        this.transactionId = transactionId;
        this.start = start;
        this.length = length;
        this.matches = Arrays.copyOf(matches, matches.length);
        this.tooltips = Arrays.copyOf(tooltips, tooltips.length);
    }

    public ClientboundCommandSuggestionsPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.transactionId = helper.readVarInt(in);
        this.start = helper.readVarInt(in);
        this.length = helper.readVarInt(in);
        this.matches = new String[helper.readVarInt(in)];
        this.tooltips = new Component[this.matches.length];
        for (int index = 0; index < this.matches.length; index++) {
            this.matches[index] = helper.readString(in);
            if (in.readBoolean()) {
                this.tooltips[index] = helper.readComponent(in);
            }
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.transactionId);
        helper.writeVarInt(out, this.start);
        helper.writeVarInt(out, this.length);
        helper.writeVarInt(out, this.matches.length);
        for (int index = 0; index < this.matches.length; index++) {
            helper.writeString(out, this.matches[index]);
            Component tooltip = this.tooltips[index];
            if (tooltip != null) {
                out.writeBoolean(true);
                helper.writeComponent(out, tooltip);
            } else {
                out.writeBoolean(false);
            }
        }
    }
}
