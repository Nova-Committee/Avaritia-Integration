package committee.nova.mods.avaritia_integration.api.event;

import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

/**
 * @author: cnlimiter
 */
public class PostInitEvent extends ParallelEvent {

    public PostInitEvent(ParallelDispatchEvent delegate) {
        super(delegate);
    }

}
