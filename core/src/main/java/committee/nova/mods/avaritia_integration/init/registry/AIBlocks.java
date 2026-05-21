package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AIBlocks {
    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(AvaritiaIntegration.MOD_ID);

    public static final DeferredBlock<LiquidBlock> molten_blaze_block = REGISTRY.register("molten_blaze_fluid",
            ()->new BurningLiquidBlock(AIFluids.source_molten_blaze, createProperties(MapColor.COLOR_RED, 15), 10, 15f));

    public static final DeferredBlock<LiquidBlock> molten_crystal_matrix_block = REGISTRY.register("molten_crystal_matrix_fluid",
            ()->new BurningLiquidBlock(AIFluids.source_molten_crystal_matrix, createProperties(MapColor.COLOR_LIGHT_BLUE, 15), 10, 10f));

    public static final DeferredBlock<LiquidBlock> molten_neutron_block = REGISTRY.register("molten_neutron_fluid",
                ()->new BurningLiquidBlock(AIFluids.source_molten_neutron, createProperties(MapColor.COLOR_GRAY, 15), 10, 27f));

    public static final DeferredBlock<LiquidBlock> molten_star_block = REGISTRY.register("molten_star_fluid",
            ()->new BurningLiquidBlock(AIFluids.source_molten_star, createProperties(MapColor.COLOR_BLUE, 15), 10, 45f));

    public static BlockBehaviour.Properties createProperties(MapColor color, int lightLevel) {
        return BlockBehaviour.Properties.of().mapColor(color).replaceable().noCollission().randomTicks().strength(100.0F).lightLevel(state -> lightLevel).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY);
    }

    public static void registers(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
