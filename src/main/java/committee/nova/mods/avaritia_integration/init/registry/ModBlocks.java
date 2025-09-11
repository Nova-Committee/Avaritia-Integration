package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AvaritiaIntegration.MOD_ID);

        public static final RegistryObject<Block> asgard_dandelion =  registerBlock("asgard_dandelion",()->
                new FlowerBlock(()-> MobEffects.LUCK,5, BlockBehaviour.Properties.copy(Blocks.DANDELION).noCollission().lightLevel(level -> 15)));

        public static final RegistryObject<Block> potted_asgard_dandelion =  registerBlock("potted_asgard_dandelion",()->
            new FlowerPotBlock(()-> ((FlowerPotBlock)Blocks.FLOWER_POT),ModBlocks.asgard_dandelion,BlockBehaviour.Properties.copy(Blocks.POTTED_DANDELION).noOcclusion().lightLevel(level -> 15)));

    public static final RegistryObject<Block> soarleander =  registerBlock("soarleander",()->
            new FlowerBlock(()-> MobEffects.LUCK,5, BlockBehaviour.Properties.copy(Blocks.DANDELION).noCollission().lightLevel(level -> 5)));

    public static final RegistryObject<Block> potted_soarleander =  registerBlock("potted_soarleander",()->
            new FlowerPotBlock(()-> ((FlowerPotBlock)Blocks.FLOWER_POT),ModBlocks.soarleander,BlockBehaviour.Properties.copy(Blocks.POTTED_DANDELION).noOcclusion().lightLevel(level -> 5)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name,toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name,RegistryObject<T> block){
        return ModItems.ITEMS.register(name,()-> new BlockItem(block.get(),new Item.Properties()));
    }

}
