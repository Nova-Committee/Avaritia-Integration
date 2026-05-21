package committee.nova.mods.avaritia_integration.init.mixins.create;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import committee.nova.mods.avaritia_integration.module.create.content.extreme_crusher.compat.BeltCrusherControllerBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CrushingWheelControllerBlockEntity.class)
public class CrushingWheelControllerBlockEntityMixin implements BeltCrusherControllerBlockEntity {
    @Shadow(remap = false) public ProcessingInventory inventory;

    @Override
    public ItemStack insertFromBelt(ItemStack stack, boolean simulate) {
        return ItemHandlerHelper.insertItemStacked(inventory, stack, simulate);
    }
}
