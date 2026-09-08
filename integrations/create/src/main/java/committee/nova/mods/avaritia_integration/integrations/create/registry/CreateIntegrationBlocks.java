package committee.nova.mods.avaritia_integration.integrations.create.registry;

import com.simibubi.create.AllTags;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.api.behaviour.interaction.ConductorBlockInteractionBehavior;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.logistics.depot.MountedDepotInteractionBehaviour;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.content.processing.basin.BasinMovementBehaviour;
import com.simibubi.create.content.processing.burner.BlazeBurnerMovementBehaviour;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.util.entry.BlockEntry;
import committee.nova.mods.avaritia_integration.integrations.create.CreateModule;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_basin.ExtremeBasinBlock;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_basin.ExtremeBasinGenerator;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_burner.ExtremeBlazeBurnerBlock;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_crusher.ExtremeCrushingWheelBlock;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_crusher.ExtremeCrushingWheelControllerBlock;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_depot.ExtremeDepotBlock;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_fan.ExtremeEncasedFanBlock;
import committee.nova.mods.avaritia_integration.integrations.create.content.matrix_mixer.MatrixMechanicalMixerBlock;

import committee.nova.mods.avaritia_integration.integrations.create.content.neutron_press.NeutronMechanicalPressBlock;
import committee.nova.mods.avaritia_integration.integrations.create.foundation.data.CreateIntegrationAssetLookup;
import committee.nova.mods.avaritia_integration.integrations.create.foundation.data.CreateIntegrationBlockStateGen;
import committee.nova.mods.avaritia_integration.integrations.create.foundation.data.CreateIntegrationBuilderTransformers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class CreateIntegrationBlocks {
    private static final CreateRegistrate REGISTRATE = CreateModule.REGISTRATE;
    public static final BlockEntry<ExtremeBlazeBurnerBlock> EXTREME_BLAZE_BURNER;
    public static final BlockEntry<CasingBlock> CRYSTAL_MATRIX_CASING;
    public static final BlockEntry<NeutronMechanicalPressBlock> NEUTRON_MECHANICAL_PRESS;
    public static final BlockEntry<ExtremeBasinBlock> EXTREME_BASIN;
    public static final BlockEntry<MatrixMechanicalMixerBlock> MATRIX_MECHANICAL_MIXER;
    public static final BlockEntry<ExtremeDepotBlock> EXTREME_DEPOT;
    public static final BlockEntry<ExtremeEncasedFanBlock> EXTREME_ENCASED_FAN;
    public static final BlockEntry<ExtremeCrushingWheelBlock> EXTREME_CRUSHING_WHEEL;
    public static final BlockEntry<ExtremeCrushingWheelControllerBlock> EXTREME_CRUSHING_WHEEL_CONTROLLER;

    public static void register() {
    }

    static {
        EXTREME_BLAZE_BURNER = REGISTRATE.block("extreme_blaze_burner", ExtremeBlazeBurnerBlock::new)
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.mapColor(MapColor.COLOR_GRAY).lightLevel(ExtremeBlazeBurnerBlock::getLight))
                .transform(TagGen.pickaxeOnly())
                
                .tag(AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_BLASTING.tag, AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SMOKING.tag,
                        AllTags.AllBlockTags.FAN_TRANSPARENT.tag, AllTags.AllBlockTags.PASSIVE_BOILER_HEATERS.tag)
                .blockstate(ExtremeBlazeBurnerBlock::blockStateDataGen)
                .onRegister(MovementBehaviour.movementBehaviour(new BlazeBurnerMovementBehaviour()))
                .onRegister(MovingInteractionBehaviour.interactionBehaviour(new ConductorBlockInteractionBehavior.BlazeBurner()))
                .item()
                .model(AssetLookup.customBlockItemModel("create", "extreme_blaze_burner", "block_with_blaze"))
                .build()
                .register();

        CRYSTAL_MATRIX_CASING = REGISTRATE.block("crystal_matrix_casing", CasingBlock::new)
                .properties(p -> p.mapColor(MapColor.COLOR_BLACK))
                .transform(CreateIntegrationBuilderTransformers.casing(() -> CreateIntegrationSpriteShifts.CRYSTAL_MATRIX_CASING))
                .register();

        NEUTRON_MECHANICAL_PRESS = REGISTRATE.block("neutron_mechanical_press", NeutronMechanicalPressBlock::new)
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.noOcclusion().mapColor(MapColor.COLOR_BLACK))
                .transform(TagGen.axeOrPickaxe())
                .blockstate(CreateIntegrationBlockStateGen.horizontalBlockProvider(true))
                .onRegister(CreateIntegrationStress.setImpact(16.0))
                .item(AssemblyOperatorBlockItem::new)
                .transform(ModelGen.customItemModel("create", "_", "block"))
                .register();

        EXTREME_BASIN = REGISTRATE.block("extreme_basin", ExtremeBasinBlock::new)
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                        .sound(SoundType.NETHERITE_BLOCK))
                .transform(TagGen.pickaxeOnly())
                .blockstate(new ExtremeBasinGenerator()::generate)
                
                .onRegister(MovementBehaviour.movementBehaviour(new BasinMovementBehaviour()))
                .item()
                .transform(ModelGen.customItemModel("create", "_", "block"))
                .register();

        MATRIX_MECHANICAL_MIXER = REGISTRATE.block("matrix_mechanical_mixer", MatrixMechanicalMixerBlock::new)
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.noOcclusion().mapColor(MapColor.STONE))
                .transform(TagGen.axeOrPickaxe())
                .blockstate((c, p) -> p.simpleBlock(c.getEntry(), CreateIntegrationAssetLookup.partialBaseModel(c, p)))
                
                .onRegister(CreateIntegrationStress.setImpact(16.0))
                .item(com.simibubi.create.content.processing.AssemblyOperatorBlockItem::new)
                .transform(ModelGen.customItemModel("create", "_", "block"))
                .register();

        EXTREME_DEPOT = REGISTRATE.block("extreme_depot", ExtremeDepotBlock::new)
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
                .transform(TagGen.axeOrPickaxe())
                .blockstate((c, p) -> p.simpleBlock(c.getEntry(), CreateIntegrationAssetLookup.partialBaseModel(c, p)))
                .onRegister(MovingInteractionBehaviour.interactionBehaviour(new MountedDepotInteractionBehaviour()))
                .item()
                .transform(ModelGen.customItemModel("create", "_", "block"))
                .register();

        EXTREME_ENCASED_FAN = REGISTRATE.block("extreme_encased_fan", ExtremeEncasedFanBlock::new)
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.mapColor(MapColor.PODZOL))
                .blockstate(CreateIntegrationBlockStateGen.directionalBlockProvider(true))
                
                .transform(TagGen.axeOrPickaxe())
                .onRegister(CreateIntegrationStress.setImpact(8.0))
                .item()
                .transform(ModelGen.customItemModel("create", "_", "block"))
                .register();

        EXTREME_CRUSHING_WHEEL = REGISTRATE.block("extreme_crushing_wheel", ExtremeCrushingWheelBlock::new)
                .properties(p -> p.mapColor(MapColor.METAL))
                .initialProperties(SharedProperties::stone)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .transform(TagGen.pickaxeOnly())
                .blockstate((c, p) -> CreateIntegrationBlockStateGen.axisBlock(c, p, s -> CreateIntegrationAssetLookup.partialBaseModel(c, p)))
                
                .onRegister(CreateIntegrationStress.setImpact(24.0))
                .item()
                .transform(ModelGen.customItemModel("create", "_", "block"))
                .register();

        EXTREME_CRUSHING_WHEEL_CONTROLLER = REGISTRATE.block("extreme_crushing_wheel_controller", ExtremeCrushingWheelControllerBlock::new)
                .properties(p -> p.mapColor(MapColor.STONE)
                        .noOcclusion()
                        .noLootTable()
                        .air()
                        .noCollission()
                        .pushReaction(PushReaction.BLOCK))
                .blockstate((c, p) -> p.getVariantBuilder(c.get())
                        .forAllStatesExcept(BlockStateGen.mapToAir(p), ExtremeCrushingWheelControllerBlock.FACING))
                .register();
    }
}
