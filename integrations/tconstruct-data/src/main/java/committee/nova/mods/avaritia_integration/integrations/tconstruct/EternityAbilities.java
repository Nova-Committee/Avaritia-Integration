package committee.nova.mods.avaritia_integration.integrations.tconstruct;

import committee.nova.mods.avaritia.init.registry.ModDamageTypes;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.tconstruct.registry.TicIntegrationModifiers;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

public final class EternityAbilities {

    private static final ResourceLocation FLIGHT = AvaritiaIntegration.rl("eternity_flight");
    private static final ResourceLocation SPEED = AvaritiaIntegration.rl("eternity_speed");
    private static final ResourceLocation STEP = AvaritiaIntegration.rl("eternity_step");

    public static void tick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }
        int level = player.isAlive() ? ArmorLevelModule.getLevel(player, TicIntegrationModifiers.ETERNITY) : 0;
        updateAttribute(player, NeoForgeMod.CREATIVE_FLIGHT, FLIGHT, 1, level >= 3);
        // Four armor pieces plus a held infinity shield can contribute the fifth level.
        updateAttribute(player, Attributes.MOVEMENT_SPEED, SPEED, 0.03, level >= 5);
        updateAttribute(player, Attributes.STEP_HEIGHT, STEP, 0.4625, level >= 5);
        if (level >= 2) {
            player.setAirSupply(player.getMaxAirSupply());
            player.getFoodData().setFoodLevel(20);
            player.getFoodData().setSaturation(20);
            MobEffectInstance vision = player.getEffect(MobEffects.NIGHT_VISION);
            if (vision == null || vision.getDuration() < 280) {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false));
            }
        }
    }

    public static void respawn(PlayerEvent.PlayerRespawnEvent event) {
        // TinkerData uses entity equality; respawn reuses the old network ID.
        // Clear only our accumulated level before equipment hooks rebuild it.
        TinkerDataCapability.getData(event.getEntity()).remove(TicIntegrationModifiers.ETERNITY);
    }

    private static void updateAttribute(Player player, Holder<Attribute> attribute, ResourceLocation id,
                                        double amount, boolean enabled) {
        var instance = player.getAttribute(attribute);
        if (instance == null) {
            return;
        }
        if (enabled) {
            if (!instance.hasModifier(id)) {
                instance.addTransientModifier(new AttributeModifier(id, amount, AttributeModifier.Operation.ADD_VALUE));
            }
        } else {
            instance.removeModifier(id);
        }
    }

    public static void protect(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player &&
                ArmorLevelModule.getLevel(player, TicIntegrationModifiers.ETERNITY) >= 4 &&
                !event.getSource().is(ModDamageTypes.INFINITY)) {
            event.setCanceled(true);
        }
    }

    private EternityAbilities() {}
}
