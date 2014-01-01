package ch.spacebase.mc.auth.request;

import ch.spacebase.mc.auth.UserAuthentication;

@SuppressWarnings("unused")
public class AuthenticationRequest {

	private Agent agent;
	private String username;
	private String password;
	private String clientToken;
	private boolean requestUser = true;

	public AuthenticationRequest(UserAuthentication auth, String username, String password) {
		this.agent = new Agent("Minecraft", 1);
		this.username = username;
		this.clientToken = auth.getClientToken();
		this.password = password;
	}

}
