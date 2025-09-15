package committee.nova.mods.avaritia_integration.module;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.function.Consumer;

public interface Module {
    void setup();

    void registerEvent(IEventBus modBus,IEventBus gameBus);
}
