package net.mindoth.dreadsteel.item.armor;

import net.mindoth.dreadsteel.Dreadsteel;
import net.mindoth.dreadsteel.DreadsteelClient;
import net.mindoth.dreadsteel.client.models.armor.DreadsteelModel;
import net.mindoth.dreadsteel.registries.DreadsteelItems;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class DreadsteelArmor extends ArmorItem {

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.dreadsteel.dreadsteel_setbonus"));
        super.appendHoverText(stack, context, tooltip, flagIn);
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    @OnlyIn(Dist.CLIENT) private final LazyLoadedValue<HumanoidModel<?>> model = new LazyLoadedValue<>(() -> this.provideArmorModelForSlot(type));

    public DreadsteelArmor(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties.fireResistant());
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        int tag = stack.has(DataComponents.CUSTOM_MODEL_DATA) ? stack.get(DataComponents.CUSTOM_MODEL_DATA).value() : 0;

        return ResourceLocation.parse(switch (tag) {
            case 1 -> "dreadsteel:textures/item/dreadsteel_armor_model_white.png";
            case 2 -> "dreadsteel:textures/item/dreadsteel_armor_model_black.png";
            case 3 -> "dreadsteel:textures/item/dreadsteel_armor_model_bronze.png";
            default -> "dreadsteel:textures/item/dreadsteel_armor_model_default.png";
        });
    }

    @OnlyIn(Dist.CLIENT)
    public HumanoidModel<?> provideArmorModelForSlot(Type type) {
        return new DreadsteelModel(Minecraft.getInstance().getEntityModels().bakeLayer(DreadsteelClient.DREADSTEEL_ARMOR), type);
    }



    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                return model.get();
            }
        });
    }

    public static class MaterialDreadsteel {
        private static final DeferredRegister<ArmorMaterial> REGISTER = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, Dreadsteel.MOD_ID);

        public static DeferredHolder<ArmorMaterial, ArmorMaterial> DREADSTEEL = REGISTER.register("dreadsteel", location -> new ArmorMaterial(
                Util.make(new EnumMap<>(Type.class), (map) -> {
                    map.put(Type.BOOTS, 0);
                    map.put(Type.LEGGINGS, 0);
                    map.put(Type.CHESTPLATE, 0);
                    map.put(Type.HELMET, 0);
                }),
                25,
                SoundEvents.ARMOR_EQUIP_LEATHER,
                () -> Ingredient.of(DreadsteelItems.DREADSTEEL_INGOT.get()),
                List.of(new ArmorMaterial.Layer(location)),
                0.0f,
                0.0f));

        public static void init(IEventBus bus) {
            REGISTER.register(bus);
        }
    }
}
