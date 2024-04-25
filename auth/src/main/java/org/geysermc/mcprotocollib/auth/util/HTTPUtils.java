package org.geysermc.mcprotocollib.auth.util;

import net.lenni0451.commons.httpclient.HttpClient;
import net.lenni0451.commons.httpclient.HttpResponse;
import net.lenni0451.commons.httpclient.RetryHandler;
import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.constants.Headers;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.requests.HttpContentRequest;
import net.lenni0451.commons.httpclient.requests.HttpRequest;
import org.geysermc.mcprotocollib.auth.exception.request.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.*;
import java.net.Proxy;
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

    public static <T> T makeRequest(Proxy proxy, URI uri, Object input, Class<T> responseType) throws RequestException {
        if(proxy == null) {
            throw new IllegalArgumentException("Proxy cannot be null.");
        } else if(uri == null) {
            throw new IllegalArgumentException("URI cannot be null.");
        }

        try {
            HttpResponse response = createHttpClient().execute(input == null ? new HttpRequest("GET", uri.toURL()) :
                    new HttpContentRequest("POST", uri.toURL()).setContent(HttpContent.string(GSON.toJson(input))));

            if (responseType == null) {
                return null;
            }

            return GSON.fromJson(new InputStreamReader(new ByteArrayInputStream(response.getContent())), responseType);
        } catch(IOException e) {
            throw new ServiceUnavailableException("Could not make request to '" + uri + "'.", e);
        }
    }

    public static HttpClient createHttpClient() {
        final int timeout = 5000;

        return new HttpClient()
                .setConnectTimeout(timeout)
                .setReadTimeout(timeout * 2)
                .setCookieManager(null)
                .setFollowRedirects(false)
                .setRetryHandler(new RetryHandler(0, 50))
                .setHeader(Headers.ACCEPT, ContentTypes.APPLICATION_JSON.toString())
                .setHeader(Headers.ACCEPT_LANGUAGE, "en-US,en");
    }
}
