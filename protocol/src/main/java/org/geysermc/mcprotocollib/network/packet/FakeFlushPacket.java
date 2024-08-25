package org.geysermc.mcprotocollib.network.packet;

/**
 * Packet only exists to be writeAndFlushed to ensure all packets that came before are encoded and written out.
 */
public class FakeFlushPacket implements Packet {
    public static final FakeFlushPacket INSTANCE = new FakeFlushPacket();

    private FakeFlushPacket() {
    }
}
