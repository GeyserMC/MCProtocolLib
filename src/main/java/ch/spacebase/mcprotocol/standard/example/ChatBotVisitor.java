package ch.spacebase.mcprotocol.standard.example;

import ch.spacebase.mcprotocol.exception.LoginException;
import ch.spacebase.mcprotocol.exception.OutdatedLibraryException;
import java.text.DecimalFormat;

import ch.spacebase.mcprotocol.event.DisconnectEvent;
import ch.spacebase.mcprotocol.event.PacketRecieveEvent;
import ch.spacebase.mcprotocol.event.PacketSendEvent;
import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.event.PacketVisitorAdapter;
import ch.spacebase.mcprotocol.event.ProtocolListener;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.StandardClient;
import ch.spacebase.mcprotocol.standard.packet.PacketChat;
import ch.spacebase.mcprotocol.standard.packet.PacketPlayerPositionLook;
import ch.spacebase.mcprotocol.util.Util;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple bot that prints
 * "Hello, this is Heisenberg at coordinate <coordinate>". Be sure to use the
 * Bukkit server setting online-mode=false in server.properties. Otherwise
 * supply a valid minecraft.net username and password.
 */
public class ChatBotVisitor {

    private static final String USERNAME = "";
    private static final String PASSWORD = "";
    private static final String HOST = "127.0.0.1";
    private static final int    PORT = 25565;

    private Client client;
    private Listener listener;
    private PacketVisitor visitor;
    

    public ChatBotVisitor(String host, int port) {
        this.client = new StandardClient(host, port);
        this.listener = new Listener();

        this.client.listen(this.listener);
        
        this.visitor = new Visitor();
    }

    public void login(String username, String password) {
        try {
            this.client.login(username, password);
        } catch (LoginException ex) {
            Logger.getLogger("Login Error: " + ex.getLocalizedMessage());
        } catch (OutdatedLibraryException ex) {
            Logger.getLogger(ChatBotVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.client.connect();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }

    public void say(String text) {
        PacketChat chat = new PacketChat();
        chat.message = text;
        this.client.send(chat);
    }

    public static void main(String[] args) {
        ChatBotVisitor bot = new ChatBotVisitor(HOST, PORT);
        Util.logger().info("Logging in...");
        bot.login(USERNAME, PASSWORD);
    }

    private class Visitor extends PacketVisitorAdapter {

        @Override
        public void visit(PacketPlayerPositionLook packet) {
            client.send(packet);
            DecimalFormat format = new DecimalFormat("#.00");

            ChatBotVisitor.this.say("Hello, this is "+USERNAME+" at coordinate (" + format.format(packet.x) + ", " + format.format(packet.y) + ", " + format.format(packet.z) + ")");
        }
    }

    private class Listener extends ProtocolListener {

        @Override
        public void onPacketReceive(PacketRecieveEvent event) {
            Packet packet = event.getPacket();
            packet.accept(visitor);                        
        }

        @Override
        public void onPacketSend(PacketSendEvent event) {
        }

        @Override
        public void onDisconnect(DisconnectEvent event) {
            Util.logger().info("Disconnected: " + event.getReason());
        }
    }
}
