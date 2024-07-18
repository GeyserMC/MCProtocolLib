package org.geysermc.mcprotocollib.auth.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.constants.Headers;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.proxy.ProxyHandler;
import net.lenni0451.commons.httpclient.proxy.ProxyType;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.network.ProxyInfo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.UUID;

/**
 * Utilities for making HTTP requests.
 */
public class HTTPUtils {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(UUID.class, new UndashedUUIDAdapter())
            .create();

    private HTTPUtils() {
    }

    public static <T> T makeRequest(@Nullable ProxyInfo proxy, URI uri, Object input, Class<T> responseType) throws IOException {
        if (uri == null) {
            throw new IllegalArgumentException("URI cannot be null.");
        }

        HttpResponse response = createHttpClient(proxy)
            .execute(input == null ? new HttpRequest("GET", uri.toURL()) :
                new HttpContentRequest("POST", uri.toURL())
                    .setContent(HttpContent.string(GSON.toJson(input)))
                    .setHeader(Headers.CONTENT_TYPE, ContentTypes.APPLICATION_JSON.toString()));

        if (responseType == null) {
            return null;
        }

        return GSON.fromJson(new InputStreamReader(new ByteArrayInputStream(response.getContent())), responseType);
    }

    public static HttpClient createHttpClient(@Nullable ProxyInfo proxy) {
        final int timeout = 5000;

        HttpClient client = new HttpClient()
                .setConnectTimeout(timeout)
                .setReadTimeout(timeout * 2)
                .setCookieManager(null)
                .setFollowRedirects(false)
                .setHeader(Headers.ACCEPT, ContentTypes.APPLICATION_JSON.toString())
                .setHeader(Headers.ACCEPT_LANGUAGE, "en-US,en");

        if (proxy != null) {
            client.setProxyHandler(new ProxyHandler(switch (proxy.type()) {
                case HTTP -> ProxyType.HTTP;
                case SOCKS4 -> ProxyType.SOCKS4;
                case SOCKS5 -> ProxyType.SOCKS5;
            }, proxy.address(), proxy.username(), proxy.password()));
        }

        return client;
    }
}
