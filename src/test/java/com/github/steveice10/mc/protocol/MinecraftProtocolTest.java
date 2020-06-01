package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.mc.protocol.data.status.PlayerInfo;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.VersionInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoBuilder;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static com.github.steveice10.mc.protocol.MinecraftConstants.SERVER_COMPRESSION_THRESHOLD;
import static com.github.steveice10.mc.protocol.MinecraftConstants.SERVER_INFO_BUILDER_KEY;
import static com.github.steveice10.mc.protocol.MinecraftConstants.SERVER_INFO_HANDLER_KEY;
import static com.github.steveice10.mc.protocol.MinecraftConstants.SERVER_LOGIN_HANDLER_KEY;
import static com.github.steveice10.mc.protocol.MinecraftConstants.VERIFY_USERS_KEY;
import static com.github.steveice10.mc.protocol.data.SubProtocol.STATUS;
import static com.github.steveice10.mc.protocol.data.game.world.WorldType.DEFAULT;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MinecraftProtocolTest {
    private static final String HOST = "localhost";
    private static final int PORT = 25560;

    private static final ServerStatusInfo SERVER_INFO = new ServerStatusInfo(
            VersionInfo.CURRENT,
            new PlayerInfo(100, 0, new GameProfile[0]),
            new TextMessage.Builder().text("Hello world!").build(),
            null
    );
    private static final ServerJoinGamePacket JOIN_GAME_PACKET = new ServerJoinGamePacket(0, false, GameMode.SURVIVAL, 0, 100, 0, DEFAULT, 16, false, false);

    private static Server server;

    @BeforeClass
    public static void setupServer() {
        server = new Server(HOST, PORT, MinecraftProtocol.class, new TcpSessionFactory());
        server.setGlobalFlag(VERIFY_USERS_KEY, false);
        server.setGlobalFlag(SERVER_COMPRESSION_THRESHOLD, 100);
        server.setGlobalFlag(SERVER_INFO_BUILDER_KEY, (ServerInfoBuilder) session -> SERVER_INFO);
        server.setGlobalFlag(SERVER_LOGIN_HANDLER_KEY, (ServerLoginHandler) session -> session.send(JOIN_GAME_PACKET));

        assertTrue("Could not bind server.", server.bind(true).isListening());
    }

    @AfterClass
    public static void tearDownServer() {
        if(server != null) {
            server.close(true);
            server = null;
        }
    }

    @Test
    public void testStatus() throws InterruptedException {
        Client client = new Client(HOST, PORT, new MinecraftProtocol(STATUS), new TcpSessionFactory());
        try {
            Session session = client.getSession();

            ServerInfoHandlerTest handler = new ServerInfoHandlerTest();
            session.setFlag(SERVER_INFO_HANDLER_KEY, handler);
            session.addListener(new DisconnectListener());
            session.connect();

            handler.status.await(4, SECONDS);
            assertNotNull("Failed to get server info.", handler.info);
            assertEquals("Received incorrect server info.", SERVER_INFO, handler.info);
        } finally {
            client.getSession().disconnect("Status test complete.");
        }
    }

    @Test
    public void testLogin() throws InterruptedException {
        Client client = new Client(HOST, PORT, new MinecraftProtocol("Username"), new TcpSessionFactory());
        try {
            Session session = client.getSession();

            LoginListenerTest listener = new LoginListenerTest();
            session.addListener(listener);
            session.addListener(new DisconnectListener());
            session.connect();

            listener.login.await(4, SECONDS);
            assertNotNull("Failed to log in.", listener.packet);
            assertEquals("Received incorrect join packet.", JOIN_GAME_PACKET, listener.packet);
        } finally {
            client.getSession().disconnect("Login test complete.");
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
        public ServerJoinGamePacket packet;

        @Override
        public void packetReceived(PacketReceivedEvent event) {
            Packet packet = event.getPacket();
            if(packet instanceof ServerJoinGamePacket) {
                this.packet = (ServerJoinGamePacket) packet;
                this.login.countDown();
            }
        }
    }

    private static class DisconnectListener extends SessionAdapter {
        @Override
        public void disconnected(DisconnectedEvent event) {
            System.err.println("Disconnected: " + event.getReason());
            if(event.getCause() != null) {
                event.getCause().printStackTrace();
            }
        }
    }
}
