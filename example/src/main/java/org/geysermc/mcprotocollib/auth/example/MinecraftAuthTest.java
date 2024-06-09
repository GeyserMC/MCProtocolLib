package org.geysermc.mcprotocollib.auth.example;

import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.step.java.StepMCProfile;
import net.raphimc.minecraftauth.step.java.StepMCToken;
import net.raphimc.minecraftauth.step.java.session.StepFullJavaSession;
import net.raphimc.minecraftauth.step.msa.StepCredentialsMsaCode;
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

        StepFullJavaSession.FullJavaSession fullJavaSession;
        try {
            fullJavaSession = MinecraftAuth.JAVA_CREDENTIALS_LOGIN.getFromInput(
                    MinecraftAuth.createHttpClient(),
                    new StepCredentialsMsaCode.MsaCredentials(EMAIL, PASSWORD));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        StepMCProfile.MCProfile mcProfile = fullJavaSession.getMcProfile();
        StepMCToken.MCToken mcToken = mcProfile.getMcToken();
        GameProfile profile = new GameProfile(mcProfile.getId(), mcProfile.getName());
        try {
            service.fillProfileProperties(profile);

            log.info("Selected Profile: {}", profile);
            log.info("Selected Profile Textures: {}", profile.getTextures(REQUIRE_SECURE_TEXTURES));
            log.info("Access Token: {}", mcToken.getAccessToken());
            log.info("Expire Time: {}", mcToken.getExpireTimeMs());
        } catch (Exception e) {
            log.error("Failed to get properties and textures of selected profile {}.", profile, e);
        }
    }
}
