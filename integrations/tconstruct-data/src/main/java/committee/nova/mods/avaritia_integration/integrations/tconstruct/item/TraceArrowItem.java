package committee.nova.mods.avaritia_integration.integrations.tconstruct.item;

import committee.nova.mods.avaritia.common.entity.arrow.TraceArrowEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TraceArrowItem extends ArrowItem {

    public TraceArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        return new TraceArrowEntity(level, shooter);
    }
}
