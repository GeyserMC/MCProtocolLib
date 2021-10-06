package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
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

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientboundChangeDifficultyPacket implements Packet {
    private @NonNull Difficulty difficulty;
    private boolean difficultyLocked;

    @Override
    public void read(NetInput in) throws IOException {
        this.difficulty = MagicValues.key(Difficulty.class, in.readUnsignedByte());
        this.difficultyLocked = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(MagicValues.value(Integer.class, this.difficulty));
        out.writeBoolean(this.difficultyLocked);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
