package org.geysermc.mcprotocollib.network.packet;

public class PacketCancelException extends RuntimeException {
    public PacketCancelException() {
        super("This packet should not be sent out!");
    }
}
