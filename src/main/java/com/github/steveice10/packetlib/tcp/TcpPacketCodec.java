package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.codec.PacketCodecHelper;
import com.github.steveice10.packetlib.codec.PacketDefinition;
import com.github.steveice10.packetlib.event.session.PacketErrorEvent;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class TcpPacketCodec extends ByteToMessageCodec<Packet> {
    private final Session session;
    private final boolean client;

    public TcpPacketCodec(Session session, boolean client) {
        this.session = session;
        this.client = client;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf buf) throws Exception {
        int initial = buf.writerIndex();

        PacketProtocol packetProtocol = this.session.getPacketProtocol();
        PacketCodecHelper codecHelper = this.session.getCodecHelper();
        try {
            int packetId = this.client ? packetProtocol.getServerboundId(packet) : packetProtocol.getClientboundId(packet);
            PacketDefinition definition = this.client ? packetProtocol.getServerboundDefinition(packetId) : packetProtocol.getClientboundDefinition(packetId);
            
            packetProtocol.getPacketHeader().writePacketId(buf, codecHelper, packetId);
            definition.getSerializer().serialize(buf, codecHelper, packet);
        } catch (Throwable t) {
            // Reset writer index to make sure incomplete data is not written out.
            buf.writerIndex(initial);

            PacketErrorEvent e = new PacketErrorEvent(this.session, t);
            this.session.callEvent(e);
            if (!e.shouldSuppress()) {
                throw t;
            }
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int initial = buf.readerIndex();

        PacketProtocol packetProtocol = this.session.getPacketProtocol();
        PacketCodecHelper codecHelper = this.session.getCodecHelper();
        try {
            int id = packetProtocol.getPacketHeader().readPacketId(buf, codecHelper);
            if (id == -1) {
                buf.readerIndex(initial);
                return;
            }

            Packet packet = this.client ? packetProtocol.createClientboundPacket(id, buf, codecHelper) : packetProtocol.createServerboundPacket(id, buf, codecHelper);

            if (buf.readableBytes() > 0) {
                throw new IllegalStateException("Packet \"" + packet.getClass().getSimpleName() + "\" not fully read.");
            }

            out.add(packet);
        } catch (Throwable t) {
            // Advance buffer to end to make sure remaining data in this packet is skipped.
            buf.readerIndex(buf.readerIndex() + buf.readableBytes());

            PacketErrorEvent e = new PacketErrorEvent(this.session, t);
            this.session.callEvent(e);
            if (!e.shouldSuppress()) {
                throw t;
            }
        }
    }
}
