package committee.nova.mods.avaritia_integration.integrations.slashblade.slasharts;

import committee.nova.mods.avaritia_integration.integrations.slashblade.entity.MeteoriteSwordEntity;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


import mods.flammpfeil.slashblade.RegistryEvents;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.slasharts.SlashArts;

import java.util.function.Function;

public class MeteoriteSwordSlashArts extends SlashArts {

    public MeteoriteSwordSlashArts(Function<LivingEntity, ResourceLocation> state) {
        super(state);
    }

    @Override
    public ResourceLocation doArts(SlashArts.ArtsType type, LivingEntity user) {
        Level world = user.level();
        ItemStack stack = user.getMainHandItem();
        BladeStateAccess.of(stack).ifPresent(state -> {
            final int cost = 10;
            if (state.getProudSoulCount() >= cost) {
                state.setProudSoulCount(state.getProudSoulCount() - cost);
            } else {
                stack.hurtAndBreak(10, user, EquipmentSlot.MAINHAND);
            }
            if (user instanceof Player player && !world.isClientSide()) {
                createTriangleSwordArray(player, world);
            }
        });
        return super.doArts(type, user);
    }

    private static void createTriangleSwordArray(Player player, Level world) {
        Vec3 playerLook = player.getLookAngle();
        Vec3 playerPos = player.position().add(0, player.getEyeHeight(), 0);
        int[] rainbowColors = {
                0xFF0000,
                0xFF8000,
                0xFFFF00,
                0x00FF00,
                0x00FFFF,
                0x0000FF,
                0x8000FF
        };
        Vec3[][] trianglePositions = {
                { new Vec3(0, 0.8, 0) },
                { new Vec3(-0.4, 0.4, 0), new Vec3(0.4, 0.4, 0) },
                { new Vec3(-0.6, 0, 0), new Vec3(0, 0, 0), new Vec3(0.6, 0, 0) },
                { new Vec3(-0.8, -0.4, 0), new Vec3(0.8, -0.4, 0) }
        };

        Vec3 right = playerLook.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 up = right.cross(playerLook).normalize();
        float baseDistance = 2.5f;
        int swordIndex = 0;
        int delay = 0;

        for (Vec3[] layer : trianglePositions) {
            for (Vec3 relativePos : layer) {
                if (swordIndex >= 7) {
                    break;
                }
                Vec3 worldOffset = right.scale(relativePos.x)
                        .add(up.scale(relativePos.y))
                        .add(playerLook.scale(baseDistance + relativePos.z));
                Vec3 spawnPos = playerPos.add(worldOffset);

                MeteoriteSwordEntity sword = new MeteoriteSwordEntity(RegistryEvents.StormSwords, world);
                sword.setOwner(player);
                sword.setDamage(10000f);
                sword.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                sword.setDelay(delay);
                sword.setColor(rainbowColors[swordIndex]);

                Vec3 shootDir = playerLook.add(right.scale(relativePos.x * 0.2))
                        .add(up.scale(relativePos.y * 0.1))
                        .normalize();
                sword.shoot(shootDir.x, shootDir.y, shootDir.z, 3.0F, 0.2F);
                world.addFreshEntity(sword);

                swordIndex++;
                delay += 3;
            }
            if (swordIndex >= 7) {
                break;
            }
        }
    }
}
