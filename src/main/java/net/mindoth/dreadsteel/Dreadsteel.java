package net.mindoth.dreadsteel;

import net.mindoth.dreadsteel.config.DreadsteelCommonConfig;
import net.mindoth.dreadsteel.item.armor.DreadsteelArmor;
import net.mindoth.dreadsteel.message.MessageSwingArm;
import net.mindoth.dreadsteel.registries.DreadsteelEntities;
import net.mindoth.dreadsteel.registries.DreadsteelItems;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

@Mod(Dreadsteel.MOD_ID)
public class Dreadsteel {

    //public static SidedProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static final String MOD_ID = "dreadsteel";
    private static final String PROTOCOL_VERSION = Integer.toString(1);

    public Dreadsteel(IEventBus modEventBus, ModContainer container) {
        DreadsteelItems.register(modEventBus);
        addRegistries(modEventBus);
        modEventBus.addListener(this::setup);

        container.registerConfig(ModConfig.Type.COMMON, DreadsteelCommonConfig.SPEC, "dreadsteel-common.toml");
    }

    private void addRegistries(final IEventBus modEventBus) {
        DreadsteelEntities.ENTITIES.register(modEventBus);
        DreadsteelArmor.MaterialDreadsteel.init(modEventBus);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if ( event.getTabKey() == CreativeModeTabs.COMBAT ) {
            event.accept(DreadsteelItems.DREADSTEEL_HELMET.get());
            event.accept(DreadsteelItems.DREADSTEEL_CHESTPLATE.get());
            event.accept(DreadsteelItems.DREADSTEEL_LEGGINGS.get());
            event.accept(DreadsteelItems.DREADSTEEL_BOOTS.get());
            event.accept(DreadsteelItems.DREADSTEEL_SCYTHE.get());
            event.accept(DreadsteelItems.DREADSTEEL_SHIELD.get());
        }
        if ( event.getTabKey() == CreativeModeTabs.INGREDIENTS ) {
            event.accept(DreadsteelItems.DREADSTEEL_INGOT.get());
            event.accept(DreadsteelItems.DEFAULT_KIT.get());
            event.accept(DreadsteelItems.WHITE_KIT.get());
            event.accept(DreadsteelItems.BLACK_KIT.get());
            event.accept(DreadsteelItems.BRONZE_KIT.get());
        }
    }

    public static <MSG extends CustomPacketPayload> void sendMSGToServer(MSG message) {
        PacketDistributor.sendToServer(message);
    }

    private void setup(final RegisterPayloadHandlersEvent event) {
        event.registrar(PROTOCOL_VERSION).playToServer(MessageSwingArm.TYPE, MessageSwingArm.STREAM_CODEC, MessageSwingArm::handle);
    }
}
