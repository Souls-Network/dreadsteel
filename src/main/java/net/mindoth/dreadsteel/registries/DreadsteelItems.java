package net.mindoth.dreadsteel.registries;

import net.mindoth.dreadsteel.Dreadsteel;
import net.mindoth.dreadsteel.item.CosmeticKit;
import net.mindoth.dreadsteel.item.DreadsteelIngot;
import net.mindoth.dreadsteel.item.armor.DreadsteelArmor;
import net.mindoth.dreadsteel.item.weapon.DreadsteelScythe;
import net.mindoth.dreadsteel.item.weapon.DreadsteelShield;
import net.mindoth.dreadsteel.item.weapon.DreadsteelTier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DreadsteelItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Dreadsteel.MOD_ID);

    public static final DeferredHolder<Item, Item> SCYTHE_PROJECTILE_DEFAULT = ITEMS.register("dreadsteel_scythe_projectile_default",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> SCYTHE_PROJECTILE_BLACK = ITEMS.register("dreadsteel_scythe_projectile_black",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> SCYTHE_PROJECTILE_BRONZE = ITEMS.register("dreadsteel_scythe_projectile_bronze",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> SCYTHE_PROJECTILE_WHITE = ITEMS.register("dreadsteel_scythe_projectile_white",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, CosmeticKit> DEFAULT_KIT = ITEMS.register("kit_default",
            () -> new CosmeticKit(new Item.Properties()));
    public static final DeferredHolder<Item, CosmeticKit> WHITE_KIT = ITEMS.register("kit_white",
            () -> new CosmeticKit(new Item.Properties()));
    public static final DeferredHolder<Item, CosmeticKit> BLACK_KIT = ITEMS.register("kit_black",
            () -> new CosmeticKit(new Item.Properties()));
    public static final DeferredHolder<Item, CosmeticKit> BRONZE_KIT = ITEMS.register("kit_bronze",
            () -> new CosmeticKit(new Item.Properties()));

    public static final DeferredHolder<Item, DreadsteelIngot> DREADSTEEL_INGOT = ITEMS.register("dreadsteel_ingot",
            () -> new DreadsteelIngot(itemBuilder()));
    public static final DeferredHolder<Item, DreadsteelArmor> DREADSTEEL_HELMET = ITEMS.register("dreadsteel_helmet",
            () -> new DreadsteelArmor(DreadsteelArmor.MaterialDreadsteel.DREADSTEEL, ArmorItem.Type.HELMET, itemBuilder()));
    public static final DeferredHolder<Item, DreadsteelArmor> DREADSTEEL_CHESTPLATE = ITEMS.register("dreadsteel_chestplate",
            () -> new DreadsteelArmor(DreadsteelArmor.MaterialDreadsteel.DREADSTEEL, ArmorItem.Type.CHESTPLATE, itemBuilder()));
    public static final DeferredHolder<Item, DreadsteelArmor> DREADSTEEL_LEGGINGS = ITEMS.register("dreadsteel_leggings",
            () -> new DreadsteelArmor(DreadsteelArmor.MaterialDreadsteel.DREADSTEEL, ArmorItem.Type.LEGGINGS, itemBuilder()));
    public static final DeferredHolder<Item, DreadsteelArmor> DREADSTEEL_BOOTS = ITEMS.register("dreadsteel_boots",
            () -> new DreadsteelArmor(DreadsteelArmor.MaterialDreadsteel.DREADSTEEL, ArmorItem.Type.BOOTS, itemBuilder()));

    public static final DeferredHolder<Item, DreadsteelScythe> DREADSTEEL_SCYTHE = ITEMS.register("dreadsteel_scythe",
            () -> new DreadsteelScythe(DreadsteelTier.DREADSTEEL, itemBuilder().attributes(SwordItem.createAttributes(DreadsteelTier.DREADSTEEL, 0, -2.4f))));
    public static final DeferredHolder<Item, DreadsteelShield> DREADSTEEL_SHIELD = ITEMS.register("dreadsteel_shield", DreadsteelShield::new);

    private static Item.Properties itemBuilder() {
        return new Item.Properties().fireResistant();
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
