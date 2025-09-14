package committee.nova.mods.avaritia_integration;

import com.mojang.logging.LogUtils;
import committee.nova.mods.avaritia_integration.init.registry.ModBlockEntities;
import committee.nova.mods.avaritia_integration.init.registry.ModBlocks;
import committee.nova.mods.avaritia_integration.init.registry.ModCreativeModeTabs;
import committee.nova.mods.avaritia_integration.init.registry.ModItems;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.lib.ResourceLocationHelper;
import vazkii.botania.forge.CapabilityUtil;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(AvaritiaIntegration.MOD_ID)
public class AvaritiaIntegration
{

    public static final String MOD_ID = "avaritia_integration";

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, MOD_ID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public AvaritiaIntegration()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModCreativeModeTabs.CREATIVE_TABS.register(modEventBus);

        DistExecutor.runWhenOn(Dist.CLIENT,()->()-> FMLJavaModLoadingContext.get().getModEventBus().addListener(this::Baked));
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addGenericListener(BlockEntity.class, this::attachBeCaps);
        event.enqueueWork(() ->{
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.asgard_dandelion.getId(),ModBlocks.potted_asgard_dandelion);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.soarleander.getId(),ModBlocks.potted_soarleander);
        });
    }
    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @OnlyIn(Dist.CLIENT)
    private void Baked(final ModelEvent.ModifyBakingResult event) {
        ModelResourceLocation loc2 = new ModelResourceLocation(
                Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ModItems.STREDGEUNIVERSE.get())), "inventory");
        BladeModel model2 = new BladeModel(event.getModels().get(loc2), event.getModelBakery());
        event.getModels().put(loc2, model2);
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof Chicken) {
            if (event.getSource() == event.getEntity().level().damageSources().fellOutOfWorld()) {
                event.getDrops().clear();
            }
        }
    }
    private void attachBeCaps(AttachCapabilitiesEvent<BlockEntity> e) {
        BlockEntity be = e.getObject();

        if (be.getType() == ModBlockEntities.INFINITY_MANA_POOL.get())  {
            e.addCapability(ResourceLocationHelper.prefix("mana_receiver"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver)be));
            e.addCapability(ResourceLocationHelper.prefix("wandable"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.WANDABLE, (Wandable)be));
        }

    }
}
