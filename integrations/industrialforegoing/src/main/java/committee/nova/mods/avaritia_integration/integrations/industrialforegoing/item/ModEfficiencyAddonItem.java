package committee.nova.mods.avaritia_integration.integrations.industrialforegoing.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.hrznstudio.titanium.api.augment.AugmentTypes;
import com.hrznstudio.titanium.item.AugmentWrapper;
import com.hrznstudio.titanium.item.BasicItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModEfficiencyAddonItem extends AddonItem {

    public ModEfficiencyAddonItem(int tier, Component materialName) {
        super(tier, materialName);
    }

    public static float energyMultiplier(int tier) {
        return switch (tier) {
            case 3, 4 -> 0.25F;
            case 5, 8 -> 0.10F;
            case 12, 16 -> 0.05F;
            default -> 0.01F;
        };
    }

    @Override
    protected void applyAugment(ItemStack stack) {
        AugmentWrapper.setType(stack, AugmentTypes.EFFICIENCY, energyMultiplier(this.tier));
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Player playerIn) {
        super.onCraftedBy(stack, worldIn, playerIn);
        applyAugment(stack);
    }

    @Override
    public @NotNull String getDescriptionId() {
        String addon = Component.translatable("item.industrialforegoing.addon").getString();
        return addon + Component.translatable("item.industrialforegoing.efficiency").getString() + "Tier " +
                materialName.getString() + " ";
    }

    @Override
    public boolean hasTooltipDetails(@Nullable BasicItem.Key key) {
        if (key == null) {
            return true;
        }
        return super.hasTooltipDetails(key);
    }

    @Override
    public void addTooltipDetails(@Nullable BasicItem.Key key, ItemStack stack, List<Component> tooltip,
                                  boolean advanced) {
        super.addTooltipDetails(key, stack, tooltip, advanced);
        float reduction = (1.0F - energyMultiplier(tier)) * -100.0F;
        tooltip.add(Component.translatable("tooltip.avaritia_integration.cooldown_amount")
                .append(": " + reduction + "%").withStyle(ChatFormatting.GRAY));
    }
}
