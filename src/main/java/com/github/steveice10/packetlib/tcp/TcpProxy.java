package com.github.steveice10.packetlib.tcp;

import com.github.steveice10.packetlib.ProxyInfo;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.proxy.HttpProxyHandler;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;

public class TcpProxy {

    private ProxyInfo proxy;

    public TcpProxy(ProxyInfo proxy) {
        this.proxy = proxy;
    }

    public void addProxy(ChannelPipeline pipeline) {
        if(proxy != null) {
            switch(proxy.getType()) {
                case HTTP:
                    if (proxy.isAuthenticated()) {
                        pipeline.addFirst("proxy", new HttpProxyHandler(proxy.getAddress(), proxy.getUsername(), proxy.getPassword()));
                    } else {
                        pipeline.addFirst("proxy", new HttpProxyHandler(proxy.getAddress()));
                    }

                    break;
                case SOCKS4:
                    if (proxy.isAuthenticated()) {
                        pipeline.addFirst("proxy", new Socks4ProxyHandler(proxy.getAddress(), proxy.getUsername()));
                    } else {
                        pipeline.addFirst("proxy", new Socks4ProxyHandler(proxy.getAddress()));
                    }

                    break;
                case SOCKS5:
                    if (proxy.isAuthenticated()) {
                        pipeline.addFirst("proxy", new Socks5ProxyHandler(proxy.getAddress(), proxy.getUsername(), proxy.getPassword()));
                    } else {
                        pipeline.addFirst("proxy", new Socks5ProxyHandler(proxy.getAddress()));
                    }

                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported proxy type: " + proxy.getType());
            }
        }
    }


}
