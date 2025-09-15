package committee.nova.mods.avaritia_integration.api.event;

import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * @author: cnlimiter
 */
public class ParallelEvent {
    ParallelDispatchEvent delegate;

    public ParallelEvent(ParallelDispatchEvent delegate) {
        this.delegate = delegate;
    }

    public CompletableFuture<Void> enqueueWork(Runnable work) {
        return delegate.enqueueWork(work);
    }

    public <T> CompletableFuture<T> enqueueWork(Supplier<T> work) {
        return delegate.enqueueWork(work);
    }
}
