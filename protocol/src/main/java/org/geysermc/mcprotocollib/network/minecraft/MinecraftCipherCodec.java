package org.geysermc.mcprotocollib.network.minecraft;

import com.velocitypowered.natives.encryption.VelocityCipher;
import com.velocitypowered.natives.util.MoreByteBufUtils;
import com.velocitypowered.natives.util.Natives;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.*;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Encrypt Minecraft protocol packets using {@link VelocityCipher}.
 */
@RequiredArgsConstructor
public class MinecraftCipherCodec extends MessageToMessageCodec<ByteBuf, ByteBuf> {
    private final VelocityCipher encoder;
    private final VelocityCipher decoder;

    public MinecraftCipherCodec(SecretKey keyEncode, SecretKey keyDecode) {
        try {
            this.encoder = Natives.cipher.get().forEncryption(keyEncode);
            this.decoder = Natives.cipher.get().forDecryption(keyDecode);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Unable to initialize cipher", e);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        var compatible = MoreByteBufUtils.ensureCompatible(ctx.alloc(), encoder, msg);
        try {
            encoder.process(compatible);
            out.add(compatible.retain());
        } finally {
            compatible.release();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        if (!ctx.channel().isActive()) {
            return;
        }

        var compatible = MoreByteBufUtils.ensureCompatible(ctx.alloc(), decoder, msg);
        try {
            decoder.process(compatible);
            out.add(compatible.retain());
        } finally {
            compatible.release();
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        encoder.close();
        decoder.close();
    }
}
