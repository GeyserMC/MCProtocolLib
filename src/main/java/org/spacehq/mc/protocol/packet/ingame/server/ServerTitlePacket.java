package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.TitleAction;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerTitlePacket implements Packet {
    private TitleAction action;

    private Message title;

    private Message subtitle;

    private int fadeIn;
    private int stay;
    private int fadeOut;

    @SuppressWarnings("unused")
    private ServerTitlePacket() {
    }

    public ServerTitlePacket(String title, boolean sub) {
        this(Message.fromString(title), sub);
    }

    public ServerTitlePacket(Message title, boolean sub) {
        if(sub) {
            this.action = TitleAction.SUBTITLE;
            this.subtitle = title;
        } else {
            this.action = TitleAction.TITLE;
            this.title = title;
        }
    }

    public ServerTitlePacket(int fadeIn, int stay, int fadeOut) {
        this.action = TitleAction.TIMES;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public ServerTitlePacket(boolean clear) {
        if(clear) {
            this.action = TitleAction.CLEAR;
        } else {
            this.action = TitleAction.RESET;
        }
    }

    public TitleAction getAction() {
        return this.action;
    }

    public Message getTitle() {
        return this.title;
    }

    public Message getSubtitle() {
        return this.subtitle;
    }

    public int getFadeIn() {
        return this.fadeIn;
    }

    public int getStay() {
        return this.stay;
    }

    public int getFadeOut() {
        return this.fadeOut;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.action = MagicValues.key(TitleAction.class, in.readVarInt());
        switch(this.action) {
            case TITLE:
                this.title = Message.fromString(in.readString());
                break;
            case SUBTITLE:
                this.subtitle = Message.fromString(in.readString());
                break;
            case TIMES:
                this.fadeIn = in.readInt();
                this.stay = in.readInt();
                this.fadeOut = in.readInt();
                break;
            case CLEAR:
                break;
            case RESET:
                break;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        switch(this.action) {
            case TITLE:
                out.writeString(this.title.toJsonString());
                break;
            case SUBTITLE:
                out.writeString(this.subtitle.toJsonString());
                break;
            case TIMES:
                out.writeInt(this.fadeIn);
                out.writeInt(this.stay);
                out.writeInt(this.fadeOut);
                break;
            case CLEAR:
                break;
            case RESET:
                break;
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
