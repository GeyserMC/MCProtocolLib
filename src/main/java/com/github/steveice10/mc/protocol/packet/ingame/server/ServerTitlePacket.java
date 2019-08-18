package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.TitleAction;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerTitlePacket implements Packet {
    private @NonNull TitleAction action;

    private Message title;

    private int fadeIn;
    private int stay;
    private int fadeOut;

    public ServerTitlePacket(TitleAction action) {
        if(action != TitleAction.CLEAR && action != TitleAction.RESET) {
            throw new IllegalArgumentException("Constructor (action, title) only accepts CLEAR, RESET.");
        }

        this.action = action;
    }

    public ServerTitlePacket(TitleAction action, Message title) {
        if(action != TitleAction.TITLE && action != TitleAction.SUBTITLE && action != TitleAction.ACTION_BAR) {
            throw new IllegalArgumentException("Constructor (action, title) only accepts TITLE, SUBTITLE, and ACTION_BAR.");
        }

        this.action = action;

        this.title = title;
    }

    public ServerTitlePacket(int fadeIn, int stay, int fadeOut) {
        this.action = TitleAction.TIMES;

        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.action = MagicValues.key(TitleAction.class, in.readVarInt());
        switch(this.action) {
            case TITLE:
            case SUBTITLE:
            case ACTION_BAR:
                this.title = Message.fromString(in.readString());
                break;
            case TIMES:
                this.fadeIn = in.readInt();
                this.stay = in.readInt();
                this.fadeOut = in.readInt();
                break;
            case CLEAR:
            case RESET:
                break;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        switch(this.action) {
            case TITLE:
            case SUBTITLE:
            case ACTION_BAR:
                out.writeString(this.title.toJsonString());
                break;
            case TIMES:
                out.writeInt(this.fadeIn);
                out.writeInt(this.stay);
                out.writeInt(this.fadeOut);
                break;
            case CLEAR:
            case RESET:
                break;
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
