package committee.nova.mods.avaritia_integration.integrations.create.registry;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;

public class CreateIntegrationSpriteShifts {
    public static final SpriteShiftEntry STAR_BURNER_FLAME = get("block/blaze_burner_flame", "block/create/blaze_burner_flame_starheated_scroll");
    public static final SpriteShiftEntry BLAZE_BURNER_FLAME = get("block/blaze_burner_flame", "block/create/blaze_burner_flame_blazeheated_scroll");
    public static final CTSpriteShiftEntry CRYSTAL_MATRIX_CASING = omni("crystal_matrix_casing");

    public static void init() {
    }

    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, AvaritiaIntegration.rl("block/create/" + blockTextureName),
                AvaritiaIntegration.rl("block/create/" + connectedTextureName + "_connected"));
    }

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(Create.asResource(originalLocation), AvaritiaIntegration.rl(targetLocation));
    }
}
