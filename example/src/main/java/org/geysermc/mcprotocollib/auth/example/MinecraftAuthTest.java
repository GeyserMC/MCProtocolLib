package org.geysermc.mcprotocollib.auth.example;

import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.java.JavaAuthManager;
import net.raphimc.minecraftauth.java.model.MinecraftProfile;
import net.raphimc.minecraftauth.java.model.MinecraftToken;
import net.raphimc.minecraftauth.msa.model.MsaCredentials;
import net.raphimc.minecraftauth.msa.service.impl.CredentialsMsaAuthService;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.auth.SessionService;
import org.geysermc.mcprotocollib.network.ProxyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinecraftAuthTest {
    private static final Logger log = LoggerFactory.getLogger(MinecraftAuthTest.class);
    private static final String EMAIL = "Username@mail.com";
    private static final String PASSWORD = "Password";
    private static final boolean REQUIRE_SECURE_TEXTURES = true;

    private static final ProxyInfo PROXY = null;

    public static void main(String[] args) {
        auth();
    }

    private static void auth() {
        SessionService service = new SessionService();
        service.setProxy(PROXY);

        JavaAuthManager authManager;
        try {
            authManager = JavaAuthManager.create(MinecraftAuth.createHttpClient()).login(CredentialsMsaAuthService::new, new MsaCredentials(EMAIL, PASSWORD));
            authManager.getMinecraftToken().refresh(); // Preload the Minecraft token
            authManager.getMinecraftProfile().refresh(); // Preload the Minecraft profile
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        MinecraftProfile mcProfile = authManager.getMinecraftProfile().getCached();
        MinecraftToken mcToken = authManager.getMinecraftToken().getCached();
        GameProfile profile = new GameProfile(mcProfile.getId(), mcProfile.getName());
        try {
            service.fillProfileProperties(profile);

            log.info("Selected Profile: {}", profile);
            log.info("Selected Profile Textures: {}", profile.getTextures(REQUIRE_SECURE_TEXTURES));
            log.info("Access Token: {}", mcToken.getToken());
            log.info("Expire Time: {}", mcToken.getExpireTimeMs());
        } catch (Exception e) {
            log.error("Failed to get properties and textures of selected profile {}.", profile, e);
        }
    }
}
