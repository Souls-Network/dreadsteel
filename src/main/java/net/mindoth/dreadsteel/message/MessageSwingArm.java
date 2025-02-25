package net.mindoth.dreadsteel.message;

import net.mindoth.dreadsteel.Dreadsteel;
import net.mindoth.dreadsteel.item.weapon.DreadsteelScythe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public enum MessageSwingArm implements CustomPacketPayload {
    INSTANCE;

    public static final Type<MessageSwingArm> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Dreadsteel.MOD_ID, "swing_arm"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSwingArm> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        Player player = context.player();
        DreadsteelScythe.onLeftClick(player, player.getItemInHand(InteractionHand.MAIN_HAND));
    }
}
