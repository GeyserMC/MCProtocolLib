package org.geysermc.mcprotocollib.network.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.codec.CodecByteBuf;
import org.geysermc.mcprotocollib.network.codec.PacketDefinition;
import org.geysermc.mcprotocollib.network.event.session.PacketErrorEvent;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;

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
    public void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf buf) {
        int initial = buf.writerIndex();

        PacketProtocol packetProtocol = this.session.getPacketProtocol();
        try {
            int packetId = this.client ? packetProtocol.getServerboundId(packet) : packetProtocol.getClientboundId(packet);
            PacketDefinition definition = this.client ? packetProtocol.getServerboundDefinition(packetId) : packetProtocol.getClientboundDefinition(packetId);

            CodecByteBuf codecBuf = packetProtocol.getByteBufWrapper().wrap(buf);
            packetProtocol.getPacketHeader().writePacketId(codecBuf, packetId);
            definition.getSerializer().serialize(codecBuf, packet);
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
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
        int initial = buf.readerIndex();

        PacketProtocol<?> packetProtocol = this.session.getPacketProtocol();
        CodecByteBuf codecBuf = packetProtocol.getByteBufWrapper().wrap(buf);
        try {
            int id = packetProtocol.getPacketHeader().readPacketId(codecBuf);
            if (id == -1) {
                buf.readerIndex(initial);
                return;
            }

            Packet packet = this.client ? packetProtocol.createClientboundPacket(id, buf) : packetProtocol.createServerboundPacket(id, buf);

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
