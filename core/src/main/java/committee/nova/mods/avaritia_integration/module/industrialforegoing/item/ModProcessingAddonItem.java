package committee.nova.mods.avaritia_integration.module.industrialforegoing.item;

import com.buuz135.industrial.item.addon.ProcessingAddonItem;
import com.hrznstudio.titanium.item.AugmentWrapper;
import com.hrznstudio.titanium.item.BasicItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModProcessingAddonItem extends AddonItem{
    public ModProcessingAddonItem(int tier, Component materialName) {
        super(tier,materialName);
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Player playerIn) {
        super.onCraftedBy(stack, worldIn, playerIn);
        AugmentWrapper.setType(stack, ProcessingAddonItem.PROCESSING, (float)(1 + this.tier));
    }

    @Override
    public @NotNull String getDescriptionId() {
        String addon = Component.translatable("item.industrialforegoing.addon").getString();
        return addon + Component.translatable("item.industrialforegoing.processing").getString() + "Tier " + materialName.getString() + " ";
    }

    @Override
    public boolean hasTooltipDetails(@Nullable BasicItem.Key key) {
        if(key == null){
            return true;
        }
        return super.hasTooltipDetails(key);
    }


    @Override
    public void addTooltipDetails(@Nullable BasicItem.Key key, ItemStack stack, List<Component> tooltip, boolean advanced) {
        super.addTooltipDetails(key, stack, tooltip, advanced);
        float upgrade = 1 + tier;
        tooltip.add(Component.translatable("item.industrialforegoing.processing").append("x" + upgrade).withStyle(ChatFormatting.GRAY));
    }
}
