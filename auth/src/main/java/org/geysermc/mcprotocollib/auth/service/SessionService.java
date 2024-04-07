package org.geysermc.mcprotocollib.auth.service;

import org.geysermc.mcprotocollib.auth.data.GameProfile;
import org.geysermc.mcprotocollib.auth.exception.profile.ProfileException;
import org.geysermc.mcprotocollib.auth.exception.profile.ProfileLookupException;
import org.geysermc.mcprotocollib.auth.exception.profile.ProfileNotFoundException;
import org.geysermc.mcprotocollib.auth.exception.request.RequestException;
import org.geysermc.mcprotocollib.auth.util.HTTP;
import org.geysermc.mcprotocollib.auth.util.UUIDSerializer;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service used for session-related queries.
 */
public class SessionService extends Service {
    private static final URI DEFAULT_BASE_URI = URI.create("https://sessionserver.mojang.com/session/minecraft/");
    private static final String JOIN_ENDPOINT = "join";
    private static final String HAS_JOINED_ENDPOINT = "hasJoined";
    private static final String PROFILE_ENDPOINT = "profile";

    /**
     * Creates a new SessionService instance.
     */
    public SessionService() {
        super(DEFAULT_BASE_URI);
    }

    /**
     * Calculates the server ID from a base string, public key, and secret key.
     *
     * @param base      Base server ID to use.
     * @param publicKey Public key to use.
     * @param secretKey Secret key to use.
     * @return The calculated server ID.
     * @throws IllegalStateException If the server ID hash algorithm is unavailable.
     */
    public String getServerId(String base, PublicKey publicKey, SecretKey secretKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(base.getBytes(StandardCharsets.ISO_8859_1));
            digest.update(secretKey.getEncoded());
            digest.update(publicKey.getEncoded());
            return new BigInteger(digest.digest()).toString(16);
        } catch(NoSuchAlgorithmException e) {
            throw new IllegalStateException("Server ID hash algorithm unavailable.", e);
        }
    }

    /**
     * Joins a server.
     *
     * @param profile             Profile to join the server with.
     * @param authenticationToken Authentication token to join the server with.
     * @param serverId            ID of the server to join.
     * @throws RequestException If an error occurs while making the request.
     */
    public void joinServer(GameProfile profile, String authenticationToken, String serverId) throws RequestException {
        JoinServerRequest request = new JoinServerRequest(authenticationToken, profile.getId(), serverId);
        HTTP.makeRequest(this.getProxy(), this.getEndpointUri(JOIN_ENDPOINT), request, null);
    }

    /**
     * Gets the profile of the given user if they are currently logged in to the given server.
     *
     * @param name     Name of the user to get the profile of.
     * @param serverId ID of the server to check if they're logged in to.
     * @return The profile of the given user, or null if they are not logged in to the given server.
     * @throws RequestException If an error occurs while making the request.
     */
    public GameProfile getProfileByServer(String name, String serverId) throws RequestException {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("username", name);
        queryParams.put("serverId", serverId);

        HasJoinedResponse response = HTTP.makeRequest(this.getProxy(), this.getEndpointUri(HAS_JOINED_ENDPOINT, queryParams), null, HasJoinedResponse.class);
        if(response != null && response.id != null) {
            GameProfile result = new GameProfile(response.id, name);
            result.setProperties(response.properties);
            return result;
        } else {
            return null;
        }
    }

    /**
     * Fills in the properties of a profile.
     *
     * @param profile Profile to fill in the properties of.
     * @throws ProfileException If the property lookup fails.
     */
    public void fillProfileProperties(GameProfile profile) throws ProfileException {
        if(profile.getId() == null) {
            return;
        }

        try {
            MinecraftProfileResponse response = HTTP.makeRequest(this.getProxy(), this.getEndpointUri(PROFILE_ENDPOINT + "/" + UUIDSerializer.fromUUID(profile.getId()), Collections.singletonMap("unsigned", "false")), null, MinecraftProfileResponse.class);
            if(response == null) {
                throw new ProfileNotFoundException("Couldn't fetch profile properties for " + profile + " as the profile does not exist.");
            }

            profile.setProperties(response.properties);
        } catch(RequestException e) {
            throw new ProfileLookupException("Couldn't look up profile properties for " + profile + ".", e);
        }
    }

    @Override
    public String toString() {
        return "SessionService{}";
    }

    private static class JoinServerRequest {
        private String accessToken;
        private UUID selectedProfile;
        private String serverId;

        protected JoinServerRequest(String accessToken, UUID selectedProfile, String serverId) {
            this.accessToken = accessToken;
            this.selectedProfile = selectedProfile;
            this.serverId = serverId;
        }
    }

    private static class HasJoinedResponse {
        public UUID id;
        public List<GameProfile.Property> properties;
    }

    private static class MinecraftProfileResponse {
        public UUID id;
        public String name;
        public List<GameProfile.Property> properties;
    }
}
