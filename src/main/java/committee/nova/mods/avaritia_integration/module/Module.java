package committee.nova.mods.avaritia_integration.module;

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

    //TODO::Maybe auto bus?
    default void registerEvent(IEventBus modBus, IEventBus gameBus) {
    }
}
