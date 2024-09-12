package org.geysermc.mcprotocollib.network.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class DropPacketFilter extends ChannelOutboundHandlerAdapter {
    public static final DropPacket DROP_PACKET = new DropPacket();

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg == DROP_PACKET) {
            promise.setSuccess();
        } else {
            super.write(ctx, msg, promise);
        }
    }

    public static class DropPacket {
        private DropPacket() {
        }
    }
}
