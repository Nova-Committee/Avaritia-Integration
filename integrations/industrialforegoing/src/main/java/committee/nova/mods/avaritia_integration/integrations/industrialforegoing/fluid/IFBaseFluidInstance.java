package committee.nova.mods.avaritia_integration.integrations.industrialforegoing.fluid;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class IFBaseFluidInstance {

    private final DeferredHolder<FluidType, FluidType> fluidType;
    private final DeferredHolder<Fluid, Fluid> flowingFluid;
    private final DeferredHolder<Fluid, Fluid> sourceFluid;
    private final DeferredHolder<Item, Item> bucketFluid;
    private final DeferredHolder<Block, Block> blockFluid;

    private final String fluidName;

    public IFBaseFluidInstance(
                               DeferredRegister<Item> itemRegister,
                               DeferredRegister<Block> blockRegister,
                               DeferredRegister<Fluid> fluidRegister,
                               DeferredRegister<FluidType> fluidTypeRegister,
                               String name,
                               FluidType.Properties fluidTypeProperties,
                               IClientFluidTypeExtensions renderProperties) {
        this.fluidName = name;

        this.fluidType = fluidTypeRegister.register(name, () -> new FluidType(fluidTypeProperties) {

            @Override
            @SuppressWarnings("removal")
            public void initializeClient(@NotNull Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(renderProperties);
            }
        });

        this.sourceFluid = fluidRegister.register(name + "_source",
                () -> new Source(this));

        this.flowingFluid = fluidRegister.register(name + "_flowing",
                () -> new Flowing(this));

        this.blockFluid = blockRegister.register(name,
                () -> new LiquidBlock(
                        (FlowingFluid) sourceFluid.get(),
                        BlockBehaviour.Properties.of()
                                .replaceable()
                                .noCollission()
                                .strength(100f)
                                .pushReaction(PushReaction.DESTROY)
                                .liquid().sound(SoundType.EMPTY)
                                .noLootTable()));

        this.bucketFluid = itemRegister.register(name + "_bucket",
                () -> new BucketItem(
                        sourceFluid.get(),
                        new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    }

    public DeferredHolder<FluidType, FluidType> getFluidType() {
        return fluidType;
    }

    public DeferredHolder<Fluid, Fluid> getFlowingFluid() {
        return flowingFluid;
    }

    public DeferredHolder<Fluid, Fluid> getSourceFluid() {
        return sourceFluid;
    }

    public Item getBucketFluid() {
        return bucketFluid.get();
    }

    public Block getBlockFluid() {
        return blockFluid.get();
    }

    public String getFluidName() {
        return fluidName;
    }

    public static class Source extends IFBaseFluid {

        public Source(IFBaseFluidInstance instance) {
            super(instance);
        }

        @Override
        public int getAmount(@NotNull FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(@NotNull FluidState state) {
            return true;
        }
    }

    public static class Flowing extends IFBaseFluid {

        public Flowing(IFBaseFluidInstance instance) {
            super(instance);
            this.registerDefaultState(
                    this.getStateDefinition().any().setValue(LEVEL, 7));
        }

        @Override
        protected void createFluidStateDefinition(
                                                  StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(@NotNull FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(@NotNull FluidState state) {
            return false;
        }
    }
}
