package committee.nova.mods.avaritia_integration.common.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import wayoftime.bloodmagic.common.item.BloodOrb;
import wayoftime.bloodmagic.common.item.IBloodOrb;
import wayoftime.bloodmagic.common.item.ItemBindableBase;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

import java.util.function.Supplier;

public class BloodOrbOfArmokItem extends ItemBindableBase implements IBloodOrb, IForgeItem {
    private final Supplier<BloodOrb> sup;

    public BloodOrbOfArmokItem(Supplier<BloodOrb> sup) {
        this.sup = sup;
    }

    public BloodOrb getOrb(ItemStack stack) {
        return (BloodOrb)this.sup.get();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BloodOrb orb = this.getOrb(stack);
        if (orb == null) {
            return InteractionResultHolder.fail(stack);
        } else if (world == null) {
            return super.use(world, player, hand);
        } else {
            world.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
            if (PlayerHelper.isFakePlayer(player)) {
                return super.use(world, player, hand);
            } else if (!stack.hasTag()) {
                return super.use(world, player, hand);
            } else {
                Binding binding = this.getBinding(stack);
                if (binding == null) {
                    return super.use(world, player, hand);
                } else if (world.isClientSide) {
                    return super.use(world, player, hand);
                } else {
                    SoulNetwork ownerNetwork = NetworkHelper.getSoulNetwork(binding);
                    if (binding.getOwnerId().equals(player.getGameProfile().getId())) {
                        ownerNetwork.setOrbTier(orb.getTier());
                    }


                    ownerNetwork.setCurrentEssence(1000000000);
                    return InteractionResultHolder.success(stack);
                }
            }
        }
    }
}
