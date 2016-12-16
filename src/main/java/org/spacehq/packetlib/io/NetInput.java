package org.spacehq.packetlib.io;

import java.io.IOException;
import java.util.UUID;

/**
 * An interface for reading network data.
 */
public interface NetInput {
    /**
     * Reads the next boolean.
     *
     * @return The next boolean.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public boolean readBoolean() throws IOException;

    /**
     * Reads the next byte.
     *
     * @return The next byte.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public byte readByte() throws IOException;

    /**
     * Reads the next unsigned byte.
     *
     * @return The next unsigned byte.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readUnsignedByte() throws IOException;

    /**
     * Reads the next short.
     *
     * @return The next short.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public short readShort() throws IOException;

    /**
     * Reads the next unsigned short.
     *
     * @return The next unsigned short.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readUnsignedShort() throws IOException;

    /**
     * Reads the next char.
     *
     * @return The next char.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public char readChar() throws IOException;

    /**
     * Reads the next integer.
     *
     * @return The next integer.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readInt() throws IOException;

    /**
     * Reads the next varint. A varint is a form of integer where only necessary bytes are written. This is done to save bandwidth.
     *
     * @return The next varint.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readVarInt() throws IOException;

    /**
     * Reads the next long.
     *
     * @return The next long.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public long readLong() throws IOException;

    /**
     * Reads the next varlong. A varlong is a form of long where only necessary bytes are written. This is done to save bandwidth.
     *
     * @return The next varlong.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public long readVarLong() throws IOException;

    /**
     * Reads the next float.
     *
     * @return The next float.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public float readFloat() throws IOException;

    /**
     * Reads the next double.
     *
     * @return The next double.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public double readDouble() throws IOException;

    /**
     * Reads the next byte array.
     *
     * @param length The length of the byte array.
     * @return The next byte array.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public byte[] readBytes(int length) throws IOException;

    /**
     * Reads as much data as possible into the given byte array.
     *
     * @param b Byte array to read to.
     * @return The amount of bytes read, or -1 if no bytes could be read.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readBytes(byte b[]) throws IOException;

    /**
     * Reads the given amount of bytes into the given array at the given offset.
     *
     * @param b      Byte array to read to.
     * @param offset Offset of the array to read to.
     * @param length Length of bytes to read.
     * @return The amount of bytes read, or -1 if no bytes could be read.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readBytes(byte b[], int offset, int length) throws IOException;

    /**
     * Reads the next short array.
     *
     * @param length The length of the short array.
     * @return The next short array.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public short[] readShorts(int length) throws IOException;

    /**
     * Reads as much data as possible into the given short array.
     *
     * @param s Short array to read to.
     * @return The amount of shorts read, or -1 if no shorts could be read.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readShorts(short s[]) throws IOException;

    /**
     * Reads the given amount of shorts into the given array at the given offset.
     *
     * @param s      Short array to read to.
     * @param offset Offset of the array to read to.
     * @param length Length of bytes to read.
     * @return The amount of shorts read, or -1 if no shorts could be read.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readShorts(short s[], int offset, int length) throws IOException;

    /**
     * Reads the next int array.
     *
     * @param length The length of the int array.
     * @return The next int array.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int[] readInts(int length) throws IOException;

    /**
     * Reads as much data as possible into the given int array.
     *
     * @param i Int array to read to.
     * @return The amount of ints read, or -1 if no ints could be read.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readInts(int i[]) throws IOException;

    /**
     * Reads the given amount of ints into the given array at the given offset.
     *
     * @param i      Int array to read to.
     * @param offset Offset of the array to read to.
     * @param length Length of bytes to read.
     * @return The amount of ints read, or -1 if no ints could be read.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readInts(int i[], int offset, int length) throws IOException;

    /**
     * Reads the next long array.
     *
     * @param length The length of the long array.
     * @return The next long array.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public long[] readLongs(int length) throws IOException;

    /**
     * Reads as much data as possible into the given long array.
     *
     * @param l Long array to read to.
     * @return The amount of longs read, or -1 if no longs could be read.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readLongs(long l[]) throws IOException;

    /**
     * Reads the given amount of longs into the given array at the given offset.
     *
     * @param l      Long array to read to.
     * @param offset Offset of the array to read to.
     * @param length Length of bytes to read.
     * @return The amount of longs read, or -1 if no longs could be read.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readLongs(long l[], int offset, int length) throws IOException;

    /**
     * Reads the next string.
     *
     * @return The next string.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public String readString() throws IOException;

    /**
     * Reads the next UUID.
     *
     * @return The next UUID.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public UUID readUUID() throws IOException;

    /**
     * Gets the number of available bytes.
     *
     * @return The number of available bytes.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int available() throws IOException;
}
