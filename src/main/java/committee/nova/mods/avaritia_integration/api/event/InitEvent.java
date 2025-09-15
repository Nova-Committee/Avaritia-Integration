package committee.nova.mods.avaritia_integration.api.event;

import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

/**
 * @author: cnlimiter
 */
public class InitEvent extends ParallelEvent{
    public InitEvent(ParallelDispatchEvent delegate) {
        super(delegate);
    }

}
