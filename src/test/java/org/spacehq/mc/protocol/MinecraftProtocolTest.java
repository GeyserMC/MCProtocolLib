package org.spacehq.mc.protocol;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.data.status.PlayerInfo;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.VersionInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoBuilder;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoHandler;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.packet.Packet;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;
import static org.spacehq.mc.protocol.MinecraftConstants.*;
import static org.spacehq.mc.protocol.data.SubProtocol.STATUS;
import static org.spacehq.mc.protocol.data.game.values.entity.player.GameMode.SURVIVAL;
import static org.spacehq.mc.protocol.data.game.values.setting.Difficulty.PEACEFUL;
import static org.spacehq.mc.protocol.data.game.values.world.WorldType.DEFAULT;

public class MinecraftProtocolTest {
    private static final String HOST = "localhost";
    private static final int PORT = 25560;

    private static Server server;
    private Client client;

    @BeforeClass
    public static void setUpServer() {
        server = new Server(HOST, PORT, MinecraftProtocol.class, new TcpSessionFactory());
        server.setGlobalFlag(VERIFY_USERS_KEY, false);
        server.setGlobalFlag(SERVER_COMPRESSION_THRESHOLD, 100);
        server.setGlobalFlag(SERVER_INFO_BUILDER_KEY, new ServerInfoBuilder() {
            @Override
            public ServerStatusInfo buildInfo(Session session) {
                return new ServerStatusInfo(new VersionInfo(GAME_VERSION, PROTOCOL_VERSION),
                        new PlayerInfo(100, 0, new GameProfile[0]), new TextMessage("Hello world!"), null);
            }
        });

        server.setGlobalFlag(SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler() {
            @Override
            public void loggedIn(Session session) {
                session.send(new ServerJoinGamePacket(0, false, SURVIVAL, 0, PEACEFUL, 100, DEFAULT, false));
            }
        });

        assertTrue("Could not bind server.", server.bind().isListening());
    }

    @Test
    public void testStatus() throws InterruptedException {
        client = new Client(HOST, PORT, new MinecraftProtocol(STATUS), new TcpSessionFactory());
        Session session = client.getSession();

        ServerInfoHandlerTest handler = new ServerInfoHandlerTest();
        session.setFlag(SERVER_INFO_HANDLER_KEY, handler);
        session.addListener(new DisconnectListener());
        session.connect();

        assertTrue("Could not connect status session.", session.isConnected());
        handler.status.await(2, SECONDS);

        ServerStatusInfo info = handler.info;
        assertNotNull("Failed to get server info.", info);

        assertEquals("Received incorrect description.", "Hello world!", info.getDescription().getFullText());
        assertEquals("Received incorrect game version.", GAME_VERSION, info.getVersionInfo().getVersionName());
        assertEquals("Received incorrect protocol version.", PROTOCOL_VERSION, info.getVersionInfo().getProtocolVersion());
        assertEquals("Received incorrect online players.", 0, info.getPlayerInfo().getOnlinePlayers());
        assertEquals("Received incorrect max players.", 100, info.getPlayerInfo().getMaxPlayers());
    }

    @Test
    public void testLogin() throws InterruptedException {
        Client client = new Client(HOST, PORT, new MinecraftProtocol("test"), new TcpSessionFactory());
        Session session = client.getSession();

        LoginListenerTest listener = new LoginListenerTest();
        session.addListener(listener);
        session.addListener(new DisconnectListener());
        session.connect();

        assertTrue("Could not connect login session.", session.isConnected());
        listener.login.await(4, SECONDS);

        ServerJoinGamePacket packet = listener.packet;
        assertNotNull("Failed to log in.", packet);

        assertEquals("Received incorrect entity ID.", 0, packet.getEntityId());
        assertFalse("Received incorrect hardcore flag.", packet.getHardcore());
        assertEquals("Received incorrect gamemode.", SURVIVAL, packet.getGameMode());
        assertEquals("Received incorrect dimension.", 0, packet.getDimension());
        assertEquals("Received incorrect difficulty.", PEACEFUL, packet.getDifficulty());
        assertEquals("Received incorrect max players.", 100, packet.getMaxPlayers());
        assertEquals("Received incorrect world type.", DEFAULT, packet.getWorldType());
        assertFalse("Received incorrect reduced debug info flag.", packet.getReducedDebugInfo());
    }

    @After
    public void tearDownClient() {
        if(this.client != null) {
            this.client.getSession().disconnect("Bye!");
        }
    }

    @AfterClass
    public static void tearDownServer() {
        server.close(true);
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
