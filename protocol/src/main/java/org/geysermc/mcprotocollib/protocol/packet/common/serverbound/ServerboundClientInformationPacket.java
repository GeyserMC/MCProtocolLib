package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.HandPreference;
import org.geysermc.mcprotocollib.protocol.data.game.setting.ChatVisibility;
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

    public ServerboundClientInformationPacket(MinecraftByteBuf buf) {
        this.locale = buf.readString();
        this.renderDistance = buf.readByte();
        this.chatVisibility = ChatVisibility.from(buf.readVarInt());
        this.useChatColors = buf.readBoolean();
        this.visibleParts = new ArrayList<>();

        int flags = buf.readUnsignedByte();
        for (SkinPart part : SkinPart.VALUES) {
            int bit = 1 << part.ordinal();
            if ((flags & bit) == bit) {
                this.visibleParts.add(part);
            }
        }

        this.mainHand = HandPreference.from(buf.readVarInt());
        this.textFilteringEnabled = buf.readBoolean();
        this.allowsListing = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeString(this.locale);
        buf.writeByte(this.renderDistance);
        buf.writeVarInt(this.chatVisibility.ordinal());
        buf.writeBoolean(this.useChatColors);

        int flags = 0;
        for (SkinPart part : this.visibleParts) {
            flags |= 1 << part.ordinal();
        }

        buf.writeByte(flags);

        buf.writeVarInt(this.mainHand.ordinal());
        buf.writeBoolean(this.textFilteringEnabled);
        buf.writeBoolean(allowsListing);
    }
}
