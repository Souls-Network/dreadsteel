package net.mindoth.dreadsteel.item.weapon;

import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.AtomicDouble;
import net.mindoth.dreadsteel.Dreadsteel;
import net.mindoth.dreadsteel.config.DreadsteelCommonConfig;
import net.mindoth.dreadsteel.entity.EntityScytheProjectileBlack;
import net.mindoth.dreadsteel.entity.EntityScytheProjectileBronze;
import net.mindoth.dreadsteel.entity.EntityScytheProjectileWhite;
import net.mindoth.dreadsteel.message.MessageSwingArm;
import net.mindoth.dreadsteel.registries.DreadsteelEntities;
import net.mindoth.dreadsteel.entity.EntityScytheProjectileDefault;
import net.mindoth.dreadsteel.registries.DreadsteelItems;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

@EventBusSubscriber(modid = Dreadsteel.MOD_ID)
public class DreadsteelScythe extends SwordItem {
    static final Map<Item, Map<Holder<Attribute>, AttributeModifier>> WEAPON_ATTRIBUTE_MODIFIERS = new HashMap<>();
    private static final ResourceLocation ATTACK_DAMAGE_MODIFIER_NAME = ResourceLocation.fromNamespaceAndPath(Dreadsteel.MOD_ID, "dreadsteel_attack");
    private static final ResourceLocation ATTACK_SPEED_MODIFIER_NAME = ResourceLocation.fromNamespaceAndPath(Dreadsteel.MOD_ID, "dreadsteel_speed");

    public DreadsteelScythe(DreadsteelTier p_i48460_1_, Properties p_i48460_4_) {
        super(p_i48460_1_, p_i48460_4_);
    }

    @SubscribeEvent
    public static void dreadsteelScytheAttackAttributeEvent(ItemAttributeModifierEvent event) {
        Item item = event.getItemStack().getItem();
        if ( item == DreadsteelItems.DREADSTEEL_SCYTHE.get() ) {
            findAndRemoveVanillaModifier(event, Attributes.ATTACK_DAMAGE, Item.BASE_ATTACK_DAMAGE_ID);
            event.addModifier(Attributes.ATTACK_DAMAGE, getAttackDamage(item, DreadsteelCommonConfig.SCYTHE_DAMAGE.get()), EquipmentSlotGroup.MAINHAND);
            findAndRemoveVanillaModifier(event, Attributes.ATTACK_SPEED, Item.BASE_ATTACK_DAMAGE_ID);
            event.addModifier(Attributes.ATTACK_SPEED, getAttackSpeed(item, (float)(DreadsteelCommonConfig.SCYTHE_SPEED.get() - 4)), EquipmentSlotGroup.MAINHAND);
        }
    }

    private static void findAndRemoveVanillaModifier(ItemAttributeModifierEvent event, Holder<Attribute> attribute, ResourceLocation baseUUID) {
        event.removeModifier(attribute, baseUUID);
//        event.getDefaultModifiers().modifiers().stream()
//                .filter(modifier -> modifier.matches(attribute, baseUUID)) // we don't use "equals" because vanilla enforces a direct memory address comparison
//                .findAny()
//                .ifPresent(modifier -> event.removeModifier().removeModifier(attribute, modifier));
    }

    private static AttributeModifier getAttackDamage(Item item, double defaultValue) {
        return getModifier(item, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER_NAME, defaultValue, AttributeModifier.Operation.ADD_VALUE);
    }

    private static AttributeModifier getAttackSpeed(Item item, double defaultValue) {
        return getModifier(item, Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER_NAME, defaultValue, AttributeModifier.Operation.ADD_VALUE);
    }

    private static AttributeModifier getModifier(Item item, Holder<Attribute> attribute, ResourceLocation modifierName, double defaultValue, AttributeModifier.Operation operation) {
        return WEAPON_ATTRIBUTE_MODIFIERS
                .computeIfAbsent(item, k -> new HashMap<>())
                .computeIfAbsent(attribute, k ->
                        new AttributeModifier(modifierName, defaultValue, operation)
                );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.dreadsteel.dreadsteel_scythe"));
        super.appendHoverText(stack, context, tooltip, flagIn);
    }

    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        onLeftClick(event.getEntity(), event.getItemStack());
        if (event.getLevel().isClientSide) {
            Dreadsteel.sendMSGToServer(MessageSwingArm.INSTANCE);
        }
    }

    public static void onLeftClick(final Player playerEntity, final ItemStack stack) {
        if ( stack.getItem() == DreadsteelItems.DREADSTEEL_SCYTHE.get() ) {
            DreadsteelScythe.spawnProjectile(stack, playerEntity);
        }
    }

    public static void spawnProjectile(ItemStack stack, Player player) {
        if ( player.swingTime > 0.5F ) {
            return;
        }
        if ( player.getItemInHand(InteractionHand.MAIN_HAND) == stack ) {
            var damageAccumulator = new ModifierDamageAccumulater(Attributes.ATTACK_DAMAGE);

            stack.getAttributeModifiers().forEach(EquipmentSlot.MAINHAND, damageAccumulator);

            shootScytheProjectile(player, damageAccumulator, stack);

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 0.75f, 0.75f);
        }
    }

    public static EntityType<? extends AbstractArrow> getScythe(ItemStack stack) {
        var customModelData = stack.has(DataComponents.CUSTOM_MODEL_DATA) ? stack.get(DataComponents.CUSTOM_MODEL_DATA).value() : 0;

        return switch (customModelData) {
            case 1 -> DreadsteelEntities.SCYTHE_PROJECTILE_WHITE.get();
            case 2 -> DreadsteelEntities.SCYTHE_PROJECTILE_BLACK.get();
            case 3 -> DreadsteelEntities.SCYTHE_PROJECTILE_BRONZE.get();
            default -> DreadsteelEntities.SCYTHE_PROJECTILE_DEFAULT.get();
        };
    }

    public static void shootScytheProjectile(Player player, ModifierDamageAccumulater accumulater, ItemStack stack) {
        EntityScytheProjectileDefault shot = new EntityScytheProjectileDefault(getScythe(stack), player.level(), player, accumulater.totalDmg);
        //Vec3 vector3d = player.getLookAngle();
        //Vector3f vector3f = new Vector3f(vector3d);
        shot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.0F, 0.0F);
        player.level().addFreshEntity(shot);
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    public static class ModifierDamageAccumulater implements BiConsumer<Holder<Attribute>, AttributeModifier> {
        private final Holder<Attribute> attribute;
        private double totalDmg = 0.0;

        public ModifierDamageAccumulater(Holder<Attribute> attribute) {
            this.attribute = attribute;
        }

        @Override
        public void accept(Holder<Attribute> attributeHolder, AttributeModifier modifier) {
            if(attributeHolder.is(attribute)) totalDmg += modifier.amount();
        }

        public double getTotalDmg() {
            return totalDmg;
        }
    }

}
