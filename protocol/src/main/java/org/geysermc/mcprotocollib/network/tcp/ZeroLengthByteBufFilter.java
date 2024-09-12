package org.geysermc.mcprotocollib.network.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class ZeroLengthByteBufFilter extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf byteBuf) {
            // Only forward non-zero-length ByteBufs
            if (byteBuf.readableBytes() > 0) {
                super.write(ctx, msg, promise);
            } else {
                // Release the buffer if not forwarded
                byteBuf.release();
                // Optionally notify the promise of success
                promise.setSuccess();
            }
        } else {
            // If it's not a ByteBuf, just pass it through
            super.write(ctx, msg, promise);
        }
    }
}
