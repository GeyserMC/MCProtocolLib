package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

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

    public ClientboundCommandSuggestionsPacket(ByteBuf in) {
        this.transactionId = MinecraftTypes.readVarInt(in);
        this.start = MinecraftTypes.readVarInt(in);
        this.length = MinecraftTypes.readVarInt(in);
        this.matches = new String[MinecraftTypes.readVarInt(in)];
        this.tooltips = new Component[this.matches.length];
        for (int index = 0; index < this.matches.length; index++) {
            this.matches[index] = MinecraftTypes.readString(in);
            if (in.readBoolean()) {
                this.tooltips[index] = MinecraftTypes.readComponent(in);
            }
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.transactionId);
        MinecraftTypes.writeVarInt(out, this.start);
        MinecraftTypes.writeVarInt(out, this.length);
        MinecraftTypes.writeVarInt(out, this.matches.length);
        for (int index = 0; index < this.matches.length; index++) {
            MinecraftTypes.writeString(out, this.matches[index]);
            Component tooltip = this.tooltips[index];
            if (tooltip != null) {
                out.writeBoolean(true);
                MinecraftTypes.writeComponent(out, tooltip);
            } else {
                out.writeBoolean(false);
            }
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
