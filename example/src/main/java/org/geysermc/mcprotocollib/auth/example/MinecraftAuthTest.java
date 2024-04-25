package org.geysermc.mcprotocollib.auth.example;

import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.step.java.StepMCProfile;
import net.raphimc.minecraftauth.step.java.StepMCToken;
import net.raphimc.minecraftauth.step.java.session.StepFullJavaSession;
import net.raphimc.minecraftauth.step.msa.StepCredentialsMsaCode;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.auth.SessionService;

import java.net.Proxy;

public class MinecraftAuthTest {
    private static final String EMAIL = "Username@mail.com";
    private static final String PASSWORD = "Password";
    private static final boolean REQUIRE_SECURE_TEXTURES = true;

    private static final Proxy PROXY = Proxy.NO_PROXY;

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

            System.out.println("Selected Profile: " + profile);
            System.out.println("Selected Profile Textures: " + profile.getTextures(REQUIRE_SECURE_TEXTURES));
            System.out.println("Access Token: " + mcToken.getAccessToken());
            System.out.println("Expire Time: " + mcToken.getExpireTimeMs());
        } catch (Exception e) {
            System.err.println("Failed to get properties and textures of selected profile " + profile + ".");
            e.printStackTrace();
        }
    }
}
