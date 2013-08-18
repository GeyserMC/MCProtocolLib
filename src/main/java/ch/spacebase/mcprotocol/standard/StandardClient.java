package ch.spacebase.mcprotocol.standard;

import java.io.BufferedReader;
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

import ch.spacebase.mcprotocol.exception.ConnectException;
import ch.spacebase.mcprotocol.exception.LoginException;
import ch.spacebase.mcprotocol.exception.OutdatedLibraryException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.standard.packet.PacketHandshake;
import ch.spacebase.mcprotocol.util.Constants;
import ch.spacebase.mcprotocol.util.Util;

/**
 * A client implementing standard Minecraft protocol.
 */
public class StandardClient extends StandardConnection implements Client {

	/**
	 * The client's session id.
	 */
	private String sessionId;
	
	/**
	 * Whether the client is logged in.
	 */
	private boolean loggedIn;
	
	/**
	 * Creates a new standard client.
	 * @param host Host to connect to.
	 * @param port Port to connect to.
	 */
	public StandardClient(String host, int port) {
		super(host, port);
	}
	
	/**
	 * Gets the client's session id.
	 * @return The client's session id.
	 */
	public String getSessionId() {
		return this.sessionId;
	}

	@Override
	public boolean login(String username, String password) throws LoginException, OutdatedLibraryException {
		if(this.loggedIn || this.getUsername() != null) {
			throw new IllegalStateException("Already logged in with username: " + this.getUsername());
		}
		
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
					this.setUsername(values[2].trim());
					this.sessionId = values[3].trim();
					this.loggedIn = true;

					new Thread(new KeepAliveTask()).start();
				} catch (ArrayIndexOutOfBoundsException e) {
					throw new LoginException("Response contained incorrect amount of parameters: " + result);
				}

				Util.logger().info("Finished logging in to minecraft.net");
				return true;
			} else {
				if(result.trim().equals("Old version")) {
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
	
	@Override
	public void disconnect(String reason, boolean packet) {
		this.loggedIn = false;
		super.disconnect(reason, packet);
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
			while(loggedIn) {
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
	public void connect() throws ConnectException {
		try {
			Socket sock = new Socket(InetAddress.getByName(this.getRemoteHost()), this.getRemotePort());
			sock.setSoTimeout(30000);
			sock.setTrafficClass(24);
			super.connect(sock);
			this.send(new PacketHandshake(this.getUsername(), this.getRemoteHost(), this.getRemotePort()));
		} catch (UnknownHostException e) {
			throw new ConnectException("Unknown host: " + this.getRemoteHost());
		} catch (IOException e) {
			throw new ConnectException("Failed to open stream: " + this.getRemoteHost(), e);
		}
	}
	
}
