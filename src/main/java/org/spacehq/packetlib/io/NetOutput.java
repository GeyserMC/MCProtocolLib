package org.spacehq.packetlib.io;

import java.io.IOException;
import java.util.UUID;

/**
 * An interface for writing network data.
 */
public interface NetOutput {
    /**
     * Writes a boolean.
     *
     * @param b Boolean to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeBoolean(boolean b) throws IOException;

    /**
     * Writes a byte.
     *
     * @param b Byte to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeByte(int b) throws IOException;

    /**
     * Writes a short.
     *
     * @param s Short to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeShort(int s) throws IOException;

    /**
     * Writes a char.
     *
     * @param c Char to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeChar(int c) throws IOException;

    /**
     * Writes a integer.
     *
     * @param i Integer to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeInt(int i) throws IOException;

    /**
     * Writes a varint. A varint is a form of integer where only necessary bytes are written. This is done to save bandwidth.
     *
     * @param i Varint to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeVarInt(int i) throws IOException;

    /**
     * Writes a long.
     *
     * @param l Long to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeLong(long l) throws IOException;

    /**
     * Writes a varlong. A varlong is a form of long where only necessary bytes are written. This is done to save bandwidth.
     *
     * @param l Varlong to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeVarLong(long l) throws IOException;

    /**
     * Writes a float.
     *
     * @param f Float to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeFloat(float f) throws IOException;

    /**
     * Writes a double.
     *
     * @param d Double to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeDouble(double d) throws IOException;

    /**
     * Writes a byte array.
     *
     * @param b Byte array to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeBytes(byte b[]) throws IOException;

    /**
     * Writes a byte array, using the given amount of bytes.
     *
     * @param b      Byte array to write.
     * @param length Bytes to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeBytes(byte b[], int length) throws IOException;

    /**
     * Writes a short array.
     *
     * @param s Short array to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeShorts(short s[]) throws IOException;

    /**
     * Writes a short array, using the given amount of bytes.
     *
     * @param s      Short array to write.
     * @param length Shorts to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeShorts(short s[], int length) throws IOException;

    /**
     * Writes an int array.
     *
     * @param i Int array to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeInts(int i[]) throws IOException;

    /**
     * Writes an int array, using the given amount of bytes.
     *
     * @param i      Int array to write.
     * @param length Ints to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeInts(int i[], int length) throws IOException;

    /**
     * Writes a long array.
     *
     * @param l Long array to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeLongs(long l[]) throws IOException;

    /**
     * Writes a long array, using the given amount of bytes.
     *
     * @param l      Long array to write.
     * @param length Longs to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeLongs(long l[], int length) throws IOException;

    /**
     * Writes a string.
     *
     * @param s String to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeString(String s) throws IOException;

    /**
     * Writes a UUID.
     *
     * @param uuid UUID to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeUUID(UUID uuid) throws IOException;

    /**
     * Flushes the output.
     *
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void flush() throws IOException;
}
