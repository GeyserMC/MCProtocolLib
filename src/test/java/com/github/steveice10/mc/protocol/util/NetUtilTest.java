package com.github.steveice10.mc.protocol.util;

import org.junit.Test;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;

import java.io.IOException;

import static com.github.steveice10.mc.protocol.ByteBufHelper.*;
import static com.github.steveice10.mc.protocol.util.NetUtil.readPosition;
import static com.github.steveice10.mc.protocol.util.NetUtil.writePosition;

public class NetUtilTest {

    @Test
    public void testPosition() throws IOException {
        writePosition(out, new Position(1, 61, -1));
        assertPosition(readPosition(in), 1, 61, -1);
    }

}
