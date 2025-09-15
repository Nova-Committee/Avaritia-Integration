package committee.nova.mods.avaritia_integration.api.event;

import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

/**
 * @author: cnlimiter
 */
public class ClientInitEvent extends ParallelEvent {

    public ClientInitEvent(ParallelDispatchEvent delegate) {
        super(delegate);
    }

}
