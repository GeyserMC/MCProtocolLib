package ch.spacebase.mc.protocol.packet.ingame.server.world;

import java.io.IOException;

import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

public class ServerPlaySoundPacket implements Packet {

	private String sound;
	private double x;
	private double y;
	private double z;
	private float volume;
	private float pitch;
	
	@SuppressWarnings("unused")
	private ServerPlaySoundPacket() {
	}
	
	public ServerPlaySoundPacket(String sound, double x, double y, double z, float volume, float pitch) {
		this.sound = sound;
		this.x = x;
		this.y = y;
		this.z = z;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public String getSound() {
		return this.sound;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getZ() {
		return this.z;
	}
	
	public float getVolume() {
		return this.volume;
	}
	
	public float getPitch() {
		return this.pitch;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.sound = in.readString();
		this.x = in.readInt() / 8D;
		this.y = in.readInt() / 8D;
		this.z = in.readInt() / 8D;
		this.volume = in.readFloat();
		this.pitch = in.readUnsignedByte() / 63f;
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.sound);
		out.writeInt((int) (this.x * 8));
		out.writeInt((int) (this.y * 8));
		out.writeInt((int) (this.z * 8));
		out.writeFloat(this.volume);
		int pitch = (int) (this.pitch * 63);
		if(pitch > 255) {
			pitch = 255;
		}
		
		if(pitch < 0) {
			pitch = 0;
		}
		
		out.writeByte(pitch);
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	public static class Sound {
		public static final String CLICK = "random.click";
		public static final String FIZZ = "random.fizz";
		public static final String FIRE_AMBIENT = "fire.fire";
		public static final String IGNITE_FIRE = "fire.ignite";
		public static final String WATER_AMBIENT = "liquid.water";
		public static final String LAVA_AMBIENT = "liquid.lava";
		public static final String LAVA_POP = "liquid.lavapop";
		public static final String HARP = "note.harp";
		public static final String BASS_DRUM = "note.bd";
		public static final String SNARE_DRUM = "note.snare";
		public static final String HI_HAT = "note.hat";
		public static final String DOUBLE_BASS = "note.bassattack";
		public static final String PISTON_EXTEND = "tile.piston.out";
		public static final String PISTON_RETRACT = "tile.piston.in";
		public static final String PORTAL_AMBIENT = "portal.portal";
		public static final String TNT_PRIMED = "game.tnt.primed";
		public static final String BOW_HIT = "random.bowhit";
		public static final String COLLECT_ITEM = "random.pop";
		public static final String COLLECT_EXP = "random.orb";
		public static final String SUCCESSFUL_HIT = "random.successful_hit";
		public static final String FIREWORK_BLAST = "fireworks.blast";
		public static final String FIREWORK_LARGE_BLAST = "fireworks.largeBlast";
		public static final String FIREWORK_FAR_BLAST = "fireworks.blast_far";
		public static final String FIREWORK_FAR_LARGE_BLAST = "fireworks.largeBlast_far";
		public static final String FIREWORK_TWINKLE = "fireworks.twinkle";
		public static final String FIREWORK_FAR_TWINKLE = "fireworks.twinkle_far";
		public static final String RAIN_AMBIENT = "ambient.weather.rain";
		public static final String WITHER_SPAWN = "mob.wither.spawn";
		public static final String ENDER_DRAGON_DEATH = "mob.enderdragon.end";
		public static final String FIRE_PROJECTILE = "random.bow";
		public static final String DOOR_OPEN = "random.door_open";
		public static final String DOOR_CLOSE = "random.door_close";
		public static final String GHAST_CHARGE = "mob.ghast.charge";
		public static final String GHAST_FIRE = "mob.ghast.fireball";
		public static final String POUND_WOODEN_DOOR = "mob.zombie.wood";
		public static final String POUND_METAL_DOOR = "mob.zombie.metal";
		public static final String BREAK_WOODEN_DOOR = "mob.zombie.woodbreak";
		public static final String WITHER_SHOOT = "mob.wither.shoot";
		public static final String BAT_TAKE_OFF = "mob.bat.takeoff";
		public static final String INFECT_VILLAGER = "mob.zombie.infect";
		public static final String DISINFECT_VILLAGER = "mob.zombie.unfect";
		public static final String ANVIL_BREAK = "random.anvil_break";
		public static final String ANVIL_USE = "random.anvil_use";
		public static final String ANVIL_LAND = "random.anvil_land";
		public static final String BREAK_SPLASH_POTION = "game.potion.smash";
		public static final String THORNS_DAMAGE = "damage.thorns";
		public static final String EXPLOSION = "random.explode";
		public static final String CAVE_AMBIENT = "ambient.cave.cave";
		public static final String OPEN_CHEST = "random.chestopen";
		public static final String CLOSE_CHEST = "random.chestclosed";
		public static final String DIG_STONE = "dig.stone";
		public static final String DIG_WOOD = "dig.wood";
		public static final String DIG_GRAVEL = "dig.gravel";
		public static final String DIG_GRASS = "dig.grass";
		public static final String DIG_CLOTH = "dig.cloth";
		public static final String DIG_SAND = "dig.sand";
		public static final String DIG_SNOW = "dig.snow";
		public static final String DIG_GLASS = "dig.glass";
		public static final String ANVIL_STEP = "step.anvil";
		public static final String LADDER_STEP = "step.ladder";
		public static final String STONE_STEP = "step.stone";
		public static final String WOOD_STEP = "step.wood";
		public static final String GRAVEL_STEP = "step.gravel";
		public static final String GRASS_STEP = "step.grass";
		public static final String CLOTH_STEP = "step.cloth";
		public static final String SAND_STEP = "step.sand";
		public static final String SNOW_STEP = "step.snow";
		public static final String BURP = "random.burp";
		public static final String SADDLE_HORSE = "mob.horse.leather";
		public static final String ENDER_DRAGON_FLAP_WINGS = "mob.enderdragon.wings";
		public static final String THUNDER_AMBIENT = "ambient.weather.thunder";
		public static final String LAUNCH_FIREWORKS = "fireworks.launch";
		public static final String CREEPER_PRIMED = "creeper.primed";
		public static final String ENDERMAN_STARE = "mob.endermen.stare";
		public static final String ENDERMAN_TELEPORT = "mob.endermen.portal";
		public static final String IRON_GOLEM_THROW = "mob.irongolem.throw";
		public static final String IRON_GOLEM_WALK = "mob.irongolem.walk";
		public static final String ZOMBIE_PIGMAN_ANGRY = "mob.zombiepig.zpigangry";
		public static final String SILVERFISH_STEP = "mob.silverfish.step";
		public static final String SKELETON_STEP = "mob.skeleton.step";
		public static final String SPIDER_STEP = "mob.spider.step";
		public static final String ZOMBIE_STEP = "mob.zombie.step";
		public static final String ZOMBIE_CURE = "mob.zombie.remedy";
		public static final String CHICKEN_LAY_EGG = "mob.chicken.plop";
		public static final String CHICKEN_STEP = "mob.chicken.step";
		public static final String COW_STEP = "mob.cow.step";
		public static final String HORSE_EATING = "eating";
		public static final String HORSE_LAND = "mob.horse.land";
		public static final String HORSE_WEAR_ARMOR = "mob.horse.armor";
		public static final String HORSE_GALLOP = "mob.horse.gallop";
		public static final String HORSE_BREATHE = "mob.horse.breathe";
		public static final String HORSE_WOOD_STEP = "mob.horse.wood";
		public static final String HORSE_SOFT_STEP = "mob.horse.soft";
		public static final String HORSE_JUMP = "mob.horse.jump";
		public static final String SHEAR_SHEEP = "mob.sheep.shear";
		public static final String PIG_STEP = "mob.pig.step";
		public static final String SHEEP_STEP = "mob.sheep.step";
		public static final String VILLAGER_YES = "mob.villager.yes";
		public static final String VILLAGER_NO = "mob.villager.no";
		public static final String WOLF_STEP = "mob.wolf.step";
		public static final String WOLF_SHAKE = "mob.wolf.shake";
		public static final String DRINK = "random.drink";
		public static final String EAT = "random.eat";
		public static final String LEVEL_UP = "random.levelup";
		public static final String FISH_HOOK_SPLASH = "random.splash";
		public static final String ITEM_BREAK = "random.break";
		public static final String SWIM = "game.neutral.swim";
		public static final String SPLASH = "game.neutral.swim.splash";
		public static final String HURT = "game.neutral.hurt";
		public static final String DEATH = "game.neutral.die";
		public static final String BIG_FALL = "game.neutral.hurt.fall.big";
		public static final String SMALL_FALL = "game.neutral.hurt.fall.small";
		public static final String MOB_SWIM = "game.hostile.swim";
		public static final String MOB_SPLASH = "game.hostile.swim.splash";
		public static final String PLAYER_SWIM = "game.player.swim";
		public static final String PLAYER_SPLASH = "game.player.swim.splash";
		public static final String ENDER_DRAGON_GROWL = "mob.enderdragon.growl";
		public static final String WITHER_IDLE = "mob.wither.idle";
		public static final String BLAZE_BREATHE = "mob.blaze.breathe";
		public static final String ENDERMAN_SCREAM = "mob.endermen.scream";
		public static final String ENDERMAN_IDLE = "mob.endermen.idle";
		public static final String GHAST_MOAN = "mob.ghast.moan";
		public static final String ZOMBIE_PIGMAN_IDLE = "mob.zombiepig.zpig";
		public static final String SILVERFISH_IDLE = "mob.silverfish.say";
		public static final String SKELETON_IDLE = "mob.skeleton.say";
		public static final String SPIDER_IDLE = "mob.spider.say";
		public static final String WITCH_IDLE = "mob.witch.idle";
		public static final String ZOMBIE_IDLE = "mob.zombie.say";
		public static final String BAT_IDLE = "mob.bat.idle";
		public static final String CHICKEN_IDLE = "mob.chicken.say";
		public static final String COW_IDLE = "mob.cow.say";
		public static final String HORSE_IDLE = "mob.horse.idle";
		public static final String DONKEY_IDLE = "mob.horse.donkey.idle";
		public static final String ZOMBIE_HORSE_IDLE = "mob.horse.zombie.idle";
		public static final String SKELETON_HORSE_IDLE = "mob.horse.skeleton.idle";
		public static final String OCELOT_PURR = "mob.cat.purr";
		public static final String OCELOT_PURR_MEOW = "mob.cat.purreow";
		public static final String OCELOT_MEOW = "mob.cat.meow";
		public static final String PIG_IDLE = "mob.pig.say";
		public static final String SHEEP_IDLE = "mob.sheep.say";
		public static final String VILLAGER_HAGGLE = "mob.villager.haggle";
		public static final String VILLAGER_IDLE = "mob.villager.idle";
		public static final String WOLF_GROWL = "mob.wolf.growl";
		public static final String WOLF_PANT = "mob.wolf.panting";
		public static final String WOLF_WHINE = "mob.wolf.whine";
		public static final String WOLF_BARK = "mob.wolf.bark";
		public static final String MOB_BIG_FALL = "game.hostile.hurt.fall.big";
		public static final String MOB_SMALL_FALL = "game.hostile.hurt.fall.small";
		public static final String PLAYER_BIG_FALL = "game.player.hurt.fall.big";
		public static final String PLAYER_SMALL_FALL = "game.player.hurt.fall.small";
		public static final String ENDER_DRAGON_HURT = "mob.enderdragon.hit";
		public static final String WITHER_HURT = "mob.wither.hurt";
		public static final String WITHER_DEATH = "mob.wither.death";
		public static final String BLAZE_HURT = "mob.blaze.hit";
		public static final String BLAZE_DEATH = "mob.blaze.death";
		public static final String CREEPER_HURT = "mob.creeper.say";
		public static final String CREEPER_DEATH = "mob.creeper.death";
		public static final String ENDERMAN_HURT = "mob.endermen.hit";
		public static final String ENDERMAN_DEATH = "mob.endermen.death";
		public static final String GHAST_HURT = "mob.ghast.scream";
		public static final String GHAST_DEATH = "mob.ghast.death";
		public static final String IRON_GOLEM_HURT = "mob.irongolem.hit";
		public static final String IRON_GOLEM_DEATH = "mob.irongolem.death";
		public static final String MOB_HURT = "game.hostile.hurt";
		public static final String MOB_DEATH = "game.hostile.die";
		public static final String ZOMBIE_PIGMAN_HURT = "mob.zombiepig.zpighurt";
		public static final String ZOMBIE_PIGMAN_DEATH = "mob.zombiepig.zpigdeath";
		public static final String SILVERFISH_HURT = "mob.silverfish.hit";
		public static final String SILVERFISH_DEATH = "mob.silverfish.kill";
		public static final String SKELETON_HURT = "mob.skeleton.hurt";
		public static final String SKELETON_DEATH = "mob.skeleton.death";
		public static final String SLIME = "mob.slime.small";
		public static final String BIG_SLIME = "mob.slime.big";
		public static final String SPIDER_DEATH = "mob.spider.death";
		public static final String WITCH_HURT = "mob.witch.hurt";
		public static final String WITCH_DEATH = "mob.witch.death";
		public static final String ZOMBIE_HURT = "mob.zombie.hurt";
		public static final String ZOMBIE_DEATH = "mob.zombie.death";
		public static final String PLAYER_HURT = "game.player.hurt";
		public static final String PLAYER_DEATH = "game.player.die";
		public static final String WOLF_HURT = "mob.wolf.hurt";
		public static final String WOLF_DEATH = "mob.wolf.death";
		public static final String VILLAGER_HURT = "mob.villager.hit";
		public static final String VILLAGER_DEATH = "mob.villager.death";
		public static final String PIG_DEATH = "mob.pig.death";
		public static final String OCELOT_HURT = "mob.cat.hitt";
		public static final String HORSE_HURT = "mob.horse.hit";
		public static final String DONKEY_HURT = "mob.horse.donkey.hit";
		public static final String ZOMBIE_HORSE_HURT = "mob.horse.zombie.hit";
		public static final String SKELETON_HORSE_HURT = "mob.horse.skeleton.hit";
		public static final String HORSE_DEATH = "mob.horse.death";
		public static final String DONKEY_DEATH = "mob.horse.donkey.death";
		public static final String ZOMBIE_HORSE_DEATH = "mob.horse.zombie.death";
		public static final String SKELETON_HORSE_DEATH = "mob.horse.skeleton.death";
		public static final String COW_HURT = "mob.cow.hurt";
		public static final String CHICKEN_HURT = "mob.chicken.hurt";
		public static final String BAT_HURT = "mob.bat.hurt";
		public static final String BAT_DEATH = "mob.bat.death";
		public static final String MOB_ATTACK = "mob.attack";
	}
	
}
