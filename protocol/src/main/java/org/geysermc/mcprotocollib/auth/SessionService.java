package org.geysermc.mcprotocollib.auth;

import lombok.Getter;
import lombok.Setter;
import org.geysermc.mcprotocollib.auth.util.HTTPUtils;
import org.geysermc.mcprotocollib.network.ProxyInfo;
import org.geysermc.mcprotocollib.auth.util.UUIDUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.List;
import java.util.UUID;

/**
 * Service used for session-related queries.
 */
@Setter
@Getter
public class SessionService {
    private static final URI JOIN_ENDPOINT = URI.create("https://sessionserver.mojang.com/session/minecraft/join");
    private static final String HAS_JOINED_ENDPOINT = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=%s&serverId=%s";
    private static final String PROFILE_ENDPOINT = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
    private ProxyInfo proxy;

    /**
     * Calculates the server ID from a base string, public key, and secret key.
     *
     * @param base Base server ID to use.
     * @param publicKey Public key to use.
     * @param secretKey Secret key to use.
     * @return The calculated server ID.
     * @throws IllegalStateException If the server ID hash algorithm is unavailable.
     */
    public static String getServerId(String base, PublicKey publicKey, SecretKey secretKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(base.getBytes(StandardCharsets.ISO_8859_1));
            digest.update(secretKey.getEncoded());
            digest.update(publicKey.getEncoded());
            return new BigInteger(digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Server ID hash algorithm unavailable.", e);
        }
    }

    /**
     * Joins a server.
     *
     * @param profile Profile to join the server with.
     * @param authenticationToken Authentication token to join the server with.
     * @param serverId ID of the server to join.
     * @throws IOException If an error occurs while making the request.
     */
    public void joinServer(GameProfile profile, String authenticationToken, String serverId) throws IOException {
        JoinServerRequest request = new JoinServerRequest(authenticationToken, profile.getId(), serverId);
        HTTPUtils.makeRequest(this.getProxy(), JOIN_ENDPOINT, request, null);
    }

    /**
     * Gets the profile of the given user if they are currently logged in to the given server.
     *
     * @param name Name of the user to get the profile of.
     * @param serverId ID of the server to check if they're logged in to.
     * @return The profile of the given user, or null if they are not logged in to the given server.
     * @throws IOException If an error occurs while making the request.
     */
    public GameProfile getProfileByServer(String name, String serverId) throws IOException {
        HasJoinedResponse response = HTTPUtils.makeRequest(this.getProxy(),
                URI.create(String.format(HAS_JOINED_ENDPOINT,
                        URLEncoder.encode(name, StandardCharsets.UTF_8),
                        URLEncoder.encode(serverId, StandardCharsets.UTF_8))),
                null, HasJoinedResponse.class);
        if (response != null && response.id != null) {
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
     * @throws IOException If the property lookup fails.
     */
    public void fillProfileProperties(GameProfile profile) throws IOException {
        if (profile.getId() == null) {
            return;
        }

        MinecraftProfileResponse response = HTTPUtils.makeRequest(this.getProxy(), URI.create(String.format(PROFILE_ENDPOINT, UUIDUtils.convertToNoDashes(profile.getId()))), null, MinecraftProfileResponse.class);
        if (response == null) {
            throw new IllegalStateException("Couldn't fetch profile properties for " + profile + " as the profile does not exist.");
        }

        profile.setProperties(response.properties);
    }

    @Override
    public String toString() {
        return "SessionService{}";
    }

    private record JoinServerRequest(String accessToken, UUID selectedProfile, String serverId) {
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
