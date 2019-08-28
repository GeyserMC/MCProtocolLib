package com.github.steveice10.mc.protocol.data.game;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NBT {
    private NBT() {
    }

    public static CompoundTag read(NetInput in) throws IOException {
        return (CompoundTag) NBTIO.readTag(new InputStream() {
            @Override
            public int read() throws IOException {
                return in.readUnsignedByte();
            }
        });
    }

    public static void write(NetOutput out, CompoundTag tag) throws IOException {
        NBTIO.writeTag(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                out.writeByte(b);
            }
        }, tag);
    }
}
