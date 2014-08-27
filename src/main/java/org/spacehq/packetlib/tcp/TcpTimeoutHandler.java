package org.spacehq.packetlib.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.TimeoutHandler;

public class TcpTimeoutHandler extends ReadTimeoutHandler {
	private Session session;
	private TimeoutHandler timeoutHandler;

	public TcpTimeoutHandler(Session session, int timeoutSeconds, TimeoutHandler timeoutHandler) {
		super(timeoutSeconds);
		this.session = session;
		this.timeoutHandler = timeoutHandler;
	}

	@Override
	protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
		if(this.timeoutHandler != null) {
			this.timeoutHandler.onTimeout(this.session);
		}
	}
}
