package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientEditBookPacket implements Packet {
    private int slot;
    private List<String> pages;
    private @Nullable
    String title;

    @Override
    public void read(NetInput in) throws IOException {
        this.slot = in.readVarInt();
        this.pages = new ArrayList<>();
        int pagesSize = in.readVarInt();
        for (int i = 0; i < pagesSize; i++) {
            this.pages.add(in.readString());
        }
        if (in.readBoolean()) {
            this.title = in.readString();
        } else {
            this.title = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(slot);
        out.writeVarInt(this.pages.size());
        for (String page : this.pages) {
            out.writeString(page);
        }
        out.writeBoolean(this.title != null);
        if (this.title != null) {
            out.writeString(title);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
