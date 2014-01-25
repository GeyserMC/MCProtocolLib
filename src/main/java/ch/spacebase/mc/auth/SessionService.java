package ch.spacebase.mc.auth;

import ch.spacebase.mc.auth.exceptions.AuthenticationException;
import ch.spacebase.mc.auth.exceptions.AuthenticationUnavailableException;
import ch.spacebase.mc.auth.request.JoinServerRequest;
import ch.spacebase.mc.auth.response.HasJoinedResponse;
import ch.spacebase.mc.auth.response.MinecraftTexturesPayload;
import ch.spacebase.mc.auth.response.Response;
import ch.spacebase.mc.util.Base64;
import ch.spacebase.mc.util.IOUtils;
import ch.spacebase.mc.util.URLUtils;

import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

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
	
	public Map<String, ProfileTexture> getTextures(GameProfile profile) {
		ProfileProperty textures = profile.getProperties().get("textures");
		if(textures == null) {
			return new HashMap<String, ProfileTexture>();
		} else if(!textures.hasSignature()) {
			System.err.println("Signature is missing from textures payload.");
			return new HashMap<String, ProfileTexture>();
		} else if(!textures.isSignatureValid(SIGNATURE_KEY)) {
			System.err.println("Textures payload has been tampered with. (signature invalid)");
			return new HashMap<String, ProfileTexture>();
		} else {
			MinecraftTexturesPayload result;
			try {
				String e = new String(Base64.decode(textures.getValue().getBytes("UTF-8")));
				result = new Gson().fromJson(e, MinecraftTexturesPayload.class);
			} catch(Exception e) {
				System.err.println("Could not decode textures payload.");
				e.printStackTrace();
				return new HashMap<String, ProfileTexture>();
			}

			if(result.getProfileId() != null && result.getProfileId().equals(profile.getId())) {
				if(result.getProfileName() != null && result.getProfileName().equals(profile.getName())) {
					return result.getTextures() == null ? new HashMap<String, ProfileTexture>() : result.getTextures();
				} else {
					System.err.println("Decrypted textures payload was for another user. (expected name " + profile.getName() + " but was for " + result.getProfileName() + ")");
					return new HashMap<String, ProfileTexture>();
				}
			} else {
				System.err.println("Decrypted textures payload was for another user. (expected id " + profile.getId() + " but was for " + result.getProfileId() + ")");
				return new HashMap<String, ProfileTexture>();
			}
		}
	}

	@Override
	public String toString() {
		return "SessionService{}";
	}

}
