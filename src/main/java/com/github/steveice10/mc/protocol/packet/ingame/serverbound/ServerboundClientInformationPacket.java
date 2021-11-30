package com.github.steveice10.mc.protocol.packet.ingame.serverbound;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.HandPreference;
import com.github.steveice10.mc.protocol.data.game.setting.ChatVisibility;
import com.github.steveice10.mc.protocol.data.game.setting.SkinPart;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ServerboundClientInformationPacket implements Packet {
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

    public ServerboundClientInformationPacket(NetInput in) throws IOException {
        this.locale = in.readString();
        this.renderDistance = in.readByte();
        this.chatVisibility = MagicValues.key(ChatVisibility.class, in.readVarInt());
        this.useChatColors = in.readBoolean();
        this.visibleParts = new ArrayList<>();

        int flags = in.readUnsignedByte();
        for (SkinPart part : SkinPart.VALUES) {
            int bit = 1 << part.ordinal();
            if ((flags & bit) == bit) {
                this.visibleParts.add(part);
            }
        }

        this.mainHand = MagicValues.key(HandPreference.class, in.readVarInt());
        this.textFilteringEnabled = in.readBoolean();
        this.allowsListing = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.locale);
        out.writeByte(this.renderDistance);
        out.writeVarInt(MagicValues.value(Integer.class, this.chatVisibility));
        out.writeBoolean(this.useChatColors);

        int flags = 0;
        for (SkinPart part : this.visibleParts) {
            flags |= 1 << part.ordinal();
        }

        out.writeByte(flags);

        out.writeVarInt(MagicValues.value(Integer.class, this.mainHand));
        out.writeBoolean(this.textFilteringEnabled);
        out.writeBoolean(allowsListing);
    }
}
