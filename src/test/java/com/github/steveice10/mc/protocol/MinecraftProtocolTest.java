package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.protocol.codec.MinecraftCodec;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerSpawnInfo;
import com.github.steveice10.mc.protocol.data.status.PlayerInfo;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.VersionInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoBuilder;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import com.github.steveice10.packetlib.tcp.TcpServer;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static com.github.steveice10.mc.protocol.MinecraftConstants.SERVER_COMPRESSION_THRESHOLD;
import static com.github.steveice10.mc.protocol.MinecraftConstants.SERVER_INFO_BUILDER_KEY;
import static com.github.steveice10.mc.protocol.MinecraftConstants.SERVER_INFO_HANDLER_KEY;
import static com.github.steveice10.mc.protocol.MinecraftConstants.SERVER_LOGIN_HANDLER_KEY;
import static com.github.steveice10.mc.protocol.MinecraftConstants.VERIFY_USERS_KEY;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

public class MinecraftProtocolTest {
    private static final String HOST = "localhost";
    private static final int PORT = 25562;

    private static final ServerStatusInfo SERVER_INFO = new ServerStatusInfo(
            new VersionInfo(MinecraftCodec.CODEC.getMinecraftVersion(), MinecraftCodec.CODEC.getProtocolVersion()),
            new PlayerInfo(100, 0, new ArrayList<>()),
            Component.text("Hello world!"),
            null,
            false
    );
    private static final ClientboundLoginPacket JOIN_GAME_PACKET = new ClientboundLoginPacket(0, false, new String[]{"minecraft:world"}, 0, 16, 16, false, false, false, new PlayerSpawnInfo("overworld", "minecraft:world", 100, GameMode.SURVIVAL, GameMode.SURVIVAL, false, false, null, 100));

    private static Server server;

    @BeforeAll
    public static void setupServer() {
        server = new TcpServer(HOST, PORT, MinecraftProtocol::new);
        server.setGlobalFlag(VERIFY_USERS_KEY, false);
        server.setGlobalFlag(SERVER_COMPRESSION_THRESHOLD, 100);
        server.setGlobalFlag(SERVER_INFO_BUILDER_KEY, (ServerInfoBuilder) session -> SERVER_INFO);
        server.setGlobalFlag(SERVER_LOGIN_HANDLER_KEY, (ServerLoginHandler) session -> {
            // Seems like in this setup the server can reply too quickly to ServerboundFinishConfigurationPacket
            // before the client can transition CONFIGURATION -> GAME. There is probably something wrong here and this is just a band-aid.
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.err.println("Failed to wait to send ClientboundLoginPacket: " + e.getMessage());
            }
            session.send(JOIN_GAME_PACKET);
        });

        assertTrue(server.bind(true).isListening(), "Could not bind server.");
    }

    @AfterAll
    public static void tearDownServer() {
        if (server != null) {
            server.close(true);
            server = null;
        }
    }

    @Test
    public void testStatus() throws InterruptedException {
        Session session = new TcpClientSession(HOST, PORT, new MinecraftProtocol());
        try {
            ServerInfoHandlerTest handler = new ServerInfoHandlerTest();
            session.setFlag(SERVER_INFO_HANDLER_KEY, handler);
            session.addListener(new DisconnectListener());
            session.connect();

            handler.status.await(4, SECONDS);
            assertNotNull(handler.info, "Failed to get server info.");
            assertEquals(SERVER_INFO, handler.info, "Received incorrect server info.");
        } finally {
            session.disconnect("Status test complete.");
        }
    }

    @Test
    public void testLogin() throws InterruptedException {
        Session session = new TcpClientSession(HOST, PORT, new MinecraftProtocol("Username"));
        try {
            LoginListenerTest listener = new LoginListenerTest();
            session.addListener(listener);
            session.addListener(new DisconnectListener());
            session.connect();

            listener.login.await(4, SECONDS);
            assertNotNull(listener.packet, "Failed to log in.");
            assertEquals(JOIN_GAME_PACKET, listener.packet, "Received incorrect join packet.");
        } finally {
            session.disconnect("Login test complete.");
        }
    }

    private static class ServerInfoHandlerTest implements ServerInfoHandler {
        public CountDownLatch status = new CountDownLatch(1);
        public ServerStatusInfo info;

        @Override
        public void handle(Session session, ServerStatusInfo info) {
            this.info = info;
            this.status.countDown();
        }
    }

    private static class LoginListenerTest extends SessionAdapter {
        public CountDownLatch login = new CountDownLatch(1);
        public ClientboundLoginPacket packet;

        @Override
        public void packetReceived(Session session, Packet packet) {
            if (packet instanceof ClientboundLoginPacket) {
                this.packet = (ClientboundLoginPacket) packet;
                this.login.countDown();
            }
        }
    }

    private static class DisconnectListener extends SessionAdapter {
        @Override
        public void disconnected(DisconnectedEvent event) {
            System.err.println("Disconnected: " + event.getReason());
            if (event.getCause() != null) {
                event.getCause().printStackTrace();
            }
        }
    }
}
