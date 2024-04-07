package org.geysermc.mcprotocollib.auth.util;

import org.geysermc.mcprotocollib.auth.exception.request.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Utilities for making HTTP requests.
 */
public class HTTP {
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDSerializer()).create();
    }

    private HTTP() {
    }

    /**
     * Makes an HTTP request.
     *
     * @param proxy        Proxy to use when making the request.
     * @param uri          URI to make the request to.
     * @param input        Input to provide in the request.
     * @param responseType Class to provide the response as.
     * @param <T>          Type to provide the response as.
     * @param extraHeaders Extra headers to add to the request.
     * @return The response of the request.
     * @throws IllegalArgumentException If the given proxy or URI is null.
     * @throws RequestException If an error occurs while making the request.
     */
    public static <T> T makeRequest(Proxy proxy, URI uri, Object input, Class<T> responseType, Map<String, String> extraHeaders) throws RequestException {
        if(proxy == null) {
            throw new IllegalArgumentException("Proxy cannot be null.");
        } else if(uri == null) {
            throw new IllegalArgumentException("URI cannot be null.");
        }

        JsonElement response;
        try {
            response = input == null ? performGetRequest(proxy, uri, extraHeaders) : performPostRequest(proxy, uri, extraHeaders, GSON.toJson(input), "application/json");
        } catch(IOException e) {
            throw new ServiceUnavailableException("Could not make request to '" + uri + "'.", e);
        }

        if(response != null) {
            if(responseType != null) {
                return GSON.fromJson(response, responseType);
            }
        }

        return null;
    }

    public static <T> T makeRequest(Proxy proxy, URI uri, Object input, Class<T> responseType) throws RequestException {
        return makeRequest(proxy, uri, input, responseType, new HashMap<>());
    }

    private static JsonElement performGetRequest(Proxy proxy, URI uri, Map<String, String> extraHeaders) throws IOException {
        HttpURLConnection connection = createUrlConnection(proxy, uri);
        for (Map.Entry<String, String> header : extraHeaders.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }
        connection.setDoInput(true);

        return processResponse(connection);
    }

    private static JsonElement performPostRequest(Proxy proxy, URI uri, Map<String, String> extraHeaders, String post, String type) throws IOException {
        byte[] bytes = post.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection connection = createUrlConnection(proxy, uri);
        connection.setRequestProperty("Content-Type", type + "; charset=utf-8");
        connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
        for (Map.Entry<String, String> header : extraHeaders.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }
        connection.setDoInput(true);
        connection.setDoOutput(true);

        try(OutputStream out = connection.getOutputStream()) {
            out.write(bytes);
        }

        return processResponse(connection);
    }

    public static HttpURLConnection createUrlConnection(Proxy proxy, URI uri) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection(proxy);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setUseCaches(false);
        return connection;
    }

    private static JsonElement processResponse(HttpURLConnection connection) throws IOException {
        try(InputStream in = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream()) {
            return in != null ? GSON.fromJson(new InputStreamReader(in), JsonElement.class) : null;
        }
    }
}
