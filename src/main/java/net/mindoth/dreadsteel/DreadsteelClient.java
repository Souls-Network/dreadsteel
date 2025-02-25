package net.mindoth.dreadsteel;

import net.mindoth.dreadsteel.client.ModelHandler;
import net.mindoth.dreadsteel.client.models.armor.DreadsteelModel;
import net.mindoth.dreadsteel.entity.renderer.RenderScytheProjectileDefault;
import net.mindoth.dreadsteel.entity.renderer.RenderScytheProjectileBlack;
import net.mindoth.dreadsteel.entity.renderer.RenderScytheProjectileBronze;
import net.mindoth.dreadsteel.entity.renderer.RenderScytheProjectileWhite;
import net.mindoth.dreadsteel.registries.DreadsteelEntities;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = Dreadsteel.MOD_ID, dist = Dist.CLIENT)
public class DreadsteelClient {
    public DreadsteelClient(IEventBus modBus) {
        modBus.addListener(DreadsteelClient::clientSetup);

        modBus.addListener(DreadsteelClient::registerLayerDefinitions);
    }
    private static void clientSetup(final FMLClientSetupEvent event) {
        ModelHandler.addCustomItemProperties();
        EntityRenderers.register(DreadsteelEntities.SCYTHE_PROJECTILE_DEFAULT.get(), RenderScytheProjectileDefault::new);
        EntityRenderers.register(DreadsteelEntities.SCYTHE_PROJECTILE_BLACK.get(), RenderScytheProjectileBlack::new);
        EntityRenderers.register(DreadsteelEntities.SCYTHE_PROJECTILE_BRONZE.get(), RenderScytheProjectileBronze::new);
        EntityRenderers.register(DreadsteelEntities.SCYTHE_PROJECTILE_WHITE.get(), RenderScytheProjectileWhite::new);
    }

    public static final ModelLayerLocation DREADSTEEL_ARMOR = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Dreadsteel.MOD_ID, "main"), "dreadsteel_armor");

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DREADSTEEL_ARMOR, DreadsteelModel::createBodyLayer);
    }
}
