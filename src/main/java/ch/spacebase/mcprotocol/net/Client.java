package ch.spacebase.mcprotocol.net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.crypto.SecretKey;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import ch.spacebase.mcprotocol.event.PacketRecieveEvent;
import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.exception.LoginException;
import ch.spacebase.mcprotocol.exception.OutdatedLibraryException;
import ch.spacebase.mcprotocol.net.packet.Packet;
import ch.spacebase.mcprotocol.net.packet.PacketDisconnect;
import ch.spacebase.mcprotocol.net.packet.PacketHandshake;
import ch.spacebase.mcprotocol.util.Constants;
import ch.spacebase.mcprotocol.util.Util;

public class Client extends Connection {
	
	private String username;
	private String sessionId;
	private String serverId;
	
	private DataInputStream input;
	private DataOutputStream output;
	private SecretKey key;
	
	private Queue<Packet> packets = new ConcurrentLinkedQueue<Packet>();
	private boolean connected;
	private boolean session;
	
	public Client(String host, int port) {
		super(host, port);
	}
	
	/**
	 * Assigns username without logging into minecraft.net. Use this login method
	 * together with the Bukkit server online-mode=false in server.properties.
	 * @param username The username to assign.
	 */
	public void setUser(String username) {
		this.username = username;
	}
	
	public boolean setUser(String username, String password) throws LoginException, OutdatedLibraryException {
		URL url = null;
		
		try {
			url = new URL("https://login.minecraft.net/");
		} catch(MalformedURLException e) {
			throw new LoginException("Login URL is malformed?", e);
		}
		
		String params = "";
		
		try {
			params = "user=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + Constants.LAUNCHER_VERSION;
		} catch(UnsupportedEncodingException e) {
			throw new LoginException("UTF-8 unsupported", e);
		}
		
		HttpURLConnection conn = null;
        
		try {
			Util.logger().info("Sending info to login.minecraft.net...");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(params.getBytes().length));
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setReadTimeout(1000 * 60 * 10);
			
            conn.connect();
            
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(params);
            out.flush();
            out.close();
            
            if (conn.getResponseCode() != 200) {
                throw new LoginException("Did not get expected 200 code");
            }
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder build = new StringBuilder();
            
            char[] buffer = new char[1024];
            int length = 0;
            while((length = reader.read(buffer)) != -1) {
                build.append(buffer, 0, length);
            }
            
            String result = build.toString();
            if (result.contains(":")) {
                String[] values = result.split(":");
                
                try {
                    this.username = values[2].trim();
                    this.sessionId = values[3].trim();
                    this.session = true;
                    
                    new Thread(new KeepAliveTask()).start();
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new LoginException("Response contained incorrect amount of parameters.");
                }
                
                Util.logger().info("Finished logging in to minecraft.net");
                return true;
            } else {
                if (result.trim().equals("Bad login")) {
                    return false;
                } else if (result.trim().equals("Old version")) {
                    throw new OutdatedLibraryException();
                } else {
                    throw new LoginException(result.trim());
                }
            }
		} catch (IOException e) {
			throw new LoginException("Failed to login", e);
		} finally {
			if (conn != null) conn.disconnect();
			conn = null;
		}
	}
	
	public Client connect() throws ConnectException {
		Util.logger().info("Connecting to \"" + this.host + ":" + this.port + "\"...");
		
		try {
			Socket sock = new Socket(InetAddress.getByName(this.host), this.port);
			sock.setSoTimeout(30000);
			sock.setTrafficClass(24);
			this.input = new DataInputStream(sock.getInputStream());
			this.output = new DataOutputStream(sock.getOutputStream());
			this.connected = true;
			new ListenThread().start();
			new WriteThread().start();
		} catch(UnknownHostException e) {
			throw new ConnectException("Unknown host: " + this.host);
		} catch(IOException e) {
			throw new ConnectException("Failed to open stream: " + this.host, e);
		}

		this.send(new PacketHandshake(this.username, this.host, this.port));
		return this;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getSessionId() {
		return this.sessionId;
	}
	
	public String getServerId() {
		return this.serverId;
	}
	
	public void setServerId(String id) {
		this.serverId = id;
	}

	public void send(Packet packet) {
		this.packets.add(packet);
	}
	
	public void disconnect(String reason) {
		this.disconnect(reason, true);
	}
	
	public void disconnect(String reason, boolean packet) {
		if(packet) this.send(new PacketDisconnect(reason));
		this.packets.clear();
		this.session = false;
		this.connected = false;
	}
	
	public void setAES() {
		BufferedBlockCipher in = new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 8));
		in.init(false, new ParametersWithIV(new KeyParameter(this.key.getEncoded()), this.key.getEncoded(), 0, 16));
		BufferedBlockCipher out = new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 8));
		out.init(true, new ParametersWithIV(new KeyParameter(this.key.getEncoded()), this.key.getEncoded(), 0, 16));
		
		this.input = new DataInputStream(new CipherInputStream(this.input, in));
		this.output = new DataOutputStream(new CipherOutputStream(this.output, out));
	}
	
	public SecretKey getSecretKey() {
		return this.key;
	}
	
	public void setSecretKey(SecretKey key) {
		this.key = key;
	}
	
	public boolean isConnected() {
		return this.connected;
	}
	
	private class ListenThread extends Thread {
		@Override
		public void run() {
			while(connected) {
				try {
					int opcode = input.readUnsignedByte();
					if(opcode < 0) {
						continue;
					}
					
					if(Packet.get(opcode) == null) {
						Util.logger().severe("Bad packet ID: " + opcode);
						disconnect("Bad packet ID: " + opcode);
						return;
					}
					
					Packet packet = Packet.get(opcode).newInstance();
					packet.read(input);
					packet.handleClient(Client.this);
					call(new PacketRecieveEvent(packet));
				} catch(Exception e) {
					Util.logger().severe("Error while listening to connection!");
					e.printStackTrace();
					disconnect("Error while listening to connection!");
				}
			}
		}
	}
	
	private class WriteThread extends Thread {
		@Override
		public void run() {
			while(connected) {
				if(packets.size() > 0) {
					Packet packet = packets.poll();
					
					try {
						output.write(packet.getId());
						packet.write(output);
						output.flush();
					} catch(Exception e) {
						Util.logger().severe("Error while writing packet \"" + packet.getId() + "\"!");
						e.printStackTrace();
						disconnect("Error while writing packet.");
					}
				}
			}
		}
	}
	
	private class KeepAliveTask implements Runnable {
		private URL url;
		private long last;
		
		public KeepAliveTask() throws LoginException {
			try {
				this.url = new URL("https://login.minecraft.net/");
			} catch(MalformedURLException e) {
				throw new LoginException("Failed to create keep alive URL!", e);
			}
		}
		
		public void run() {
			this.last = System.currentTimeMillis();
			while(session) {
				if(System.currentTimeMillis() - this.last >= 300000) {
					HttpURLConnection conn = null;
					
					try {
						conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("POST");
			            conn.setUseCaches(false);
			            conn.setDoInput(true);
			            conn.setDoOutput(true);
			            conn.setReadTimeout(1000 * 60 * 10);
			            conn.connect();
					} catch (IOException e) {
						Util.logger().severe("Failed to send keep alive to login.minecraft.net!");
						e.printStackTrace();
					} finally {
						if (conn != null) conn.disconnect();
						conn = null;
					}
				}
			}
		}
	}
	
}
