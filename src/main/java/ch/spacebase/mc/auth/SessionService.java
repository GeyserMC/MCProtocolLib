package ch.spacebase.mc.auth;

import ch.spacebase.mc.auth.exceptions.AuthenticationException;
import ch.spacebase.mc.auth.exceptions.AuthenticationUnavailableException;
import ch.spacebase.mc.auth.request.JoinServerRequest;
import ch.spacebase.mc.auth.response.HasJoinedResponse;
import ch.spacebase.mc.auth.response.Response;
import ch.spacebase.mc.util.URLUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SessionService {

	private static final String BASE_URL = "https://sessionserver.mojang.com/session/minecraft/";
	private static final URL JOIN_URL = URLUtils.constantURL(BASE_URL + "join");
	private static final URL CHECK_URL = URLUtils.constantURL(BASE_URL + "hasJoined");

	public void joinServer(GameProfile profile, String authenticationToken, String serverId) throws AuthenticationException {
		JoinServerRequest request = new JoinServerRequest(authenticationToken, profile.getId(), serverId);
		URLUtils.makeRequest(JOIN_URL, request, Response.class);
	}

	public GameProfile hasJoinedServer(GameProfile user, String serverId) throws AuthenticationUnavailableException {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("username", user.getName());
		arguments.put("serverId", serverId);
		URL url = URLUtils.concatenateURL(CHECK_URL, URLUtils.buildQuery(arguments));
		try {
			HasJoinedResponse e = URLUtils.makeRequest(url, null, HasJoinedResponse.class);
			return e != null && e.getId() != null ? new GameProfile(e.getId(), user.getName()) : null;
		} catch(AuthenticationUnavailableException var6) {
			throw var6;
		} catch(AuthenticationException var7) {
			return null;
		}
	}
	
	@Override
	public String toString() {
		return "SessionService{}";
	}

}
