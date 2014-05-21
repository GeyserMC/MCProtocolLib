package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerStatisticsPacket implements Packet {
	
	private Map<String, Integer> statistics = new HashMap<String, Integer>();
	
	@SuppressWarnings("unused")
	private ServerStatisticsPacket() {
	}
	
	public ServerStatisticsPacket(Map<String, Integer> statistics) {
		this.statistics = statistics;
	}
	
	public Map<String, Integer> getStatistics() {
		return this.statistics;
	}

	@Override
	public void read(NetInput in) throws IOException {
		int length = in.readVarInt();
		for(int index = 0; index < length; index++) {
			this.statistics.put(in.readString(), in.readVarInt());
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeVarInt(this.statistics.size());
		for(String statistic : this.statistics.keySet()) {
			out.writeString(statistic);
			out.writeVarInt(this.statistics.get(statistic));
		}
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static class Achievement {
		public static final String OPEN_INVENTORY = "achievement.openInventory";
		public static final String GET_WOOD = "achievement.mineWood";
		public static final String MAKE_WORKBENCH = "achievement.buildWorkBench";
		public static final String MAKE_PICKAXE = "achievement.buildPickaxe";
		public static final String MAKE_FURNACE = "achievement.buildFurnace";
		public static final String GET_IRON = "achievement.acquireIron";
		public static final String MAKE_HOE = "achievement.buildHoe";
		public static final String MAKE_BREAD = "achievement.makeBread";
		public static final String MAKE_CAKE = "achievement.bakeCake";
		public static final String MAKE_IRON_PICKAXE = "achievement.buildBetterPickaxe";
		public static final String COOK_FISH = "achievement.cookFish";
		public static final String RIDE_MINECART_1000_BLOCKS = "achievement.onARail";
		public static final String MAKE_SWORD = "achievement.buildSword";
		public static final String KILL_ENEMY = "achievement.killEnemy";
		public static final String KILL_COW = "achievement.killCow";
		public static final String FLY_PIG = "achievement.flyPig";
		public static final String SNIPE_SKELETON = "achievement.snipeSkeleton";
		public static final String GET_DIAMONDS = "achievement.diamonds";
		public static final String GIVE_DIAMONDS = "achievement.diamondsToYou";
		public static final String ENTER_PORTAL = "achievement.portal";
		public static final String ATTACKED_BY_GHAST = "achievement.ghast";
		public static final String GET_BLAZE_ROD = "achievement.blazeRod";
		public static final String MAKE_POTION = "achievement.potion";
		public static final String GO_TO_THE_END = "achievement.theEnd";
		public static final String DEFEAT_ENDER_DRAGON = "achievement.theEnd2";
		public static final String DEAL_18_OR_MORE_DAMAGE = "achievement.overkill";
		public static final String MAKE_BOOKCASE = "achievement.bookcase";
		public static final String BREED_COW = "achievement.breedCow";
		public static final String SPAWN_WITHER = "achievement.spawnWither";
		public static final String KILL_WITHER = "achievement.killWither";
		public static final String MAKE_FULL_BEACON = "achievement.fullBeacon";
		public static final String EXPLORE_ALL_BIOMES = "achievement.exploreAllBiomes";
	}
	
	public static class Statistic {
		public static final String TIMES_LEFT_GAME = "stat.leaveGame";
		public static final String MINUTES_PLAYED = "stat.playOneMinute";
		public static final String BLOCKS_WALKED = "stat.walkOneCm";
		public static final String BLOCKS_SWAM = "stat.swimOneCm";
		public static final String BLOCKS_FALLEN = "stat.fallOneCm";
		public static final String BLOCKS_CLIMBED = "stat.climbOneCm";
		public static final String BLOCKS_FLOWN = "stat.flyOneCm";
		public static final String BLOCKS_DOVE = "stat.diveOneCm";
		public static final String BLOCKS_TRAVELLED_IN_MINECART = "stat.minecartOneCm";
		public static final String BLOCKS_TRAVELLED_IN_BOAT = "stat.boatOneCm";
		public static final String BLOCKS_RODE_ON_PIG = "stat.pigOneCm";
		public static final String BLOCKS_RODE_ON_HORSE = "stat.horseOneCm";
		public static final String TIMES_JUMPED = "stat.jump";
		public static final String TIMES_DROPPED_ITEMS = "stat.drop";
		public static final String TIMES_DEALT_DAMAGE = "stat.damageDealt";
		public static final String DAMAGE_TAKEN = "stat.damageTaken";
		public static final String DEATHS = "stat.deaths";
		public static final String MOB_KILLS = "stat.mobKills";
		public static final String ANIMALS_BRED = "stat.animalsBred";
		public static final String PLAYERS_KILLED = "stat.playerKills";
		public static final String FISH_CAUGHT = "stat.fishCaught";
		public static final String JUNK_FISHED = "stat.junkFished";
		public static final String TREASURE_FISHED = "stat.treasureFished";
		
		private static final String CRAFT_ITEM_BASE = "stat.craftItem.";
		private static final String BREAK_BLOCK_BASE = "stat.mineBlock.";
		private static final String USE_ITEM_BASE = "stat.useItem.";
		private static final String BREAK_ITEM_BASE = "stat.breakItem.";
		
		public static final String CRAFT_ITEM(int id) {
			return CRAFT_ITEM_BASE + id;
		}
		
		public static final String BREAK_BLOCK(int id) {
			return BREAK_BLOCK_BASE + id;
		}
		
		public static final String USE_ITEM(int id) {
			return USE_ITEM_BASE + id;
		}
		
		public static final String BREAK_ITEM(int id) {
			return BREAK_ITEM_BASE + id;
		}
	}

}
