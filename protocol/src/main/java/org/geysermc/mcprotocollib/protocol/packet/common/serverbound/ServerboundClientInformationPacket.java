package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
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

    public ServerboundClientInformationPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.locale = helper.readString(in);
        this.renderDistance = in.readByte();
        this.chatVisibility = ChatVisibility.from(helper.readVarInt(in));
        this.useChatColors = in.readBoolean();
        this.visibleParts = new ArrayList<>();

        int flags = in.readUnsignedByte();
        for (SkinPart part : SkinPart.VALUES) {
            int bit = 1 << part.ordinal();
            if ((flags & bit) == bit) {
                this.visibleParts.add(part);
            }
        }

        this.mainHand = HandPreference.from(helper.readVarInt(in));
        this.textFilteringEnabled = in.readBoolean();
        this.allowsListing = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeString(out, this.locale);
        out.writeByte(this.renderDistance);
        helper.writeVarInt(out, this.chatVisibility.ordinal());
        out.writeBoolean(this.useChatColors);

        int flags = 0;
        for (SkinPart part : this.visibleParts) {
            flags |= 1 << part.ordinal();
        }

        out.writeByte(flags);

        helper.writeVarInt(out, this.mainHand.ordinal());
        out.writeBoolean(this.textFilteringEnabled);
        out.writeBoolean(allowsListing);
    }
}
