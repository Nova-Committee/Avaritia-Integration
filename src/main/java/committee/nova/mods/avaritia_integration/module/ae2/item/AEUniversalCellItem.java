package committee.nova.mods.avaritia_integration.module.ae2.item;

import committee.nova.mods.avaritia_integration.module.ae2.me.IAEUniversalCell;
import net.minecraft.world.item.Item;

/**
 * 用于承载物品到存储系统的桥接物品
 */
public abstract class AEUniversalCellItem extends Item implements IAEUniversalCell
{
    public AEUniversalCellItem(Properties pProperties)
    {
        super(pProperties);
    }
}
