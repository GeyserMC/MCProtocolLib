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
     * @param proxy Proxy to use when making the request.
     * @param uri   URI to make the request to.
     * @param input Input to provide in the request.
     * @throws IllegalArgumentException If the given proxy or URI is null.
     * @throws RequestException If an error occurs while making the request.
     */
    public static void makeRequest(Proxy proxy, URI uri, Object input) throws RequestException {
        makeRequest(proxy, uri, input, null);
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
            checkForError(response);

            if(responseType != null) {
                return GSON.fromJson(response, responseType);
            }
        }

        return null;
    }

    public static <T> T makeRequest(Proxy proxy, URI uri, Object input, Class<T> responseType) throws RequestException {
        return makeRequest(proxy, uri, input, responseType, new HashMap<String, String>());
    }

    /**
     * Makes an HTTP request as a from.
     *
     * @param proxy        Proxy to use when making the request.
     * @param uri          URI to make the request to.
     * @param input        Input to provide in the request.
     * @param responseType Class to provide the response as.
     * @param <T>          Type to provide the response as.
     * @return The response of the request.
     * @throws IllegalArgumentException If the given proxy or URI is null.
     * @throws RequestException If an error occurs while making the request.
     */
    public static <T> T makeRequestForm(Proxy proxy, URI uri, Map<String, String> input, Class<T> responseType) throws RequestException {
        if(proxy == null) {
            throw new IllegalArgumentException("Proxy cannot be null.");
        } else if(uri == null) {
            throw new IllegalArgumentException("URI cannot be null.");
        }

        String inputString = formMapToString(input);

        JsonElement response;
        try {
            response = performPostRequest(proxy, uri, new HashMap<String, String>(), inputString, "application/x-www-form-urlencoded");
        } catch(IOException e) {
            throw new ServiceUnavailableException("Could not make request to '" + uri + "'.", e);
        }

        if(response != null) {
            checkForError(response);

            if(responseType != null) {
                return GSON.fromJson(response, responseType);
            }
        }

        return null;
    }

    public static String formMapToString(Map<String, String> input) {
        StringBuilder inputString = new StringBuilder();
        for (Map.Entry<String, String> inputField : input.entrySet()) {
            if (inputString.length() > 0) {
                inputString.append("&");
            }

            try {
                inputString.append(URLEncoder.encode(inputField.getKey(), StandardCharsets.UTF_8.toString()));
                inputString.append("=");
                inputString.append(URLEncoder.encode(inputField.getValue(), StandardCharsets.UTF_8.toString()));
            } catch (UnsupportedEncodingException ignored) { }
        }

        return inputString.toString();
    }

    private static void checkForError(JsonElement response) throws RequestException {
        if(response.isJsonObject()) {
            JsonObject object = response.getAsJsonObject();
            if(object.has("error")) {
                String error = object.get("error").getAsString();
                String cause = object.has("cause") ? object.get("cause").getAsString() : "";
                String errorMessage = object.has("errorMessage") ? object.get("errorMessage").getAsString() : "";
                errorMessage = object.has("error_description") ? object.get("error_description").getAsString() : errorMessage;
                if(!error.equals("")) {
                    if(error.equals("ForbiddenOperationException")) {
                        if (cause != null && cause.equals("UserMigratedException")) {
                            throw new UserMigratedException(errorMessage);
                        } else {
                            throw new InvalidCredentialsException(errorMessage);
                        }
                    } else if (error.equals("authorization_pending")) {
                        throw new AuthPendingException(errorMessage);
                    } else {
                        throw new RequestException(errorMessage);
                    }
                }
            }
        }
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
