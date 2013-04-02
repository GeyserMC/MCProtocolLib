package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.util.IOUtils;

public class PacketTeam extends Packet {

	public String name;
	public byte action;
	public String displayName;
	public String prefix;
	public String suffix;
	public byte friendlyFire;
	public String players[];

	public PacketTeam() {
	}

	/**
	 * Creates a packet for removing a team.
	 * @param name Name of the team.
	 */
	public PacketTeam(String name) {
		this.name = name;
		this.action = 1;
	}
	
	/**
	 * Creates a packet for adding or removing a team member.
	 * @param name Name of the team.
	 * @param players Players to add or remove
	 * @param add Whether to add or remove players.
	 */
	public PacketTeam(String name, String players[], boolean add) {
		this.name = name;
		this.players = players;
		this.action = add ? (byte) 3 : 4;
	}
	
	/**
	 * Creates a packet for updating a team.
	 * @param name Name of the team.
	 * @param displayName Display name of the team.
	 * @param prefix Prefix for team members.
	 * @param suffix Suffix for team members.
	 * @param friendlyFire Friendly fire mode for the team.
	 */
	public PacketTeam(String name, String displayName, String prefix, String suffix, byte friendlyFire) {
		this.name = name;
		this.displayName = displayName;
		this.prefix = prefix;
		this.suffix = suffix;
		this.friendlyFire = friendlyFire;
		this.action = 2;
	}
	
	/**
	 * Creates a packet for adding a team.
	 * @param name Name of the team.
	 * @param displayName Display name of the team.
	 * @param prefix Prefix for team members.
	 * @param suffix Suffix for team members.
	 * @param friendlyFire Friendly fire mode for the team.
	 * @param players Players in the team.
	 */
	public PacketTeam(String name, String displayName, String prefix, String suffix, byte friendlyFire, String players[]) {
		this.name = name;
		this.displayName = displayName;
		this.prefix = prefix;
		this.suffix = suffix;
		this.friendlyFire = friendlyFire;
		this.players = players;
		this.action = 0;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.name = IOUtils.readString(in);
		this.action = in.readByte();
		switch(this.action) {
			case 0:
				this.displayName = IOUtils.readString(in);
				this.prefix = IOUtils.readString(in);
				this.suffix = IOUtils.readString(in);
				this.friendlyFire = in.readByte();
				this.players = new String[in.readShort()];
				for(int ind = 0; ind < this.players.length; ind++) {
					this.players[ind] = IOUtils.readString(in);
				}
				
				break;
			case 1:
				break;
			case 2:
				this.displayName = IOUtils.readString(in);
				this.prefix = IOUtils.readString(in);
				this.suffix = IOUtils.readString(in);
				this.friendlyFire = in.readByte();
				break;
			case 3:
			case 4:
				this.players = new String[in.readShort()];
				for(int ind = 0; ind < this.players.length; ind++) {
					this.players[ind] = IOUtils.readString(in);
				}
				
				break;
		}
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		IOUtils.writeString(out, this.name);
		out.writeByte(this.action);
		switch(this.action) {
			case 0:
				IOUtils.writeString(out, this.displayName);
				IOUtils.writeString(out, this.prefix);
				IOUtils.writeString(out, this.suffix);
				out.writeByte(this.friendlyFire);
				out.writeShort(this.players.length);
				for(int ind = 0; ind < this.players.length; ind++) {
					IOUtils.writeString(out, this.players[ind]);
				}
				
				break;
			case 1:
				break;
			case 2:
				IOUtils.writeString(out, this.displayName);
				IOUtils.writeString(out, this.prefix);
				IOUtils.writeString(out, this.suffix);
				out.writeByte(this.friendlyFire);
				break;
			case 3:
			case 4:
				out.writeShort(this.players.length);
				for(int ind = 0; ind < this.players.length; ind++) {
					IOUtils.writeString(out, this.players[ind]);
				}
				
				break;
		}
	}

	@Override
	public void handleClient(Client conn) {
	}

	@Override
	public void handleServer(ServerConnection conn) {
	}

	@Override
	public int getId() {
		return 209;
	}

}
