package org.geysermc.mcprotocollib.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.data.handshake.HandshakeIntent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.ItemTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.LightUpdateData;
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundCustomReportDetailsPacket;
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundResourcePackPushPacket;
import org.geysermc.mcprotocollib.protocol.packet.common.serverbound.ServerboundClientInformationPacket;
import org.geysermc.mcprotocollib.protocol.packet.common.serverbound.ServerboundCustomPayloadPacket;
import org.geysermc.mcprotocollib.protocol.packet.handshake.serverbound.ClientIntentionPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundCommandsPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerInfoUpdatePacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundRemoveEntitiesPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundSetPassengersPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory.ServerboundContainerClickPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory.ServerboundEditBookPacket;
import org.geysermc.mcprotocollib.protocol.packet.login.serverbound.ServerboundHelloPacket;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class CodecBoundsRegressionTest {
    @Test
    void impossibleByteAndLongArrayCountsFailBeforeAllocation() {
        for (int count : new int[]{-1, Integer.MAX_VALUE, 100}) {
            rejects(buf -> MinecraftTypes.writeVarInt(buf, count), MinecraftTypes::readByteArray);
            rejects(buf -> MinecraftTypes.writeVarInt(buf, count), MinecraftTypes::readLongArray);
        }
        ByteBuf input = Unpooled.buffer();
        try {
            MinecraftTypes.writeByteArray(input, new byte[]{1, 2});
            assertArrayEquals(new byte[]{1, 2}, MinecraftTypes.readByteArray(input));
            MinecraftTypes.writeLongArray(input, new long[]{1, Long.MIN_VALUE});
            assertArrayEquals(new long[]{1, Long.MIN_VALUE}, MinecraftTypes.readLongArray(input));
        } finally {
            input.release();
        }
    }

    @Test
    void packetAndNestedArrayCountsAreBoundedByAvailableInput() {
        for (int count : new int[]{-1, Integer.MAX_VALUE}) {
            rejects(buf -> MinecraftTypes.writeVarInt(buf, count), ClientboundRemoveEntitiesPacket::new);
            rejects(buf -> MinecraftTypes.writeVarInt(buf, count), ClientboundCommandsPacket::new);
            rejects(buf -> {
                buf.writeByte(0);
                MinecraftTypes.writeVarInt(buf, count);
            }, ClientboundPlayerInfoUpdatePacket::new);
            rejects(buf -> {
                MinecraftTypes.writeVarInt(buf, 1);
                MinecraftTypes.writeVarInt(buf, count);
            }, ClientboundSetPassengersPacket::new);
            rejects(buf -> {
                MinecraftTypes.writeString(buf, "");
                MinecraftTypes.writeVarInt(buf, count);
            }, MinecraftTypes::readChatTypeDecoration);
            rejects(buf -> {
                buf.writeZero(4);
                MinecraftTypes.writeVarInt(buf, count);
            }, LightUpdateData::read);
            rejects(buf -> {
                buf.writeByte(0);
                MinecraftTypes.writeVarInt(buf, count);
            }, ItemTypes::readFireworkExplosion);
            rejects(buf -> {
                buf.writeZero(6);
                MinecraftTypes.writeVarInt(buf, count);
            }, ServerboundContainerClickPacket::new);
        }
    }

    @Test
    void lengthPrefixedValuesRejectImpossibleLengths() {
        for (int count : new int[]{-1, 3, Integer.MAX_VALUE}) {
            rejects(buf -> {
                MinecraftTypes.writeVarInt(buf, count);
                buf.writeByte(0);
            }, buf -> MinecraftTypes.readLengthPrefixed(buf, 2, ByteBuf::readByte));
        }
    }

    @Test
    void bookPageCountAndStringLimitsApplyInBothDirections() {
        for (ServerboundEditBookPacket packet : List.of(
            new ServerboundEditBookPacket(0, List.of("x".repeat(1025)), null),
            new ServerboundEditBookPacket(0, Collections.nCopies(101, ""), null),
            new ServerboundEditBookPacket(0, List.of(), "x".repeat(33)))) {
            rejects(out -> {
                MinecraftTypes.writeVarInt(out, packet.getSlot());
                MinecraftTypes.writeList(out, packet.getPages(), MinecraftTypes::writeString);
                MinecraftTypes.writeNullable(out, packet.getTitle(), MinecraftTypes::writeString);
            }, ServerboundEditBookPacket::new);
            rejectsWrite(packet::serialize);
        }
        var maximum = new ServerboundEditBookPacket(0, Collections.nCopies(100, "x".repeat(1024)), "x".repeat(32));
        assertPacketRoundTrip(maximum, ServerboundEditBookPacket::new);
    }

    @Test
    void customReportDetailsRetainTheirCollectionAndStringLimits() {
        Map<String, String> entries = new HashMap<>();
        for (int i = 0; i < 33; i++) {
            entries.put("key" + i, "");
        }
        rejects(buf -> {
            MinecraftTypes.writeVarInt(buf, entries.size());
            entries.forEach((key, value) -> {
                MinecraftTypes.writeString(buf, key);
                MinecraftTypes.writeString(buf, value);
            });
        }, ClientboundCustomReportDetailsPacket::new);
        rejectsWrite(new ClientboundCustomReportDetailsPacket(entries)::serialize);
        rejectsWrite(new ClientboundCustomReportDetailsPacket(Map.of("x".repeat(129), ""))::serialize);
        rejectsWrite(new ClientboundCustomReportDetailsPacket(Map.of("key", "x".repeat(4097)))::serialize);
        assertPacketRoundTrip(new ClientboundCustomReportDetailsPacket(Map.of("x".repeat(128), "x".repeat(4096))),
            ClientboundCustomReportDetailsPacket::new);
    }

    @Test
    void loginAndResourcePackStringsUseTheirSpecificLimits() {
        var hello = new ServerboundHelloPacket("x".repeat(17), new UUID(0, 0));
        rejects(buf -> {
            MinecraftTypes.writeString(buf, hello.getUsername());
            MinecraftTypes.writeUUID(buf, hello.getProfileId());
        }, ServerboundHelloPacket::new);
        rejectsWrite(hello::serialize);
        assertPacketRoundTrip(new ServerboundHelloPacket("x".repeat(16), new UUID(0, 0)), ServerboundHelloPacket::new);
        var pack = new ClientboundResourcePackPushPacket(new UUID(0, 0), "", "x".repeat(41), false, null);
        rejects(buf -> {
            MinecraftTypes.writeUUID(buf, pack.getId());
            MinecraftTypes.writeString(buf, pack.getUrl());
            MinecraftTypes.writeString(buf, pack.getHash());
            buf.writeZero(2);
        }, ClientboundResourcePackPushPacket::new);
        rejectsWrite(pack::serialize);
        assertPacketRoundTrip(new ClientboundResourcePackPushPacket(new UUID(0, 0), "", "x".repeat(40), false, null),
            ClientboundResourcePackPushPacket::new);
    }

    @Test
    void handshakeAndLanguageKeepTheirAsymmetricStringLimits() {
        var handshake = new ClientIntentionPacket(777, "x".repeat(256), 25565, HandshakeIntent.STATUS);
        rejects(handshake::serialize, ClientIntentionPacket::new);
        assertPacketRoundTrip(new ClientIntentionPacket(777, "x".repeat(255), 25565, HandshakeIntent.STATUS),
            ClientIntentionPacket::new);
        rejects(buf -> {
            MinecraftTypes.writeString(buf, "x".repeat(17));
            buf.writeZero(8);
        }, ServerboundClientInformationPacket::new);
    }

    @Test
    void unknownCustomPayloadLimitsDoNotApplyToKnownChannels() {
        Key unknown = Key.key("test:unknown");
        var tooLarge = new ServerboundCustomPayloadPacket(unknown, new byte[32768]);
        rejects(buf -> {
            MinecraftTypes.writeResourceLocation(buf, unknown);
            buf.writeZero(32768);
        }, ServerboundCustomPayloadPacket::new);
        rejectsWrite(tooLarge::serialize);
        assertPacketRoundTrip(new ServerboundCustomPayloadPacket(unknown, new byte[32767]), ServerboundCustomPayloadPacket::new);
        ByteBuf brand = Unpooled.buffer();
        try {
            MinecraftTypes.writeString(brand, "é".repeat(20000));
            byte[] data = new byte[brand.readableBytes()];
            brand.readBytes(data);
            assertTrue(data.length > 32767);
            assertPacketRoundTrip(new ServerboundCustomPayloadPacket(Key.key("minecraft:brand"), data), ServerboundCustomPayloadPacket::new);
        } finally {
            brand.release();
        }
        var client = new org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundCustomPayloadPacket(unknown, new byte[1048577]);
        rejectsWrite(client::serialize);
        rejects(buf -> {
            MinecraftTypes.writeResourceLocation(buf, unknown);
            buf.writeZero(1048577);
        }, org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundCustomPayloadPacket::new);
    }

    private static void rejects(Consumer<ByteBuf> writer, Function<ByteBuf, ?> reader) {
        ByteBuf buf = Unpooled.buffer();
        try {
            writer.accept(buf);
            assertThrows(IllegalArgumentException.class, () -> reader.apply(buf));
        } finally {
            buf.release();
        }
    }

    private static void rejectsWrite(Consumer<ByteBuf> writer) {
        ByteBuf buf = Unpooled.buffer();
        try {
            assertThrows(IllegalArgumentException.class, () -> writer.accept(buf));
        } finally {
            buf.release();
        }
    }

    private static <T extends MinecraftPacket> void assertPacketRoundTrip(T packet, Function<ByteBuf, T> reader) {
        ByteBuf buf = Unpooled.buffer();
        try {
            packet.serialize(buf);
            assertEquals(packet, reader.apply(buf));
            assertFalse(buf.isReadable());
        } finally {
            buf.release();
        }
    }
}
