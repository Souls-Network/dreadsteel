package net.mindoth.dreadsteel.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class DreadsteelIngot extends Item {
    public DreadsteelIngot(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.dreadsteel.dreadsteel_ingot"));
        super.appendHoverText(stack, context, tooltip, flagIn);
    }
}
