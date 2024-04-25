package org.geysermc.mcprotocollib.auth.service;

import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * Base class for auth-related services.
 */
public abstract class Service {
    private Proxy proxy = Proxy.NO_PROXY;

    /**
     * Gets the proxy used by this service.
     *
     * @return THe proxy used by this service.
     */
    public Proxy getProxy() {
        return this.proxy;
    }

    /**
     * Sets the proxy used by this service.
     *
     * @param proxy Proxy to use. Null will be converted to NO_PROXY.
     */
    public void setProxy(Proxy proxy) {
        this.proxy = Objects.requireNonNullElse(proxy, Proxy.NO_PROXY);
    }
}
