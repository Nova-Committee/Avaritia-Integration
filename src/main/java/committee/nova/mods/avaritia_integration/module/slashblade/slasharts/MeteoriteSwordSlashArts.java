package committee.nova.mods.avaritia_integration.module.slashblade.slasharts;

import committee.nova.mods.avaritia_integration.module.slashblade.entity.MeteoriteSwordEntity;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.Function;

public class MeteoriteSwordSlashArts extends SlashArts {
    public MeteoriteSwordSlashArts(Function<LivingEntity, ResourceLocation> state) {
        super(state);
    }

    @Override
    public ResourceLocation doArts(SlashArts.ArtsType type, LivingEntity user) {
        Level world = user.level();
        ItemStack stack = user.getMainHandItem();
        LazyOptional<ISlashBladeState> stateOpt = stack.getCapability(ItemSlashBlade.BLADESTATE);
        stateOpt.ifPresent(state -> {
            final int cost = 10;
            if (state.getProudSoulCount() >= cost) {
                state.setProudSoulCount(state.getProudSoulCount() - cost);
            } else {
                stack.hurtAndBreak(10, user, (entity) -> {
                    entity.broadcastBreakEvent(entity.getUsedItemHand());
                });
            }
            if (user instanceof Player player) {
                if (!world.isClientSide()) {
                    createTriangleSwordArray(player, world);
                }
            }
        });

        return super.doArts(type, user);
    }

    private static void createTriangleSwordArray(Player player, Level world) {
        Vec3 playerLook = player.getLookAngle();
        Vec3 playerPos = player.position().add(0, player.getEyeHeight(), 0);
        int[] rainbowColors = {
                0xFF0000, // 红
                0xFF8000, // 橙
                0xFFFF00, // 黄
                0x00FF00, // 绿
                0x00FFFF, // 青
                0x0000FF, // 蓝
                0x8000FF  // 紫
        };
        // 三角形排列位置（相对于玩家前方）
        // 排列：    *      (顶部1把)
        //        * *     (中层2把)
        //       * * *    (上层3把)
        //      *     *   (底层2把，中间空)
        Vec3[][] trianglePositions = {
                // 顶部层 - 1把剑
                {new Vec3(0, 0.8, 0)},
                // 中上层 - 2把剑
                {new Vec3(-0.4, 0.4, 0), new Vec3(0.4, 0.4, 0)},
                // 中层 - 3把剑
                {new Vec3(-0.6, 0, 0), new Vec3(0, 0, 0), new Vec3(0.6, 0, 0)},
                // 底层 - 2把剑（中间空）
                {new Vec3(-0.8, -0.4, 0), new Vec3(0.8, -0.4, 0)}
        };

        // 计算右向和上向向量
        Vec3 right = playerLook.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 up = right.cross(playerLook).normalize();

        // 距离玩家的基础距离
        float baseDistance = 2.5f;

        int swordIndex = 0;
        int delay = 0;

        // 按层创建剑
        for (Vec3[] layer : trianglePositions) {
            for (Vec3 relativePos : layer) {
                if (swordIndex >= 7) break; // 确保只创建7把剑

                // 将相对位置转换为世界坐标
                Vec3 worldOffset = right.scale(relativePos.x)
                        .add(up.scale(relativePos.y))
                        .add(playerLook.scale(baseDistance + relativePos.z));

                Vec3 spawnPos = playerPos.add(worldOffset);

                // 创建幻影剑
                MeteoriteSwordEntity sword = new MeteoriteSwordEntity(SlashBlade.RegistryEvents.StormSwords, world);
                sword.setOwner(player);
                sword.setDamage(10000f);
                sword.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                sword.setDelay(delay);
                sword.setColor(rainbowColors[swordIndex]);

                // 射击方向：稍微向外扩散，然后向前
                Vec3 shootDir = playerLook.add(right.scale(relativePos.x * 0.2))
                        .add(up.scale(relativePos.y * 0.1))
                        .normalize();

                sword.shoot(shootDir.x, shootDir.y, shootDir.z, 3.0F, 0.2F);
                world.addFreshEntity(sword);

                swordIndex++;
                delay += 3; // 每把剑延迟3tick
            }
            if (swordIndex >= 7) break;
        }
    }
}