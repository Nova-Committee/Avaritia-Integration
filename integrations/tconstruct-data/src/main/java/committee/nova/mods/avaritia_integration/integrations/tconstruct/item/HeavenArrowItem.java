package committee.nova.mods.avaritia_integration.integrations.tconstruct.item;

import committee.nova.mods.avaritia.common.entity.arrow.HeavenArrowEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HeavenArrowItem extends ArrowItem {

    public HeavenArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        return new HeavenArrowEntity(level, shooter);
    }
}
