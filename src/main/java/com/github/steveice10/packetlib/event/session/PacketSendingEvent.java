package com.github.steveice10.packetlib.event.session;

import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.Getter;
import lombok.Setter;

/**
 * Called when the session is sending a packet.
 */
@Getter
public class PacketSendingEvent implements SessionEvent {
    private final Session session;
    @Setter
    private Packet packet;
    @Setter
    private boolean cancelled = false;

    public PacketSendingEvent(Session session, Packet packet) {
        this.session = session;
        this.packet = packet;
    }

    @Override
    public void call(SessionListener listener) {
        listener.packetSending(this);
    }
}
