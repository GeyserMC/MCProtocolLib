package org.geysermc.mcprotocollib.network.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.flow.FlowControlHandler;

public class TcpFlowControlHandler extends FlowControlHandler {
    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().config().isAutoRead()) {
            super.read(ctx);
        }
    }
}
