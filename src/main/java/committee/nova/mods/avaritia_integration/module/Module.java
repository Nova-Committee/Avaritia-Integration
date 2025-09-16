package committee.nova.mods.avaritia_integration.module;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Base module interface
 *
 * @author IAFEnvoy
 */
public interface Module {
    default void init(IEventBus registryBus) {
    }

    default void process() {
    }

    default void initClient() {
    }

    default void processClient() {
    }

    //TODO::Maybe auto bus?
    default void registerEvent(IEventBus modBus, IEventBus gameBus) {
    }

    default void registerClientEvent(IEventBus modBus, IEventBus gameBus) {
    }

    default void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
    }
}
