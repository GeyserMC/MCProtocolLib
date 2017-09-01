package com.github.steveice10.mc.protocol.util;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import org.junit.Test;

import java.io.IOException;

import static com.github.steveice10.mc.protocol.ByteBufHelper.assertPosition;
import static com.github.steveice10.mc.protocol.ByteBufHelper.in;
import static com.github.steveice10.mc.protocol.ByteBufHelper.out;
import static com.github.steveice10.mc.protocol.util.NetUtil.readPosition;
import static com.github.steveice10.mc.protocol.util.NetUtil.writePosition;

public class NetUtilTest {
    @Test
    public void testPosition() throws IOException {
        writePosition(out, new Position(1, 61, -1));
        assertPosition(readPosition(in), 1, 61, -1);
    }
}
