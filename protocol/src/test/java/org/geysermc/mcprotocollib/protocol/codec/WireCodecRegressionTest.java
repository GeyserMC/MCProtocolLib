package org.geysermc.mcprotocollib.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3d;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.geysermc.mcprotocollib.protocol.data.game.Holder;
import org.geysermc.mcprotocollib.protocol.data.game.chat.ChatFilterType;
import org.geysermc.mcprotocollib.protocol.data.game.chat.ChatTypeDecoration;
import org.geysermc.mcprotocollib.protocol.data.game.command.CommandNode;
import org.geysermc.mcprotocollib.protocol.data.game.command.CommandParser;
import org.geysermc.mcprotocollib.protocol.data.game.command.CommandType;
import org.geysermc.mcprotocollib.protocol.data.game.debug.DebugGameEventInfo;
import org.geysermc.mcprotocollib.protocol.data.game.debug.DebugPathInfo;
import org.geysermc.mcprotocollib.protocol.data.game.debug.DebugSubscriptions;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.WardenData;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentMatchers;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentTypes;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.ItemTypes;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.PotionContents;
import org.geysermc.mcprotocollib.protocol.data.game.level.LightUpdateData;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.ElderGuardianEffectValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.Particle;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.ParticleType;
import org.geysermc.mcprotocollib.protocol.packet.common.serverbound.ServerboundCustomClickActionPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundCommandsPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundRecipeBookAddPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundAddEntityPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundGameEventPacket;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class WireCodecRegressionTest {
    // These packet-body fixtures were checked against the official 26.3 codecs.
    @Test
    void blockPropertyListsKeepTheirCountAndValues() {
        String[] fixtures = {
            "000100000000",
            "000102036167650101310b77617465726c6f67676564010566616c7365000000"
        };
        for (int i = 0; i < fixtures.length; i++) {
            var predicate = decode(fixtures[i], ItemTypes::readBlockPredicate);
            assertEquals(i * 2, predicate.getProperties().size());
            assertEquals(fixtures[i], encode(out -> ItemTypes.writeBlockPredicate(out, predicate)));
        }
        var predicate = decode(fixtures[1], ItemTypes::readBlockPredicate);
        assertEquals("age", predicate.getProperties().getFirst().getName());
        assertEquals("false", predicate.getProperties().get(1).getValue());
    }

    @Test
    void rangedPropertiesKeepOptionalEndpoints() {
        String[] fixtures = {
            "0001010361676500010131010133000000",
            "000101036167650000010133000000",
            "000101036167650001013100000000"
        };
        for (String fixture : fixtures) {
            var predicate = decode(fixture, ItemTypes::readBlockPredicate);
            assertNull(predicate.getProperties().getFirst().getValue());
            assertEquals(fixture, encode(out -> ItemTypes.writeBlockPredicate(out, predicate)));
        }
        assertNull(decode(fixtures[1], ItemTypes::readBlockPredicate).getProperties().getFirst().getMinValue());
        assertNull(decode(fixtures[2], ItemTypes::readBlockPredicate).getProperties().getFirst().getMaxValue());
    }

    @Test
    void customClickPayloadsUseTheRootTagForPresence() {
        Map<String, Object> fixtures = Map.of(
            "020a00", NbtMap.EMPTY,
            "0a0a030001780000000100", NbtMap.builder().putInt("x", 1).build(),
            "0808000568656c6c6f", "hello",
            "05030000002a", 42
        );
        for (var fixture : fixtures.entrySet()) {
            String wire = "0e6d696e6563726166743a74657374" + fixture.getKey();
            var packet = decode(wire, ServerboundCustomClickActionPacket::new);
            assertEquals(fixture.getValue(), packet.getPayload());
            assertEquals(wire, encode(packet::serialize));
        }
        String absent = "0e6d696e6563726166743a746573740100";
        var packet = decode(absent, ServerboundCustomClickActionPacket::new);
        assertNull(packet.getPayload());
        assertEquals(absent, encode(packet::serialize));
    }

    @Test
    void lengthPrefixedValuesCannotConsumeFollowingFields() {
        ByteBuf input = Unpooled.wrappedBuffer(new byte[]{2, 7, 99, 42});
        try {
            assertEquals(7, MinecraftTypes.readLengthPrefixed(input, 64, MinecraftTypes::readVarInt));
            assertEquals(42, input.readUnsignedByte());
            assertFalse(input.isReadable());
        } finally {
            input.release();
        }
        input = Unpooled.wrappedBuffer(new byte[]{0, 7});
        try {
            ByteBuf bounded = input;
            assertThrows(IndexOutOfBoundsException.class,
                () -> MinecraftTypes.readLengthPrefixed(bounded, 64, MinecraftTypes::readVarInt));
            assertEquals(7, input.readUnsignedByte());
        } finally {
            input.release();
        }
        var patch = decode("020004017f030107", in -> MinecraftTypes.readDataComponentPatch(in, true));
        assertEquals(7, patch.getDataComponents().get(DataComponentTypes.DAMAGE).getValue());
        assertNotNull(patch.getDataComponents().get(DataComponentTypes.UNBREAKABLE));
    }

    @Test
    void particleWritersKeepEveryPreviouslyOmittedPayload() {
        Map<ParticleType, String> fixtures = Map.of(
            ParticleType.GEYSER, "0700000001",
            ParticleType.GEYSER_BASE, "08000000013f800000",
            ParticleType.GEYSER_POOF, "09000000013f800000",
            ParticleType.GEYSER_PLUME, "0a00000001",
            ParticleType.DRAGON_BREATH, "0f3f800000",
            ParticleType.EFFECT, "17000000013f800000",
            ParticleType.TINTED_LEAVES, "2e00000001",
            ParticleType.FLASH, "3400000001",
            ParticleType.INSTANT_EFFECT, "38000000013f800000"
        );
        for (var fixture : fixtures.entrySet()) {
            Particle particle = decode(fixture.getValue(), MinecraftTypes::readParticle);
            assertEquals(fixture.getKey(), particle.getType());
            assertNotNull(particle.getData());
            assertEquals(fixture.getValue(), encode(out -> MinecraftTypes.writeParticle(out, particle)));
        }
    }

    @Test
    void commandArgumentsWithNoPropertiesCanBeForwarded() {
        Set<CommandParser> withProperties = Set.of(CommandParser.DOUBLE, CommandParser.FLOAT,
            CommandParser.INTEGER, CommandParser.LONG, CommandParser.STRING, CommandParser.ENTITY,
            CommandParser.SCORE_HOLDER, CommandParser.TIME, CommandParser.RESOURCE_OR_TAG,
            CommandParser.RESOURCE_OR_TAG_KEY, CommandParser.RESOURCE, CommandParser.RESOURCE_KEY,
            CommandParser.RESOURCE_SELECTOR);
        int checked = 0;
        for (CommandParser parser : CommandParser.values()) {
            if (withProperties.contains(parser)) {
                continue;
            }
            var node = new CommandNode(CommandType.ARGUMENT, false, false, new int[0], OptionalInt.empty(),
                "value", parser, null, null);
            var original = new ClientboundCommandsPacket(new CommandNode[]{node}, 0);
            String wire = encode(original::serialize);
            var decoded = decode(wire, ClientboundCommandsPacket::new);
            assertEquals(parser, decoded.getNodes()[0].getParser());
            assertNull(decoded.getNodes()[0].getProperties());
            assertEquals(wire, encode(decoded::serialize));
            checked++;
        }
        assertEquals(49, checked);
    }

    @Test
    void recipeGroupZeroDoesNotBecomeAbsent() {
        String fixture = "010101020102010101010100010102010101";
        var packet = decode(fixture, ClientboundRecipeBookAddPacket::new);
        assertEquals(OptionalInt.of(0), packet.getEntries().getFirst().contents().group());
        assertEquals(fixture, encode(packet::serialize));
    }

    @Test
    void partiallyFilteredChatPreservesItsMaskAndFollowingFields() {
        BitSet mask = new BitSet();
        mask.set(0);
        mask.set(65);
        for (ChatFilterType filter : ChatFilterType.values()) {
            var packet = new ClientboundPlayerChatPacket(3, new UUID(1, 2), 4, null, "message", 5, 6,
                List.of(), null, filter, mask, Holder.ofId(0), Component.text("sender"), Component.text("target"));
            String wire = encode(packet::serialize);
            if (filter == ChatFilterType.PARTIALLY_FILTERED) {
                assertEquals("03000000000000000100000000000000020400076d6573736167650000000000000005"
                    + "0000000000000006000002090100000000000000020108000673656e64657201080006746172676574", wire);
            }
            var decoded = decode(wire, ClientboundPlayerChatPacket::new);
            assertEquals(filter, decoded.getFilterMask());
            assertEquals(filter == ChatFilterType.PARTIALLY_FILTERED ? mask : new BitSet(), decoded.getFilteredIndices());
            assertEquals(0, decoded.getChatType().id());
            assertEquals(Component.text("sender"), decoded.getName());
            assertEquals(Component.text("target"), decoded.getTargetName());
            assertEquals(wire, encode(decoded::serialize));
        }
    }

    @Test
    void partialComponentPredicatesPreserveNonCompoundTags() {
        List<Object> tags = List.of(new NbtList<>(NbtType.END, List.of()), "{}", NbtMap.EMPTY);
        int[] predicateIds = {1, 4, 0};
        for (int i = 0; i < tags.size(); i++) {
            Object tag = tags.get(i);
            var key = new DataComponentMatchers.PredicateType(true, predicateIds[i]);
            var matchers = new DataComponentMatchers(Map.of(), Map.of(key, tag));
            String wire = encode(out -> ItemTypes.writeDataComponentMatchers(out, matchers));
            var decoded = decode(wire, ItemTypes::readDataComponentMatchers);
            assertEquals(tag, decoded.partialMatchers().get(key));
            assertEquals(wire, encode(out -> ItemTypes.writeDataComponentMatchers(out, decoded)));
        }
    }

    @Test
    void lightingEntryPointsUseTheSameByteArrayMasks() {
        String fixture = "01010200010300000104000000010000";
        var data = decode(fixture, MinecraftTypes::readLightUpdateData);
        assertEquals(BitSet.valueOf(new byte[]{1}), data.getSkyYMask());
        assertEquals(BitSet.valueOf(new byte[]{0, 1}), data.getBlockYMask());
        assertEquals(BitSet.valueOf(new byte[]{0, 0, 1}), data.getEmptySkyYMask());
        assertEquals(BitSet.valueOf(new byte[]{0, 0, 0, 1}), data.getEmptyBlockYMask());
        assertEquals(data, decode(fixture, LightUpdateData::read));
        assertEquals(fixture, encode(out -> MinecraftTypes.writeLightUpdateData(out, data)));
        assertEquals(fixture, encode(out -> LightUpdateData.write(out, data)));
    }

    @Test
    void debugGameEventsKeepFractionalCoordinates() {
        String fixture = "023ff4000000000000c004000000000000400e000000000000";
        var event = (DebugGameEventInfo) decode(fixture,
            in -> MinecraftTypes.readDebugSubscription(in, DebugSubscriptions.GAME_EVENTS));
        assertEquals(2, event.eventId());
        assertEquals(Vector3d.from(1.25, -2.5, 3.75), event.pos());
        assertEquals(fixture, encode(out -> MinecraftTypes.writeDebugSubscription(out, DebugSubscriptions.GAME_EVENTS, event)));
    }

    @Test
    void debugNodesAcceptTheNewPathType() {
        byte[] bytes = new byte[26];
        bytes[21] = 26;
        String fixture = HexFormat.of().formatHex(bytes);
        var node = decode(fixture, MinecraftTypes::readDebugPathNode);
        assertEquals(DebugPathInfo.PathType.BIG_MOBS_CLOSE_TO_DANGER, node.type());
        assertEquals(fixture, encode(out -> MinecraftTypes.writeDebugPathNode(out, node)));
    }

    @Test
    void elderGuardianEffectsKeepTheAudibleFlag() {
        String fixture = "0a3f800000";
        var packet = decode(fixture, ClientboundGameEventPacket::new);
        assertTrue(((ElderGuardianEffectValue) packet.getValue()).isAudible());
        assertEquals(fixture, encode(packet::serialize));
    }

    @Test
    void wardenSpawnKeepsTheEmergingFlag() {
        var packet = new ClientboundAddEntityPacket(1, new UUID(1, 2), EntityType.WARDEN,
            new WardenData(1), 0, 0, 0, 0, 0, 0);
        String wire = encode(packet::serialize);
        assertTrue(wire.endsWith("01"));
        var decoded = decode(wire, ClientboundAddEntityPacket::new);
        assertTrue(((WardenData) decoded.getData()).isEmerging());
    }

    @Test
    void signedPotionColorsKeepTheirPresence() {
        for (int color : new int[]{-1, Integer.MIN_VALUE, 0xff336699, 0}) {
            var contents = new PotionContents(-1, OptionalInt.of(color), List.of(), null);
            String wire = encode(out -> ItemTypes.writePotionContents(out, contents));
            var decoded = decode(wire, ItemTypes::readPotionContents);
            assertTrue(decoded.hasCustomColor());
            assertEquals(color, decoded.getCustomColor());
            assertEquals(wire, encode(out -> ItemTypes.writePotionContents(out, decoded)));
            assertTrue(decoded.toBuilder().build().hasCustomColor());
            assertEquals(color, decoded.toBuilder().build().getCustomColor());
        }
        assertFalse(new PotionContents(-1, -1, List.of(), null).hasCustomColor());
        assertFalse(PotionContents.builder().customColor(-1).customEffects(List.of()).build().hasCustomColor());
        var signed = PotionContents.builder().customColor(OptionalInt.of(-1)).customEffects(List.of()).build();
        assertTrue(signed.hasCustomColor());
        assertEquals(-1, signed.getCustomColor());
    }

    @Test
    void chatDecorationIdsKeepTheirMeanings() {
        ChatTypeDecoration.Parameter[] expected = {ChatTypeDecoration.Parameter.SENDER,
            ChatTypeDecoration.Parameter.TARGET, ChatTypeDecoration.Parameter.CONTENT};
        for (int id = 0; id < expected.length; id++) {
            String wire = "0001" + HexFormat.of().toHexDigits((byte) id) + "0a00";
            var decoration = decode(wire, MinecraftTypes::readChatTypeDecoration);
            assertEquals(List.of(expected[id]), decoration.parameters());
            assertEquals(wire, encode(out -> MinecraftTypes.writeChatTypeDecoration(out, decoration)));
        }
        assertEquals(ChatTypeDecoration.Parameter.SENDER, ChatTypeDecoration.Parameter.fromId(100));
    }

    private static <T> T decode(String hex, Function<ByteBuf, T> reader) {
        ByteBuf input = Unpooled.wrappedBuffer(HexFormat.of().parseHex(hex));
        try {
            T value = reader.apply(input);
            assertFalse(input.isReadable(), "Codec left trailing bytes");
            return value;
        } finally {
            input.release();
        }
    }

    private static String encode(Consumer<ByteBuf> writer) {
        ByteBuf output = Unpooled.buffer();
        try {
            writer.accept(output);
            return ByteBufUtil.hexDump(output);
        } finally {
            output.release();
        }
    }
}
