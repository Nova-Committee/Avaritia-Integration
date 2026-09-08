package committee.nova.mods.avaritia_integration.integrations.create.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class CreateIntegrationPartialModels {
    public static final PartialModel EXTREME_BLAZE_INERT = block("extreme_blaze_burner/blaze/inert");
    public static final PartialModel BLAZE_STAR = block("extreme_blaze_burner/blaze/star");
    public static final PartialModel BLAZE_STAR_ACTIVE = block("extreme_blaze_burner/blaze/star_active");
    public static final PartialModel BLAZE_BLAZE = block("extreme_blaze_burner/blaze/blaze");
    public static final PartialModel BLAZE_BLAZE_ACTIVE = block("extreme_blaze_burner/blaze/blaze_active");
    public static final PartialModel CCA_LIQUID_HAT = block("extreme_blaze_burner/hat/cca/liquid_hat");
    public static final PartialModel BLAZE_BURNER_STAR_RODS = block("extreme_blaze_burner/starheated_rods_small");
    public static final PartialModel BLAZE_BURNER_STAR_RODS_2 = block("extreme_blaze_burner/starheated_rods_large");
    public static final PartialModel BLAZE_BURNER_BLAZE_RODS = block("extreme_blaze_burner/blazeheated_rods_small");
    public static final PartialModel BLAZE_BURNER_BLAZE_RODS_2 = block("extreme_blaze_burner/blazeheated_rods_large");
    public static final PartialModel NEUTRON_MECHANICAL_PRESS_HEAD = block("neutron_mechanical_press/head");
    public static final PartialModel MATRIX_MECHANICAL_MIXER_POLE = block("matrix_mechanical_mixer/pole");
    public static final PartialModel MATRIX_MECHANICAL_MIXER_HEAD = block("matrix_mechanical_mixer/head");
    public static final PartialModel MATRIX_MECHANICAL_MIXER_COGWHEEL = block("matrix_mechanical_mixer/cogwheel");
    public static final PartialModel EXTREME_ENCASED_FAN_INNER = block("extreme_encased_fan/propeller");
    public static final PartialModel EXTREME_CRUSHING_WHEEL = block("extreme_crushing_wheel/block");

    private static PartialModel block(String path) {
        return PartialModel.of(AvaritiaIntegration.rl("block/create/" + path));
    }

    public static void init() {
    }
}
