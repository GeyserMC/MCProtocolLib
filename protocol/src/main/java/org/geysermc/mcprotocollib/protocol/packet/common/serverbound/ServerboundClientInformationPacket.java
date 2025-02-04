package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.HandPreference;
import org.geysermc.mcprotocollib.protocol.data.game.setting.ChatVisibility;
import org.geysermc.mcprotocollib.protocol.data.game.setting.ParticleStatus;
import org.geysermc.mcprotocollib.protocol.data.game.setting.SkinPart;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ServerboundClientInformationPacket implements MinecraftPacket {
    private final @NonNull String locale;
    private final int renderDistance;
    private final @NonNull ChatVisibility chatVisibility;
    private final boolean useChatColors;
    private final @NonNull List<SkinPart> visibleParts;
    private final @NonNull HandPreference mainHand;
    private final boolean textFilteringEnabled;
    /**
     * Whether the client permits being shown in server ping responses.
     */
    private final boolean allowsListing;
    private final @NonNull ParticleStatus particleStatus;

    public ServerboundClientInformationPacket(ByteBuf in) {
        this.locale = MinecraftTypes.readString(in);
        this.renderDistance = in.readByte();
        this.chatVisibility = ChatVisibility.from(MinecraftTypes.readVarInt(in));
        this.useChatColors = in.readBoolean();
        this.visibleParts = new ArrayList<>();

        int flags = in.readUnsignedByte();
        for (SkinPart part : SkinPart.VALUES) {
            int bit = 1 << part.ordinal();
            if ((flags & bit) == bit) {
                this.visibleParts.add(part);
            }
        }

        this.mainHand = HandPreference.from(MinecraftTypes.readVarInt(in));
        this.textFilteringEnabled = in.readBoolean();
        this.allowsListing = in.readBoolean();
        this.particleStatus = ParticleStatus.from(MinecraftTypes.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeString(out, this.locale);
        out.writeByte(this.renderDistance);
        MinecraftTypes.writeVarInt(out, this.chatVisibility.ordinal());
        out.writeBoolean(this.useChatColors);

        int flags = 0;
        for (SkinPart part : this.visibleParts) {
            flags |= 1 << part.ordinal();
        }

        out.writeByte(flags);

        MinecraftTypes.writeVarInt(out, this.mainHand.ordinal());
        out.writeBoolean(this.textFilteringEnabled);
        out.writeBoolean(allowsListing);
        MinecraftTypes.writeVarInt(out, this.particleStatus.ordinal());
    }

    @Override
    public boolean shouldRunOnGameThread() {
        // GAME THREAD DETAIL: Code is only async during GAME state.
        return false; // False, you need to handle making it async yourself
    }
}
