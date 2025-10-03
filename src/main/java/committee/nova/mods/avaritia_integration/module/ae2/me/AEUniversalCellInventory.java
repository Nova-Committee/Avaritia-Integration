package committee.nova.mods.avaritia_integration.module.ae2.me;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import net.minecraft.network.chat.Component;

/** 能存储多种不同资源的元件的内部存储 */
public class AEUniversalCellInventory implements MEStorage
{
    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source)
    {
        return MEStorage.super.isPreferredStorageFor(what, source);
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source)
    {
        return MEStorage.super.insert(what, amount, mode, source);
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source)
    {
        return MEStorage.super.extract(what, amount, mode, source);
    }

    @Override
    public void getAvailableStacks(KeyCounter out)
    {
        MEStorage.super.getAvailableStacks(out);
    }

    @Override
    public Component getDescription()
    {
        return null;
    }
}
