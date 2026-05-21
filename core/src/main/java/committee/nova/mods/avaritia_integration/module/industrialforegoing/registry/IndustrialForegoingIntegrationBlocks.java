package committee.nova.mods.avaritia_integration.module.industrialforegoing.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IndustrialForegoingIntegrationBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, AvaritiaIntegration.MOD_ID);
}
