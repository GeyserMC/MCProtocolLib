package org.spacehq.packetlib;

public interface ConnectTimeoutHandlerContainer {
	public int getConnectTimeout();

	public TimeoutHandler getConnectTimeoutHandler();
}
