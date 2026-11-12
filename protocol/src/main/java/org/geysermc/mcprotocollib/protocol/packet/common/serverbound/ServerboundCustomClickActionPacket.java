package org.geysermc.mcprotocollib.protocol.packet.common.serverbound;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundCustomClickActionPacket implements MinecraftPacket {
    private final Key id;
    private final @Nullable Object payload;

    public ServerboundCustomClickActionPacket(Key id, @Nullable NbtMap payload) {
        this(id, (Object) payload);
    }

    public ServerboundCustomClickActionPacket(ByteBuf in) {
        this.id = MinecraftTypes.readResourceLocation(in);

        this.payload = MinecraftTypes.readLengthPrefixed(in, 65536, buf -> {
            Object tag;
            try {
                ByteBufInputStream input = new ByteBufInputStream(buf);

                int typeId = input.readUnsignedByte();
                if (typeId == 0) {
                    tag = null;
                } else {
                    NbtType<?> type = NbtType.byId(typeId);

                    tag = new NBTInputStream(input, 32768L).readValue(type, 16);
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }

            return tag;
        });
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeResourceLocation(out, this.id);

        MinecraftTypes.writeLengthPrefixed(out, 65536, this.payload, (buf, data) -> {
            try {
                ByteBufOutputStream output = new ByteBufOutputStream(buf);

                if (data == null) {
                    output.writeByte(0);
                    return;
                }

                NbtType<?> type = NbtType.byClass(data.getClass());
                output.writeByte(type.getId());

                new NBTOutputStream(output).writeValue(data, 16);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        });
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
