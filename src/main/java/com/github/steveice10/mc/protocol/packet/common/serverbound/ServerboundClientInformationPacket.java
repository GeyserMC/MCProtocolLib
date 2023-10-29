package com.github.steveice10.mc.protocol.packet.common.serverbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.player.HandPreference;
import com.github.steveice10.mc.protocol.data.game.setting.ChatVisibility;
import com.github.steveice10.mc.protocol.data.game.setting.SkinPart;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ServerboundClientInformationPacket implements MinecraftPacket {
    private final @NotNull String locale;
    private final int renderDistance;
    private final @NotNull ChatVisibility chatVisibility;
    private final boolean useChatColors;
    private final @NotNull List<SkinPart> visibleParts;
    private final @NotNull HandPreference mainHand;
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
