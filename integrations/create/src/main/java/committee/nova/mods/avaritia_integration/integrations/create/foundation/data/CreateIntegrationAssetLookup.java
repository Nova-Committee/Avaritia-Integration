package committee.nova.mods.avaritia_integration.integrations.create.foundation.data;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class CreateIntegrationAssetLookup {
    public static ModelFile partialBaseModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov,
                                             String... suffix) {
        String string = "/block";
        for (String suf : suffix)
            if (!suf.isEmpty())
                string += "_" + suf;
        final String location = "block/create/" + ctx.getName() + string;
        return prov.models()
                .getExistingFile(prov.modLoc(location));
    }

    /**
     * Custom block model from models/block/create/x.json
     */
    public static ModelFile standardModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov) {
        return prov.models()
                .getExistingFile(prov.modLoc("block/create/" + ctx.getName()));
    }
}
