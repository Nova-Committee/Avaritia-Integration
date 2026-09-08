package committee.nova.mods.avaritia_integration.integrations.create;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.create.content.BoilerHeaters;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_basin.ExtremeBasinBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_burner.ExtremeBlazeBurnerBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_crusher.ExtremeCrushingWheelBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_crusher.ExtremeCrushingWheelControllerBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_depot.ExtremeDepotBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlocks;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationDisplaySources;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationItems;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationPartialModels;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationRecipeTypes;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationSpriteShifts;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationMountedStorageTypes;

import committee.nova.mods.avaritia_integration.module.Module;

import net.createmod.catnip.lang.FontHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public final class CreateModule implements Module {

    public static final String MOD_ID = "create";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(AvaritiaIntegration.MOD_ID)
            .setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item))));

    @Override
    public void init(IEventBus registryBus) {
        REGISTRATE.registerEventListeners(registryBus);
        CreateIntegrationItems.REGISTRY.register(registryBus);
        CreateIntegrationDisplaySources.register();
        CreateIntegrationMountedStorageTypes.register();
        CreateIntegrationBlocks.register();
        CreateIntegrationBlockEntityTypes.register();
        CreateIntegrationRecipeTypes.register(registryBus);
    }

    @Override
    public void process() {
        BoilerHeaters.registerDefaults();
        MountedItemStorageType.REGISTRY.register(CreateIntegrationBlocks.EXTREME_DEPOT.get(),
                CreateIntegrationMountedStorageTypes.EXTREME_DEPOT.get());
    }

    @Override
    public void initClient() {
        CreateIntegrationPartialModels.init();
        CreateIntegrationSpriteShifts.init();
    }

    @Override
    public void registerEvent(IEventBus modBus, IEventBus gameBus) {
        modBus.addListener(CreateModule::registerCapabilities);
        gameBus.addListener(ExtremeCrushingWheelBlockEntity::handleCrushedMobDrops);
        gameBus.addListener(CreateModule::addCreateTabItems);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        ExtremeBasinBlockEntity.registerCapabilities(event);
        ExtremeBlazeBurnerBlockEntity.registerCapabilities(event);
        ExtremeDepotBlockEntity.registerCapabilities(event);
        ExtremeCrushingWheelControllerBlockEntity.registerCapabilities(event);
    }

    private static void addCreateTabItems(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey())) {
            return;
        }
        event.accept(AllItems.CHROMATIC_COMPOUND);
        event.accept(AllItems.SHADOW_STEEL);
        event.accept(AllItems.REFINED_RADIANCE);
    }


    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters,
            CreativeModeTab.Output output) {
        output.accept(CreateIntegrationItems.CREATIVE_MECHANISM.get());
        output.accept(CreateIntegrationItems.CREATIVE_COMPOUND.get());
        output.accept(CreateIntegrationItems.STAR_CAKE.get());
        output.accept(CreateIntegrationItems.STAR_CAKE_BASE.get());
        output.accept(CreateIntegrationItems.IGNIS_CAKE.get());
        output.accept(CreateIntegrationItems.IGNIS_CAKE_BASE.get());
        output.accept(CreateIntegrationBlocks.EXTREME_BLAZE_BURNER.asItem());
        output.accept(CreateIntegrationBlocks.CRYSTAL_MATRIX_CASING.asItem());
        output.accept(CreateIntegrationBlocks.NEUTRON_MECHANICAL_PRESS.asItem());
        output.accept(CreateIntegrationBlocks.EXTREME_BASIN.asItem());
        output.accept(CreateIntegrationBlocks.MATRIX_MECHANICAL_MIXER.asItem());
        output.accept(CreateIntegrationBlocks.EXTREME_DEPOT.asItem());
        output.accept(CreateIntegrationBlocks.EXTREME_ENCASED_FAN.asItem());
        output.accept(CreateIntegrationBlocks.EXTREME_CRUSHING_WHEEL.asItem());
    }
}
