package committee.nova.mods.avaritia_integration.module.industrialforegoing.item;

import com.hrznstudio.titanium.api.IRecipeProvider;
import com.hrznstudio.titanium.block.tile.MachineTile;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.item.BasicItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AddonItem extends Item implements IRecipeProvider {

    protected final int tier;
    protected final Component materialName;

    public AddonItem(int tier, Component materialName) {
        super(new Properties().stacksTo(16));
        this.tier = tier;
        this.materialName = materialName;
    }

    public int getTier() {
        return tier;
    }

    @Override
    public void registerRecipe(RecipeOutput recipeOutput) {}

    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide) {
            BlockPos blockpos = context.getClickedPos();
            BlockEntity entity = context.getLevel().getBlockEntity(blockpos);
            if (entity instanceof MachineTile<?> machineTile) {
                ItemStack stack = context.getItemInHand().copyWithCount(1);
                if (machineTile.canAcceptAugment(stack)) {
                    SidedInventoryComponent<? extends MachineTile<?>> augmentInv = machineTile.getAugmentInventory();

                    for (int i = 0; i < augmentInv.getSlots(); ++i) {
                        if (augmentInv.getStackInSlot(i).isEmpty()) {
                            augmentInv.setStackInSlot(i, stack);
                            context.getItemInHand().shrink(1);
                            return InteractionResult.CONSUME_PARTIAL;
                        }
                    }
                }
            }
        }

        return super.useOn(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        if (this.hasTooltipDetails(null)) {
            this.addTooltipDetails(null, stack, tooltip, flagIn.isAdvanced());
        }

        BasicItem.Key[] keys = BasicItem.Key.values();

        for (BasicItem.Key key : keys) {
            if (this.hasTooltipDetails(key)) {
                if (key.isDown()) {
                    this.addTooltipDetails(key, stack, tooltip, flagIn.isAdvanced());
                } else {
                    tooltip.add(Component.literal("Hold " + ChatFormatting.YELLOW + key.getSerializedName() +
                            ChatFormatting.GRAY + " for more information"));
                }
            }
        }
    }

    public void addTooltipDetails(@Nullable BasicItem.Key key, ItemStack stack, List<Component> tooltip,
                                  boolean advanced) {}

    public boolean hasTooltipDetails(@Nullable BasicItem.Key key) {
        return false;
    }
}
