package org.spacehq.mc.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.spacehq.mc.auth.exception.*;
import org.spacehq.mc.auth.properties.Property;
import org.spacehq.mc.auth.request.JoinServerRequest;
import org.spacehq.mc.auth.response.HasJoinedResponse;
import org.spacehq.mc.auth.response.MinecraftProfilePropertiesResponse;
import org.spacehq.mc.auth.response.MinecraftTexturesPayload;
import org.spacehq.mc.auth.response.Response;
import org.spacehq.mc.auth.serialize.UUIDSerializer;
import org.spacehq.mc.util.Base64;
import org.spacehq.mc.util.IOUtils;
import org.spacehq.mc.util.URLUtils;

import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class SessionService {

	private static final String BASE_URL = "https://sessionserver.mojang.com/session/minecraft/";
	private static final URL JOIN_URL = URLUtils.constantURL(BASE_URL + "join");
	private static final URL CHECK_URL = URLUtils.constantURL(BASE_URL + "hasJoined");

	private static final PublicKey SIGNATURE_KEY;
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDSerializer()).create();

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
					result.getProperties().putAll(response.getProperties());
				}

				return result;
			} else {
				return null;
			}
		} catch(AuthenticationUnavailableException e) {
			throw e;
		} catch(AuthenticationException e) {
			return null;
		}
	}

	public Map<ProfileTextureType, ProfileTexture> getTextures(GameProfile profile, boolean requireSecure) throws PropertyException {
		Property textures = profile.getProperties().get("textures");
		if(textures != null) {
			if(!textures.hasSignature()) {
				throw new ProfileTextureException("Signature is missing from textures payload.");
			}

			if(!textures.isSignatureValid(SIGNATURE_KEY)) {
				throw new ProfileTextureException("Textures payload has been tampered with. (signature invalid)");
			}

			MinecraftTexturesPayload result;
			try {
				String json = new String(Base64.decode(textures.getValue().getBytes("UTF-8")));
				result = GSON.fromJson(json, MinecraftTexturesPayload.class);
			} catch(Exception e) {
				throw new ProfileTextureException("Could not decode texture payload.", e);
			}

			if(result.getProfileId() == null || !result.getProfileId().equals(profile.getId())) {
				throw new ProfileTextureException("Decrypted textures payload was for another user. (expected id " + profile.getId() + " but was for " + result.getProfileId() + ")");
			}

				if(result.getProfileName() == null || !result.getProfileName().equals(profile.getName())) {
					throw new ProfileTextureException("Decrypted textures payload was for another user. (expected name " + profile.getName() + " but was for " + result.getProfileName() + ")");
				}
					if(requireSecure) {
						if(result.isPublic()) {
							throw new ProfileTextureException("Decrypted textures payload was public when secure data is required.");
						}

						Calendar limit = Calendar.getInstance();
						limit.add(5, -1);
						Date validFrom = new Date(result.getTimestamp());
						if(validFrom.before(limit.getTime())) {
							throw new ProfileTextureException("Decrypted textures payload is too old. (" + validFrom + ", needs to be at least " + limit + ")");
						}
					}

					return result.getTextures() == null ? new HashMap<ProfileTextureType, ProfileTexture>() : result.getTextures();
		}

		return new HashMap<ProfileTextureType, ProfileTexture>();
	}

	public GameProfile fillProfileProperties(GameProfile profile) throws ProfileException {
		if(profile.getId() == null) {
			return profile;
		}

		try {
			URL url = URLUtils.constantURL("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDSerializer.fromUUID(profile.getId()));
			MinecraftProfilePropertiesResponse response = URLUtils.makeRequest(url, null, MinecraftProfilePropertiesResponse.class);
			if(response == null) {
				throw new ProfileNotFoundException("Couldn't fetch profile properties for " + profile + " as the profile does not exist.");
			}

			GameProfile result = new GameProfile(response.getId(), response.getName());
			result.getProperties().putAll(response.getProperties());
			profile.getProperties().putAll(response.getProperties());
			return result;
		} catch(AuthenticationException e) {
			throw new ProfileLookupException("Couldn't look up profile properties for " + profile, e);
		}
	}

	@Override
	public String toString() {
		return "SessionService{}";
	}

}
