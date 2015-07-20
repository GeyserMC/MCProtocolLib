package org.spacehq.mc.protocol.packet.ingame.client;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.setting.ChatVisibility;
import org.spacehq.mc.protocol.data.game.values.setting.SkinPart;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientSettingsPacket implements Packet {

    private String locale;
    private int renderDistance;
    private ChatVisibility chatVisibility;
    private boolean chatColors;
    private List<SkinPart> visibleParts;

    @SuppressWarnings("unused")
    private ClientSettingsPacket() {
    }

    public ClientSettingsPacket(String locale, int renderDistance, ChatVisibility chatVisibility, boolean chatColors, SkinPart... visibleParts) {
        this.locale = locale;
        this.renderDistance = renderDistance;
        this.chatVisibility = chatVisibility;
        this.chatColors = chatColors;
        this.visibleParts = Arrays.asList(visibleParts);
    }

    public String getLocale() {
        return this.locale;
    }

    public int getRenderDistance() {
        return this.renderDistance;
    }

    public ChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }

    public boolean getUseChatColors() {
        return this.chatColors;
    }

    public List<SkinPart> getVisibleParts() {
        return this.visibleParts;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.locale = in.readString();
        this.renderDistance = in.readByte();
        this.chatVisibility = MagicValues.key(ChatVisibility.class, in.readByte());
        this.chatColors = in.readBoolean();
        this.visibleParts = new ArrayList<SkinPart>();
        int flags = in.readUnsignedByte();
        for(SkinPart part : SkinPart.values()) {
            int bit = 1 << part.ordinal();
            if((flags & bit) == bit) {
                this.visibleParts.add(part);
            }
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.locale);
        out.writeByte(this.renderDistance);
        out.writeByte(MagicValues.value(Integer.class, this.chatVisibility));
        out.writeBoolean(this.chatColors);
        int flags = 0;
        for(SkinPart part : this.visibleParts) {
            flags |= 1 << part.ordinal();
        }

        out.writeByte(flags);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
