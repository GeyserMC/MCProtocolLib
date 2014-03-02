package org.spacehq.mc.auth;

import com.google.gson.Gson;
import org.spacehq.mc.auth.exception.AuthenticationException;
import org.spacehq.mc.auth.exception.AuthenticationUnavailableException;
import org.spacehq.mc.auth.exception.PropertyException;
import org.spacehq.mc.auth.exception.TextureDecodeException;
import org.spacehq.mc.auth.request.JoinServerRequest;
import org.spacehq.mc.auth.response.HasJoinedResponse;
import org.spacehq.mc.auth.response.MinecraftTexturesPayload;
import org.spacehq.mc.auth.response.Response;
import org.spacehq.mc.util.Base64;
import org.spacehq.mc.util.IOUtils;
import org.spacehq.mc.util.URLUtils;

import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class SessionService {

	private static final String BASE_URL = "https://sessionserver.mojang.com/session/minecraft/";
	private static final URL JOIN_URL = URLUtils.constantURL(BASE_URL + "join");
	private static final URL CHECK_URL = URLUtils.constantURL(BASE_URL + "hasJoined");

	private static final PublicKey SIGNATURE_KEY;

	static {
		try {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(IOUtils.toByteArray(SessionService.class.getResourceAsStream("/yggdrasil_session_pubkey.der")));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			SIGNATURE_KEY = keyFactory.generatePublic(spec);
		} catch(Exception var4) {
			throw new ExceptionInInitializerError("Missing/invalid yggdrasil public key.");
		}
	}

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
			HasJoinedResponse response = URLUtils.makeRequest(url, null, HasJoinedResponse.class);
			if(response != null && response.getId() != null) {
				GameProfile result = new GameProfile(response.getId(), user.getName());
				if(response.getProperties() != null) {
					for(ProfileProperty prop : response.getProperties()) {
						result.getProperties().put(prop.getName(), prop);
					}
				}

				return result;
			} else {
				return null;
			}
		} catch(AuthenticationUnavailableException var6) {
			throw var6;
		} catch(AuthenticationException var7) {
			return null;
		}
	}

	public Map<String, ProfileTexture> getTextures(GameProfile profile) throws PropertyException {
		ProfileProperty textures = profile.getProperties().get("textures");
		if(textures != null && textures.hasSignature() && textures.isSignatureValid(SIGNATURE_KEY)) {
			MinecraftTexturesPayload result;
			try {
				String e = new String(Base64.decode(textures.getValue().getBytes("UTF-8")));
				result = new Gson().fromJson(e, MinecraftTexturesPayload.class);
			} catch(Exception e) {
				throw new TextureDecodeException("Could not decode texture payload.", e);
			}

			if(result.getProfileId() != null && result.getProfileId().equals(profile.getId())) {
				if(result.getProfileName() != null && result.getProfileName().equals(profile.getName())) {
					return result.getTextures() == null ? new HashMap<String, ProfileTexture>() : result.getTextures();
				}
			}
		}

		return new HashMap<String, ProfileTexture>();
	}

	@Override
	public String toString() {
		return "SessionService{}";
	}

}
