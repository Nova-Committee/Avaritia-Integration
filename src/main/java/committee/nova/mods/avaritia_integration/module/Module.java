package committee.nova.mods.avaritia_integration.module;

import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Base module interface
 *
 * @author IAFEnvoy
 */
public interface Module {
    void init();

    void process();

    //TODO::Maybe auto bus?
    void registerEvent(IEventBus modBus, IEventBus gameBus);
}
