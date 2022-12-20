package com.github.steveice10.mc.protocol.test;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.auth.service.MojangAuthenticationService;
import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.ServerLoginHandler;
import com.github.steveice10.mc.protocol.codec.MinecraftCodec;
import com.github.steveice10.mc.protocol.data.ProtocolState;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.status.PlayerInfo;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.VersionInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoBuilder;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.mc.protocol.data.status.handler.ServerPingTimeHandler;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.ProxyInfo;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.server.ServerAdapter;
import com.github.steveice10.packetlib.event.server.ServerClosedEvent;
import com.github.steveice10.packetlib.event.server.SessionAddedEvent;
import com.github.steveice10.packetlib.event.server.SessionRemovedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import com.github.steveice10.packetlib.tcp.TcpServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

public class MinecraftProtocolTest {
    private static final boolean SPAWN_SERVER = true;
    private static final boolean VERIFY_USERS = false;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 25565;
    private static final ProxyInfo PROXY = null;
    private static final Proxy AUTH_PROXY = Proxy.NO_PROXY;
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";

    public static void main(String[] args) {
        if (SPAWN_SERVER) {
            SessionService sessionService = new SessionService();
            sessionService.setProxy(AUTH_PROXY);

            Server server = new TcpServer(HOST, PORT, MinecraftProtocol::new);
            server.setGlobalFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
            server.setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY, VERIFY_USERS);
            server.setGlobalFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY, (ServerInfoBuilder) session ->
                    new ServerStatusInfo(
                            new VersionInfo(MinecraftCodec.CODEC.getMinecraftVersion(), MinecraftCodec.CODEC.getProtocolVersion()),
                            new PlayerInfo(100, 0, new GameProfile[0]),
                            Component.text("Hello world!"),
                            null,
                            false
                    )
            );

            server.setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, (ServerLoginHandler) session ->
                    session.send(new ClientboundLoginPacket(
                            0,
                            false,
                            GameMode.SURVIVAL,
                            GameMode.SURVIVAL,
                            1,
                            new String[]{"minecraft:world"},
                            loadNetworkCodec(),
                            "minecraft:overworld",
                            "minecraft:world",
                            100,
                            0,
                            16,
                            16,
                            false,
                            false,
                            false,
                            false,
                            null
                    ))
            );

            server.setGlobalFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD, 100);
            server.addListener(new ServerAdapter() {
                @Override
                public void serverClosed(ServerClosedEvent event) {
                    System.out.println("Server closed.");
                }

                @Override
                public void sessionAdded(SessionAddedEvent event) {
                    event.getSession().addListener(new SessionAdapter() {
                        @Override
                        public void packetReceived(Session session, Packet packet) {
                            if (packet instanceof ServerboundChatPacket) {
                                GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
                                System.out.println(profile.getName() + ": " + ((ServerboundChatPacket) packet).getMessage());

                                Component msg = Component.text("Hello, ")
                                        .color(NamedTextColor.GREEN)
                                        .append(Component.text(profile.getName())
                                                .color(NamedTextColor.AQUA)
                                                .decorate(TextDecoration.UNDERLINED))
                                        .append(Component.text("!")
                                                .color(NamedTextColor.GREEN));

                                session.send(new ClientboundSystemChatPacket(msg, false));
                            }
                        }
                    });
                }

                @Override
                public void sessionRemoved(SessionRemovedEvent event) {
                    MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
                    if (protocol.getState() == ProtocolState.GAME) {
                        System.out.println("Closing server.");
                        event.getServer().close(false);
                    }
                }
            });

            server.bind();
        }

        status();
        login();
    }

    private static void status() {
        SessionService sessionService = new SessionService();
        sessionService.setProxy(AUTH_PROXY);

        MinecraftProtocol protocol = new MinecraftProtocol();
        Session client = new TcpClientSession(HOST, PORT, protocol, PROXY);
        client.setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
        client.setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, (ServerInfoHandler) (session, info) -> {
            System.out.println("Version: " + info.getVersionInfo().getVersionName()
                    + ", " + info.getVersionInfo().getProtocolVersion());
            System.out.println("Player Count: " + info.getPlayerInfo().getOnlinePlayers()
                    + " / " + info.getPlayerInfo().getMaxPlayers());
            System.out.println("Players: " + Arrays.toString(info.getPlayerInfo().getPlayers()));
            System.out.println("Description: " + info.getDescription());
            System.out.println("Icon: " + info.getIconPng());
        });

        client.setFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY, (ServerPingTimeHandler) (session, pingTime) ->
                System.out.println("Server ping took " + pingTime + "ms"));

        client.connect();
        while (client.isConnected()) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void login() {
        MinecraftProtocol protocol;
        if (VERIFY_USERS) {
            try {
                AuthenticationService authService = new MojangAuthenticationService();
                authService.setUsername(USERNAME);
                authService.setPassword(PASSWORD);
                authService.setProxy(AUTH_PROXY);
                authService.login();

                protocol = new MinecraftProtocol(authService.getSelectedProfile(), authService.getAccessToken());
                System.out.println("Successfully authenticated user.");
            } catch (RequestException e) {
                e.printStackTrace();
                return;
            }
        } else {
            protocol = new MinecraftProtocol(USERNAME);
        }

        SessionService sessionService = new SessionService();
        sessionService.setProxy(AUTH_PROXY);

        Session client = new TcpClientSession(HOST, PORT, protocol, PROXY);
        client.setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
        client.addListener(new SessionAdapter() {
            @Override
            public void packetReceived(Session session, Packet packet) {
                if (packet instanceof ClientboundLoginPacket) {
                    session.send(new ServerboundChatPacket("Hello, this is a test of MCProtocolLib.", Instant.now().toEpochMilli(), 0, new byte[0], false, new ArrayList<>(), null));
                } else if (packet instanceof ClientboundSystemChatPacket) {
                    Component message = ((ClientboundSystemChatPacket) packet).getContent();
                    System.out.println("Received Message: " + message);
                    session.disconnect("Finished");
                }
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                System.out.println("Disconnected: " + event.getReason());
                if (event.getCause() != null) {
                    event.getCause().printStackTrace();
                }
            }
        });

        client.connect();
    }

    private static CompoundTag loadNetworkCodec() {
        try (InputStream inputStream = MinecraftProtocolTest.class.getClassLoader().getResourceAsStream("network_codec.nbt");
             DataInputStream stream = new DataInputStream(new GZIPInputStream(inputStream))) {
            return (CompoundTag) NBTIO.readTag((DataInput) stream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Unable to load network codec.");
        }
    }
}
