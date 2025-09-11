package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.common.block.AsgardDandelionBlock;
import committee.nova.mods.avaritia_integration.common.block.InfinityManaPoolBlock;
import committee.nova.mods.avaritia_integration.common.block.SoarleanderBlock;
import committee.nova.mods.avaritia_integration.common.blockentity.AsgardDandelionBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.SoarleanderBlockEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AvaritiaIntegration.MOD_ID);
    public static final BlockBehaviour.Properties FLOWER_PROPS = BlockBehaviour.Properties.copy(Blocks.POPPY);
    public static final BlockEntityType<AsgardDandelionBlockEntity> ASGARD = XplatAbstractions.INSTANCE.createBlockEntityType(AsgardDandelionBlockEntity::new);
    public static final BlockEntityType<SoarleanderBlockEntity> SOARLEANDER = XplatAbstractions.INSTANCE.createBlockEntityType(SoarleanderBlockEntity::new);
        public static final RegistryObject<Block> asgard_dandelion =  registerBlock("asgard_dandelion",()->
                new AsgardDandelionBlock(MobEffects.HUNGER, 0, FLOWER_PROPS, () -> ASGARD));

        public static final RegistryObject<Block> potted_asgard_dandelion =  registerBlock("potted_asgard_dandelion",()->
            new FlowerPotBlock(()-> ((FlowerPotBlock)Blocks.FLOWER_POT),ModBlocks.asgard_dandelion,BlockBehaviour.Properties.copy(Blocks.POTTED_DANDELION).noOcclusion().lightLevel(level -> 15)));

    public static final RegistryObject<Block> soarleander =  registerBlock("soarleander",()->
            new SoarleanderBlock(MobEffects.WITHER, 0, FLOWER_PROPS, () -> SOARLEANDER));

    public static final RegistryObject<Block> potted_soarleander =  registerBlock("potted_soarleander",()->
            new FlowerPotBlock(()-> ((FlowerPotBlock)Blocks.FLOWER_POT),ModBlocks.soarleander,BlockBehaviour.Properties.copy(Blocks.POTTED_DANDELION).noOcclusion().lightLevel(level -> 5)));

    public static final RegistryObject<Block> infinity_mana_pool = registerBlock("infinity_mana_pool",()->
            new InfinityManaPoolBlock(InfinityManaPoolBlock.Variant.CREATIVE, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name,toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name,RegistryObject<T> block){
        return ModItems.ITEMS.register(name,()-> new BlockItem(block.get(),new Item.Properties()));
    }

}
