package org.geysermc.mcprotocollib.network.packet;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.network.codec.PacketCodecHelper;
import org.geysermc.mcprotocollib.network.codec.PacketDefinition;
import org.geysermc.mcprotocollib.network.codec.PacketSerializer;

public class BufferedPacket implements Packet, PacketSerializer<BufferedPacket, PacketCodecHelper> {
    private final Class<? extends Packet> packetClass;
    private final byte[] buf;

    public BufferedPacket(Class<? extends Packet> packetClass, byte[] buf) {
        this.packetClass = packetClass;
        this.buf = buf;
    }

    public Class<? extends Packet> getPacketClass() {
        return packetClass;
    }

    @Override
    public boolean isPriority() {
        return true;
    }

    @Override
    public void serialize(ByteBuf buf, PacketCodecHelper helper, BufferedPacket packet) {
        buf.writeBytes(this.buf);
    }

    @Override
    public BufferedPacket deserialize(ByteBuf buf, PacketCodecHelper helper, PacketDefinition<BufferedPacket, PacketCodecHelper> definition) {
        byte[] array = new byte[buf.readableBytes()];
        buf.readBytes(array);

        return new BufferedPacket(definition.getPacketClass(), array);
    }
}
