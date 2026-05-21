package committee.nova.mods.avaritia_integration.module.industrialforegoing.item;

import com.hrznstudio.titanium.api.augment.AugmentTypes;
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

public class ModEfficiencyAddonItem extends AddonItem{
    public ModEfficiencyAddonItem(int tier, Component materialName) {
        super(tier,materialName);
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Player playerIn) {
        super.onCraftedBy(stack, worldIn, playerIn);
        AugmentWrapper.setType(stack, AugmentTypes.EFFICIENCY, 1.0F - (float)this.tier * 0.1F);
    }

    @Override
    public @NotNull String getDescriptionId() {
        String addon = Component.translatable("item.industrialforegoing.addon").getString();
        return addon + Component.translatable("item.industrialforegoing.efficiency").getString() + "Tier " + materialName.getString() + " ";
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
        float reduction = tier * -10;
        tooltip.add(Component.translatable("tooltip.avaritia_integration.cooldown_amount").append(": " + reduction + "%").withStyle(ChatFormatting.GRAY));
    }
}
