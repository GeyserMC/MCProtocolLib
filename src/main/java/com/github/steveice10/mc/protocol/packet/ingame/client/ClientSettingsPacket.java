package com.github.steveice10.mc.protocol.packet.ingame.client;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.HandPreference;
import com.github.steveice10.mc.protocol.data.game.setting.ChatVisibility;
import com.github.steveice10.mc.protocol.data.game.setting.SkinPart;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientSettingsPacket implements Packet {
    private @NonNull String locale;
    private int renderDistance;
    private @NonNull ChatVisibility chatVisibility;
    private boolean useChatColors;
    private @NonNull List<SkinPart> visibleParts;
    private @NonNull HandPreference mainHand;
    private boolean textFilteringEnabled;

    @Override
    public void read(NetInput in) throws IOException {
        this.locale = in.readString();
        this.renderDistance = in.readByte();
        this.chatVisibility = MagicValues.key(ChatVisibility.class, in.readVarInt());
        this.useChatColors = in.readBoolean();
        this.visibleParts = new ArrayList<>();

        int flags = in.readUnsignedByte();
        for(SkinPart part : SkinPart.values()) {
            int bit = 1 << part.ordinal();
            if((flags & bit) == bit) {
                this.visibleParts.add(part);
            }
        }

        this.mainHand = MagicValues.key(HandPreference.class, in.readVarInt());
        this.textFilteringEnabled = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.locale);
        out.writeByte(this.renderDistance);
        out.writeVarInt(MagicValues.value(Integer.class, this.chatVisibility));
        out.writeBoolean(this.useChatColors);

        int flags = 0;
        for(SkinPart part : this.visibleParts) {
            flags |= 1 << part.ordinal();
        }

        out.writeByte(flags);

        out.writeVarInt(MagicValues.value(Integer.class, this.mainHand));
        out.writeBoolean(this.textFilteringEnabled);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
