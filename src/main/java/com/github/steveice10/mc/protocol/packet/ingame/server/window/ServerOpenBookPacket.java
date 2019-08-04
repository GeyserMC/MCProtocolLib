package com.github.steveice10.mc.protocol.packet.ingame.server.window;

import java.io.IOException;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

public class ServerOpenBookPacket extends MinecraftPacket {

    private Hand hand;

    public ServerOpenBookPacket() {
    }

    public ServerOpenBookPacket(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.hand = MagicValues.key(Hand.class, in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, hand));
    }
}
