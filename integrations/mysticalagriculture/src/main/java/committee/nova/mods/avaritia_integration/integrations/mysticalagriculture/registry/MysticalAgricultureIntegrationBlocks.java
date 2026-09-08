package committee.nova.mods.avaritia_integration.integrations.mysticalagriculture.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.blakebr0.cucumber.block.BaseBlock;

public final class MysticalAgricultureIntegrationBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AvaritiaIntegration.MOD_ID);

    public static final DeferredBlock<Block> INFINITY_CRUX = BLOCKS.register("infinity_crux",
            () -> new BaseBlock(SoundType.STONE, 5.0F, 10.0F));

    public static final DeferredBlock<Block> CRYSTAL_MATRIX_CRUX = BLOCKS.register("crystal_matrix_crux",
            () -> new BaseBlock(SoundType.STONE, 5.0F, 10.0F));
}
