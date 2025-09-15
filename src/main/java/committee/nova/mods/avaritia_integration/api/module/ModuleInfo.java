package committee.nova.mods.avaritia_integration.api.module;

import committee.nova.mods.avaritia_integration.api.ModContext;
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
public class ModuleInfo {
    public final AbModule module;
    public final ModContext context;

    public ModuleInfo(ResourceLocation rl, AbModule module, ModContext context) {
        this.module = module;
        this.context = context;
        module.uid = rl;
    }

    public void preInit() {
        context.setActiveContainer();
        module.preInit();
    }

    public void register() {
        context.setActiveContainer();
        module.register();
    }

    public void init(InitEvent event) {
        context.setActiveContainer();
        module.init(event);
    }

    public void clientInit(ClientInitEvent event) {
        context.setActiveContainer();
        module.clientInit(event);
    }

    public void serverInit(ServerInitEvent event) {
        context.setActiveContainer();
        module.serverInit(event);
    }

    public void postInit(PostInitEvent event) {
        context.setActiveContainer();
        module.postInit(event);
    }
}
