package com.github.steveice10.packetlib.packet;

import com.github.steveice10.packetlib.codec.PacketCodecHelper;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * The header of a protocol's packets.
 */
public interface PacketHeader {
    /**
     * Gets whether the header's length value can vary in size.
     *
     * @return Whether the header's length value can vary in size.
     */
    public boolean isLengthVariable();

    /**
     * Gets the size of the header's length value.
     *
     * @return The length value's size.
     */
    public int getLengthSize();

    /**
     * Gets the size of the header's length value.
     *
     * @param length Length value to get the size of.
     * @return The length value's size.
     */
    public int getLengthSize(int length);

    /**
     * Reads the length of a packet from the given input.
     *
     * @param buf         Buffer to read from.
     * @param codecHelper The codec helper.
     * @param available   Number of packet bytes available after the length.
     * @return The resulting packet length.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readLength(ByteBuf buf, PacketCodecHelper codecHelper, int available) throws IOException;

    /**
     * Writes the length of a packet to the given output.
     *
     * @param buf         Buffer to write to.
     * @param codecHelper The codec helper.
     * @param length      Length to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeLength(ByteBuf buf, PacketCodecHelper codecHelper, int length) throws IOException;

    /**
     * Reads the ID of a packet from the given input.
     *
     * @param buf         Buffer to read from.
     * @param codecHelper The codec helper.
     * @return The resulting packet ID, or -1 if the packet should not be read yet.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public int readPacketId(ByteBuf buf, PacketCodecHelper codecHelper) throws IOException;

    /**
     * Writes the ID of a packet to the given output.
     *
     * @param buf         Buffer to write to.
     * @param codecHelper The codec helper.
     * @param packetId    Packet ID to write.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writePacketId(ByteBuf buf, PacketCodecHelper codecHelper, int packetId) throws IOException;
}
