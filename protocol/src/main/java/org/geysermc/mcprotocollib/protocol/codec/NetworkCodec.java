package org.geysermc.mcprotocollib.protocol.codec;

import io.netty.buffer.ByteBuf;

public record NetworkCodec<T>(Writer<T> writer, Reader<T> reader) {
    public static <T> NetworkCodec<T> of(Writer<T> writer, Reader<T> reader) {
        return new NetworkCodec<>(writer, reader);
    }

    public void write(T t, ByteBuf byteBuf, MinecraftCodecHelper helper) {
        writer.write(t, byteBuf, helper);
    }

    public T read(ByteBuf byteBuf, MinecraftCodecHelper helper) {
        return reader.read(byteBuf, helper);
    }

    public interface Writer<T> {
        void write(T t, ByteBuf out, MinecraftCodecHelper helper);
    }

    public interface Reader<T> {
        T read(ByteBuf in, MinecraftCodecHelper helper);
    }
}
