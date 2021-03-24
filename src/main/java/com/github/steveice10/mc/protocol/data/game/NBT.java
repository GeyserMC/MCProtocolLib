package com.github.steveice10.mc.protocol.data.game;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NBT {
    private NBT() {
    }

    public static CompoundBinaryTag read(NetInput in) throws IOException {
        return BinaryTagIO.reader().read(new InputStream() {
            @Override
            public int read() throws IOException {
                return in.readUnsignedByte();
            }
        });
    }

    public static void write(NetOutput out, CompoundBinaryTag nbt) throws IOException {
        BinaryTagIO.writer().write(nbt, new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                out.writeByte(b);
            }
        });
    }
}
