package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.Arrays;

@Data
@With
public class ClientboundCommandSuggestionsPacket implements MinecraftPacket {
    private final int transactionId;
    private final int start;
    private final int length;
    private final @NonNull String @NonNull [] matches;
    private final Component @NonNull [] tooltips;

    public ClientboundCommandSuggestionsPacket(int transactionId, int start, int length, @NonNull String @NonNull [] matches, Component @NonNull [] tooltips) {
        if (tooltips.length != matches.length) {
            throw new IllegalArgumentException("Length of matches and tooltips must be equal.");
        }

        this.transactionId = transactionId;
        this.start = start;
        this.length = length;
        this.matches = Arrays.copyOf(matches, matches.length);
        this.tooltips = Arrays.copyOf(tooltips, tooltips.length);
    }

    public ClientboundCommandSuggestionsPacket(MinecraftByteBuf buf) {
        this.transactionId = buf.readVarInt();
        this.start = buf.readVarInt();
        this.length = buf.readVarInt();
        this.matches = new String[buf.readVarInt()];
        this.tooltips = new Component[this.matches.length];
        for (int index = 0; index < this.matches.length; index++) {
            this.matches[index] = buf.readString();
            if (buf.readBoolean()) {
                this.tooltips[index] = buf.readComponent();
            }
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.transactionId);
        buf.writeVarInt(this.start);
        buf.writeVarInt(this.length);
        buf.writeVarInt(this.matches.length);
        for (int index = 0; index < this.matches.length; index++) {
            buf.writeString(this.matches[index]);
            Component tooltip = this.tooltips[index];
            if (tooltip != null) {
                buf.writeBoolean(true);
                buf.writeComponent(tooltip);
            } else {
                buf.writeBoolean(false);
            }
        }
    }
}
