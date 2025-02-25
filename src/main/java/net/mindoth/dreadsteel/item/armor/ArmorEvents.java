package net.mindoth.dreadsteel.item.armor;

import net.mindoth.dreadsteel.Dreadsteel;
import net.mindoth.dreadsteel.config.DreadsteelCommonConfig;
import net.mindoth.dreadsteel.item.CosmeticKit;
import net.mindoth.dreadsteel.registries.DreadsteelItems;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = Dreadsteel.MOD_ID)
public class ArmorEvents {

    @SubscribeEvent
    public static void noHat(final RenderPlayerEvent.Post event) {
        Player player = event.getEntity();
        if ( player.getItemBySlot(EquipmentSlot.HEAD).getItem() == DreadsteelItems.DREADSTEEL_HELMET.get() ) {
            event.getRenderer().getModel().hat.visible = false;
        }
    }

    @SubscribeEvent
    public static void dreadsteelSetDefence(final LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if ( entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == DreadsteelItems.DREADSTEEL_HELMET.get() &&
                entity.getItemBySlot(EquipmentSlot.CHEST).getItem() == DreadsteelItems.DREADSTEEL_CHESTPLATE.get() &&
                entity.getItemBySlot(EquipmentSlot.LEGS).getItem() == DreadsteelItems.DREADSTEEL_LEGGINGS.get() &&
                entity.getItemBySlot(EquipmentSlot.FEET).getItem() == DreadsteelItems.DREADSTEEL_BOOTS.get() ) {
            if ( event.getSource().is(DamageTypes.LIGHTNING_BOLT) || event.getSource().is(DamageTypes.IN_FIRE)
                    || event.getSource().is(DamageTypes.ON_FIRE) || event.getSource().is(DamageTypes.CACTUS) ) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void dreadsteelAttributeEvent(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();

        EquipmentSlotGroup equipmentSlot;

        if ( item == DreadsteelItems.DREADSTEEL_HELMET.get()) {
            equipmentSlot = EquipmentSlotGroup.HEAD;
        } else if(item == DreadsteelItems.DREADSTEEL_CHESTPLATE.get()) {
            equipmentSlot = EquipmentSlotGroup.CHEST;
        } else if (item == DreadsteelItems.DREADSTEEL_LEGGINGS.get()) {
            equipmentSlot = EquipmentSlotGroup.LEGS;
        } else if(item == DreadsteelItems.DREADSTEEL_BOOTS.get()) {
            equipmentSlot = EquipmentSlotGroup.FEET;
        } else return;

        event.addModifier(Attributes.ARMOR, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Dreadsteel.MOD_ID, "dreadsteel_armor"), DreadsteelCommonConfig.HELMET_ARMOR.get(), AttributeModifier.Operation.ADD_VALUE), equipmentSlot);
        event.addModifier(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Dreadsteel.MOD_ID, "dreadsteel_toughness"), DreadsteelCommonConfig.ARMOR_TOUGHNESS.get(), AttributeModifier.Operation.ADD_VALUE), equipmentSlot);
        event.addModifier(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Dreadsteel.MOD_ID, "dreadsteel_knockback_resistance"), DreadsteelCommonConfig.ARMOR_KNOCKBACK_RESISTANCE.get(), AttributeModifier.Operation.ADD_VALUE), equipmentSlot);
    }



    @SubscribeEvent
    public static void onAnvilDyeEvent(final AnvilUpdateEvent event) {
        ItemStack leftStack = event.getLeft();
        Item rightItem = event.getRight().getItem();
        if ( isDyeableDreadsteelItem(leftStack.getItem()) ) {
            ItemStack result = leftStack.copy();

            if ( rightItem instanceof CosmeticKit ) {
                if ( rightItem == DreadsteelItems.DEFAULT_KIT.get() ) result.remove(DataComponents.CUSTOM_MODEL_DATA);
                else if ( rightItem == DreadsteelItems.WHITE_KIT.get() ) result.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(1));
                else if ( rightItem == DreadsteelItems.BLACK_KIT.get() ) result.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(2));
                else if ( rightItem == DreadsteelItems.BRONZE_KIT.get() ) result.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(3));
                handleCustomAnvil(event, leftStack, result);
            }
        }
    }

    private static void handleCustomAnvil(AnvilUpdateEvent event, ItemStack leftStack, ItemStack result) {
        int xpCost = 1;
        if ( event.getName() != null && !event.getName().isBlank()) {
            if ( !event.getName().equals(leftStack.getHoverName().getString()) ) {
                result.set(DataComponents.CUSTOM_NAME, Component.literal(event.getName()));
                xpCost += 1;
            }
        }
        else if ( leftStack.has(DataComponents.CUSTOM_NAME)) result.remove(DataComponents.CUSTOM_NAME);
        event.setMaterialCost(1);
        event.setOutput(result);
        event.setCost(xpCost);
    }

    private static boolean isDyeableDreadsteelItem(Item item) {
        return (item instanceof ArmorItem armorItem && armorItem.getMaterial().equals(DreadsteelArmor.MaterialDreadsteel.DREADSTEEL))
                || item == DreadsteelItems.DREADSTEEL_SCYTHE.get() || item == DreadsteelItems.DREADSTEEL_SHIELD.get();
    }
}
