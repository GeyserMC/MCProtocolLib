package ch.spacebase.mcprotocol.standard;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.crypto.SecretKey;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import ch.spacebase.mcprotocol.exception.LoginException;
import ch.spacebase.mcprotocol.exception.OutdatedLibraryException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.Connection;
import ch.spacebase.mcprotocol.net.Protocol;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.standard.packet.PacketDisconnect;
import ch.spacebase.mcprotocol.standard.packet.PacketHandshake;
import ch.spacebase.mcprotocol.standard.packet.PacketKeepAlive;
import ch.spacebase.mcprotocol.util.Constants;
import ch.spacebase.mcprotocol.util.Util;

/**
 * The standard Minecraft protocol backend.
 */
public class StandardProtocol extends Protocol {

	/**
	 * Whether the protocol's session is active.
	 */
	private boolean session;
	
	/**
	 * The protocol's secret key.
	 */
	private SecretKey key;
	
	/**
	 * The protocol's last-sent server keep alive id.
	 */
	private int aliveId;
	
	/**
	 * The protocol's login key.
	 */
	private String loginKey;
	
	/**
	 * The protocol's security token.
	 */
	private byte token[];

	/**
	 * Creates a new standard protocol instance.
	 */
	public StandardProtocol() {
		super(Type.STANDARD);
	}

	@Override
	public void connect(Client c) {
		c.send(new PacketHandshake(c.getUsername(), c.getRemoteHost(), c.getRemotePort()));
	}

	@Override
	public boolean login(Client c, String username, String password) throws LoginException, OutdatedLibraryException {
		URL url = null;

		try {
			url = new URL("https://login.minecraft.net/");
		} catch (MalformedURLException e) {
			throw new LoginException("Login URL is malformed?", e);
		}

		String params = "";

		try {
			params = "user=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + Constants.LAUNCHER_VERSION;
		} catch (UnsupportedEncodingException e) {
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

			if(conn.getResponseCode() != 200) {
				throw new LoginException("Login returned response " + conn.getResponseCode() + ": " + conn.getResponseMessage());
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuilder build = new StringBuilder();

			char[] buffer = new char[1024];
			int length = 0;
			while((length = reader.read(buffer)) != -1) {
				build.append(buffer, 0, length);
			}

			String result = build.toString();
			if(result.contains(":")) {
				String[] values = result.split(":");

				try {
					c.setUser(values[2].trim());
					c.setSessionId(values[3].trim());
					this.session = true;

					new Thread(new KeepAliveTask()).start();
				} catch (ArrayIndexOutOfBoundsException e) {
					throw new LoginException("Response contained incorrect amount of parameters: " + result);
				}

				Util.logger().info("Finished logging in to minecraft.net");
				return true;
			} else {
				if(result.trim().equals("Bad login")) {
					return false;
				} else if(result.trim().equals("Old version")) {
					throw new OutdatedLibraryException();
				} else {
					throw new LoginException(result.trim());
				}
			}
		} catch (IOException e) {
			throw new LoginException("Failed to login", e);
		} finally {
			if(conn != null) conn.disconnect();
			conn = null;
		}
	}

	/**
	 * A task that keeps the client's minecraft.net session alive.
	 */
	private class KeepAliveTask implements Runnable {
		/**
		 * The minecraft.net session URL.
		 */
		private URL url;
		
		/**
		 * The time when a keep alive was last sent.
		 */
		private long last;

		/**
		 * Creates a new keep alive task runnable.
		 * @throws LoginException If a login error occurs.
		 */
		public KeepAliveTask() throws LoginException {
			try {
				this.url = new URL("https://login.minecraft.net/");
			} catch (MalformedURLException e) {
				throw new LoginException("Failed to create keep alive URL!", e);
			}
		}

		@Override
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
						if(conn != null) conn.disconnect();
						conn = null;
					}
				}
				
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	@Override
	public void disconnected(Connection conn, String reason, boolean packet) {
		if(packet) conn.send(new PacketDisconnect(reason));
		this.session = false;
	}

	@Override
	public void keepAlive(ServerConnection c) {
		this.aliveId = Util.random().nextInt();
		c.send(new PacketKeepAlive(this.aliveId));
	}

	/**
	 * Gets the protocol's login key.
	 * @return The protocol's login key.
	 */
	public String getLoginKey() {
		return this.loginKey;
	}

	/**
	 * Sets the protocol's login key.
	 * @param key The new login key.
	 */
	public void setLoginKey(String key) {
		this.loginKey = key;
	}
	
	/**
	 * Gets the protocol's secret key.
	 * @return The protocol's secret key.
	 */
	public SecretKey getSecretKey() {
		return this.key;
	}

	/**
	 * Sets the protocol's secret key.
	 * @param key The new secret key.
	 */
	public void setSecretKey(SecretKey key) {
		this.key = key;
	}

	/**
	 * Gets the protocol's security token.
	 * @return The protocol's security token.
	 */
	public byte[] getToken() {
		return this.token;
	}

	/**
	 * Sets the protocol's security token.
	 * @param token The new security token.
	 */
	public void setToken(byte token[]) {
		this.token = token;
	}

	/**
	 * Enabled AES encryption on the connection.
	 * @param conn Connection to enable AES on.
	 */
	public void setAES(Connection conn) {
		BufferedBlockCipher in = new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 8));
		in.init(false, new ParametersWithIV(new KeyParameter(this.key.getEncoded()), this.key.getEncoded(), 0, 16));
		BufferedBlockCipher out = new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 8));
		out.init(true, new ParametersWithIV(new KeyParameter(this.key.getEncoded()), this.key.getEncoded(), 0, 16));

		conn.setIn(new DataInputStream(new CipherInputStream(conn.getIn(), in)));
		conn.setOut(new DataOutputStream(new CipherOutputStream(conn.getOut(), out)));
	}

}
