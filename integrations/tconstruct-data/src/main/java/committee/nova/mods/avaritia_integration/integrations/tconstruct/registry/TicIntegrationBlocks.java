package committee.nova.mods.avaritia_integration.integrations.tconstruct.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.init.registry.AIBlocks;
import committee.nova.mods.avaritia_integration.init.registry.BurningLiquidBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TicIntegrationBlocks {

    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(AvaritiaIntegration.MOD_ID);

    public static final DeferredBlock<Block> FAKE_BEDROCK = REGISTRY.register("fake_bedrock",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM).strength(1000.0F, 3600000.0F)
                    .isValidSpawn((state, level, pos, value) -> false)));

    public static final DeferredBlock<Block> FAKE_END_PORTAL_FRAME = REGISTRY.register("fake_end_portal_frame",
            () -> new EndPortalFrameBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN)
                    .instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.GLASS).lightLevel(state -> 1)
                    .strength(400F, 3600000.0F)));

    public static final DeferredBlock<Block> FAKE_END_PORTAL = REGISTRY.register("fake_end_portal",
            () -> new EndPortalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).noCollission()
                    .lightLevel(state -> 15).strength(400F, 3600000.0F).pushReaction(PushReaction.BLOCK)));

    public static final DeferredBlock<LiquidBlock> MOLTEN_INFINITY_FLUID = REGISTRY.register("molten_infinity_fluid",
            () -> new BurningLiquidBlock(TicIntegrationFluids.SOURCE_MOLTEN_INFINITY,
                    AIBlocks.createProperties(MapColor.COLOR_LIGHT_GRAY, 15), 10, 45f));

    private TicIntegrationBlocks() {}
}
