package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.TitleAction;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ServerTitlePacket extends MinecraftPacket {
    private TitleAction action;

    private String title;

    private String subtitle;

    private String actionBar;

    private int fadeIn;
    private int stay;
    private int fadeOut;

    @SuppressWarnings("unused")
    private ServerTitlePacket() {
    }

    public ServerTitlePacket(String title, boolean sub, boolean escape) {
        this(sub ? TitleAction.SUBTITLE : TitleAction.TITLE, title, escape);
    }

    public ServerTitlePacket(TitleAction action, String title, boolean escape) {
        this.action = action;

        if (escape) {
            title = ServerChatPacket.escapeText(title);
        }

        switch(action) {
            case TITLE:
                this.title = title;
                break;
            case SUBTITLE:
                this.subtitle = title;
                break;
            case ACTION_BAR:
                this.actionBar = title;
                break;
            default:
                throw new IllegalArgumentException("action must be one of TITLE, SUBTITLE, ACTION_BAR");
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

    public String getTitle() {
        return this.title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public String getActionBar() {
        return this.actionBar;
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
                this.title = in.readString();
                break;
            case SUBTITLE:
                this.subtitle = in.readString();
                break;
            case ACTION_BAR:
                this.actionBar = in.readString();
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
                out.writeString(this.title);
                break;
            case SUBTITLE:
                out.writeString(this.subtitle);
                break;
            case ACTION_BAR:
                out.writeString(this.actionBar);
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
}
