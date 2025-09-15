package committee.nova.mods.avaritia_integration.api.module;

import committee.nova.mods.avaritia_integration.api.event.ClientInitEvent;
import committee.nova.mods.avaritia_integration.api.event.InitEvent;
import committee.nova.mods.avaritia_integration.api.event.PostInitEvent;
import committee.nova.mods.avaritia_integration.api.event.ServerInitEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.IModBusEvent;

/**
 * @author: cnlimiter
 */
public abstract class AbModule {
    public ResourceLocation uid;

    protected void preInit() {
        // NO-OP
    }

    protected void register() {
        // NO-OP
    }

    protected void init(InitEvent event) {
        // NO-OP
    }

    protected void clientInit(ClientInitEvent event) {
        // NO-OP
    }

    protected void serverInit(ServerInitEvent event) {
        // NO-OP
    }

    protected void postInit(PostInitEvent event) {
        // NO-OP
    }
    public ResourceLocation RL(String path) {
        return new ResourceLocation(uid.getNamespace(), path);
    }

}
