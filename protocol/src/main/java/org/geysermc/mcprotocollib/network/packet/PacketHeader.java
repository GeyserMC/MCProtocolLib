package org.geysermc.mcprotocollib.network.packet;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;

/**
 * The header of a protocol's packets.
 */
public interface PacketHeader {
    /**
     * Gets whether the header's length value can vary in size.
     *
     * @return Whether the header's length value can vary in size.
     */
    boolean isLengthVariable();

    /**
     * Gets the size of the header's length value.
     *
     * @return The length value's size.
     */
    int getLengthSize();

    /**
     * Gets the size of the header's length value.
     *
     * @param length Length value to get the size of.
     * @return The length value's size.
     */
    int getLengthSize(int length);

    /**
     * Reads the length of a packet from the given input.
     *
     * @param buf Buffer to read from.
     * @param codecHelper The codec helper.
     * @param available Number of packet bytes available after the length.
     * @return The resulting packet length.
     */
    int readLength(ByteBuf buf, PacketCodecHelper codecHelper, int available);

    /**
     * Writes the length of a packet to the given output.
     *
     * @param buf Buffer to write to.
     * @param codecHelper The codec helper.
     * @param length Length to write.
     */
    void writeLength(ByteBuf buf, PacketCodecHelper codecHelper, int length);

    /**
     * Reads the ID of a packet from the given input.
     *
     * @param buf Buffer to read from.
     * @param codecHelper The codec helper.
     * @return The resulting packet ID, or -1 if the packet should not be read yet.
     */
    int readPacketId(ByteBuf buf, PacketCodecHelper codecHelper);

    /**
     * Writes the ID of a packet to the given output.
     *
     * @param buf Buffer to write to.
     * @param codecHelper The codec helper.
     * @param packetId Packet ID to write.
     */
    void writePacketId(ByteBuf buf, PacketCodecHelper codecHelper, int packetId);
}
