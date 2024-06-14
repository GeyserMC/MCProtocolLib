package org.geysermc.mcprotocollib.auth.util;

import java.net.IDN;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TextureUrlChecker {
    private static final Set<String> ALLOWED_SCHEMES = Set.of(
        "http",
        "https"
    );

    private static final List<String> ALLOWED_DOMAINS = List.of(
        ".minecraft.net",
        ".mojang.com"
    );

    private static final List<String> BLOCKED_DOMAINS = List.of(
        "bugs.mojang.com",
        "education.minecraft.net",
        "feedback.minecraft.net"
    );

    public static boolean isAllowedTextureDomain(final String url) {
        final URI uri;

        try {
            uri = new URI(url).normalize();
        } catch (final URISyntaxException ignored) {
            return false;
        }

        final String scheme = uri.getScheme();
        if (scheme == null || !ALLOWED_SCHEMES.contains(scheme)) {
            return false;
        }

        final String domain = uri.getHost();
        if (domain == null) {
            return false;
        }

        final String decodedDomain = IDN.toUnicode(domain);
        final String lowerCaseDomain = decodedDomain.toLowerCase(Locale.ROOT);
        if (!lowerCaseDomain.equals(decodedDomain)) {
            return false;
        }

        return isDomainOnList(decodedDomain, ALLOWED_DOMAINS) && !isDomainOnList(decodedDomain, BLOCKED_DOMAINS);
    }

    private static boolean isDomainOnList(final String domain, final List<String> list) {
        for (final String entry : list) {
            if (domain.endsWith(entry)) {
                return true;
            }
        }

        return false;
    }
}
