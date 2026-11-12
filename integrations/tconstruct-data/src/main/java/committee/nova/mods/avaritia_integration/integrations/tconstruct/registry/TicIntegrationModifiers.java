package committee.nova.mods.avaritia_integration.integrations.tconstruct.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.modifiers.BlazeCrownModifier;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.modifiers.CrystalshineModifier;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.modifiers.CrystaluixModifier;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.modifiers.StarDashModifier;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.modifiers.SuperheatModifier;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.modules.CondensingModule;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.modules.InfinitumModule;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.modules.RuleOverModule;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.modules.VaultSplitModule;

import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.RegisterEvent;

import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.capability.TinkerDataKeys;

public final class TicIntegrationModifiers {

    public static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister
            .create(AvaritiaIntegration.MOD_ID);
    public static final StaticModifier<BlazeCrownModifier> BLAZE_CROWN = MODIFIERS.register("blaze_crown",
            BlazeCrownModifier::new);
    public static final StaticModifier<CrystalshineModifier> CRYSTAL_SHINE = MODIFIERS.register("crystalshine",
            CrystalshineModifier::new);
    public static final StaticModifier<CrystaluixModifier> CRYSTALUIX = MODIFIERS.register("crystaluix",
            CrystaluixModifier::new);
    public static final StaticModifier<StarDashModifier> STAR_DASH = MODIFIERS.register("star_dash",
            StarDashModifier::new);
    public static final StaticModifier<SuperheatModifier> SUPERHEAT = MODIFIERS.register("superheat",
            SuperheatModifier::new);
    public static final TinkerDataCapability.TinkerDataKey<Integer> ETERNITY = TinkerDataKeys.INTEGER_REGISTRY.register(
            TinkerDataCapability.TinkerDataKey.of(AvaritiaIntegration.rl("eternity")));

    public static void registerSerializers(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            ModifierModule.LOADER.register(AvaritiaIntegration.rl("infinitum"), InfinitumModule.LOADER);
            ModifierModule.LOADER.register(AvaritiaIntegration.rl("condensing"), CondensingModule.LOADER);
            ModifierModule.LOADER.register(AvaritiaIntegration.rl("vault_split"), VaultSplitModule.LOADER);
            ModifierModule.LOADER.register(AvaritiaIntegration.rl("rule_over"), RuleOverModule.LOADER);
        }
    }

    private TicIntegrationModifiers() {}
}
