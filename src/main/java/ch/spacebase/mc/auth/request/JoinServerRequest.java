package ch.spacebase.mc.auth.request;

@SuppressWarnings("unused")
public class JoinServerRequest {

	private String accessToken;
	private String selectedProfile;
	private String serverId;

	public JoinServerRequest(String accessToken, String id, String serverId) {
		this.accessToken = accessToken;
		this.selectedProfile = id;
		this.serverId = serverId;
	}

}
