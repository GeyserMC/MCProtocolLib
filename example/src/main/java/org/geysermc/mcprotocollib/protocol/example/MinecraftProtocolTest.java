package org.geysermc.mcprotocollib.protocol.example;

import org.geysermc.mcprotocollib.auth.data.GameProfile;
import org.geysermc.mcprotocollib.auth.exception.request.RequestException;
import org.geysermc.mcprotocollib.auth.service.AuthenticationService;
import org.geysermc.mcprotocollib.auth.service.MojangAuthenticationService;
import org.geysermc.mcprotocollib.auth.service.SessionService;
import org.geysermc.mcprotocollib.protocol.MinecraftConstants;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.protocol.ServerLoginHandler;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodec;
import org.geysermc.mcprotocollib.protocol.data.ProtocolState;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerSpawnInfo;
import org.geysermc.mcprotocollib.protocol.data.status.PlayerInfo;
import org.geysermc.mcprotocollib.protocol.data.status.ServerStatusInfo;
import org.geysermc.mcprotocollib.protocol.data.status.VersionInfo;
import org.geysermc.mcprotocollib.protocol.data.status.handler.ServerInfoBuilder;
import org.geysermc.mcprotocollib.protocol.data.status.handler.ServerInfoHandler;
import org.geysermc.mcprotocollib.protocol.data.status.handler.ServerPingTimeHandler;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import org.geysermc.mcprotocollib.network.ProxyInfo;
import org.geysermc.mcprotocollib.network.Server;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.server.ServerAdapter;
import org.geysermc.mcprotocollib.network.event.server.ServerClosedEvent;
import org.geysermc.mcprotocollib.network.event.server.SessionAddedEvent;
import org.geysermc.mcprotocollib.network.event.server.SessionRemovedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.tcp.TcpClientSession;
import org.geysermc.mcprotocollib.network.tcp.TcpServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.BitSet;

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
            server.setGlobalFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY, session ->
                    new ServerStatusInfo(
                            new VersionInfo(MinecraftCodec.CODEC.getMinecraftVersion(), MinecraftCodec.CODEC.getProtocolVersion()),
                            new PlayerInfo(100, 0, new ArrayList<>()),
                            Component.text("Hello world!"),
                            null,
                            false
                    )
            );

            server.setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, session ->
                    session.send(new ClientboundLoginPacket(
                            0,
                            false,
                            new String[]{"minecraft:world"},
                            0,
                            16,
                            16,
                            false,
                            false,
                            false,
                            new PlayerSpawnInfo(
                                    "minecraft:overworld",
                                    "minecraft:world",
                                    100,
                                    GameMode.SURVIVAL,
                                    GameMode.SURVIVAL,
                                    false,
                                    false,
                                    null,
                                    100
                            )
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
        client.setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, (session, info) -> {
            System.out.println("Version: " + info.getVersionInfo().getVersionName()
                    + ", " + info.getVersionInfo().getProtocolVersion());
            System.out.println("Player Count: " + info.getPlayerInfo().getOnlinePlayers()
                    + " / " + info.getPlayerInfo().getMaxPlayers());
            System.out.println("Players: " + Arrays.toString(info.getPlayerInfo().getPlayers().toArray()));
            System.out.println("Description: " + info.getDescription());
            System.out.println("Icon: " + new String(Base64.getEncoder().encode(info.getIconPng()), StandardCharsets.UTF_8));
        });

        client.setFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY, (session, pingTime) ->
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
                    session.send(new ServerboundChatPacket("Hello, this is a test of MCProtocolLib.", Instant.now().toEpochMilli(), 0L, null, 0, new BitSet()));
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
}
