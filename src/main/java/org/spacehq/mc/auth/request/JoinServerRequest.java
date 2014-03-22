package org.spacehq.mc.auth.request;

import java.util.UUID;

@SuppressWarnings("unused")
public class JoinServerRequest {

	private String accessToken;
	private UUID selectedProfile;
	private String serverId;

	public JoinServerRequest(String accessToken, UUID id, String serverId) {
		this.accessToken = accessToken;
		this.selectedProfile = id;
		this.serverId = serverId;
	}

}
