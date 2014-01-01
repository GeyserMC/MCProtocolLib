package ch.spacebase.mc.auth;

import ch.spacebase.mc.auth.SessionService;
import ch.spacebase.mc.auth.UserAuthentication;

public class AuthenticationService {

	private String clientToken;

	public AuthenticationService(String clientToken) {
		this.clientToken = clientToken;
	}

	public UserAuthentication createUserAuthentication() {
		return new UserAuthentication(this);
	}

	public SessionService createMinecraftSessionService() {
		return new SessionService(this);
	}

	public String getClientToken() {
		return this.clientToken;
	}
	
}
