package committee.nova.mods.avaritia_integration.module.botania.item.block.entity;

import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;

public class SoarleanderBlockEntity extends GeneratingFlowerBlockEntity {
    private static final int MANA_PER_DAMAGE = 32;
    private static final int MANA_PER_KILL = 1500;
    private static final int MANA_PER_CHICKEN_KILL = 5000;
    private static final float DAMAGE_AMOUNT = 4.0f;

    public SoarleanderBlockEntity(BlockPos pos, BlockState state) {
        super(BotaniaIntegrationBlockEntities.SOARLEANDER_BLOCK_ENTITIES.get(), pos, state);
    }

    @Override
    public int getMaxMana() {
        return 32767;
    }

    @Override
    public int getColor() {
        return 0xFF0000;
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Circle(this.getBlockPos(), 15.0);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();
        if (this.level == null || this.level.isClientSide) return;


        if (this.level.getGameTime() % 5 == 0) {
            Vec3 offset = this.level.getBlockState(this.getBlockPos()).getOffset(this.level, this.getBlockPos());
            double x = this.getBlockPos().getX() + offset.x;
            double y = this.getBlockPos().getY() + offset.y;
            double z = this.getBlockPos().getZ() + offset.z;
            BotaniaAPI.instance().sparkleFX(this.level, x + 0.3 + Math.random() * 0.5, y + 0.5 + Math.random() * 0.5, z + 0.3 + Math.random() * 0.5, 1.0f, 0.0f, 0.0f, 1.0f, 5);
        }

        if (this.level.getGameTime() % 20 != 0) return;

        BlockPos pos = this.getBlockPos();
        AABB area = new AABB(pos).inflate(15.0);

        this.level.getEntitiesOfClass(LivingEntity.class, area, entity ->
                        !(entity instanceof Player) && !(entity instanceof ArmorStand) && entity.isAlive())
                .forEach(this::attackEntity);
        double particleChance = 1F - (double) this.getMana() / (double) this.getMaxMana() / 3.5F;
        int color = this.getColor();
        float red = (color >> 16 & 0xFF) / 255F;
        float green = (color >> 8 & 0xFF) / 255F;
        float blue = (color & 0xFF) / 255F;
        if (Math.random() > particleChance) {
            Vec3 offset = this.level.getBlockState(this.getBlockPos()).getOffset(this.level, this.getBlockPos());
            double x = this.getBlockPos().getX() + offset.x;
            double y = this.getBlockPos().getY() + offset.y;
            double z = this.getBlockPos().getZ() + offset.z;
            BotaniaAPI.instance().sparkleFX(this.getLevel(), x + 0.3 + Math.random() * 0.5, y + 0.5 + Math.random() * 0.5, z + 0.3 + Math.random() * 0.5, red, green, blue, (float) Math.random(), 5);
        }
    }

    private void attackEntity(LivingEntity entity) {
        boolean isChicken = entity instanceof Chicken;

        DamageSource voidDamage = this.level.damageSources().fellOutOfWorld();
        if (entity.hurt(voidDamage, DAMAGE_AMOUNT)) {
            this.addMana(MANA_PER_DAMAGE);

            if (entity.isDeadOrDying()) {
                if (isChicken) {
                    entity.die(voidDamage);
                    this.addMana(MANA_PER_CHICKEN_KILL);
                } else {
                    this.addMana(MANA_PER_KILL);
                }
            }
            this.sync();
        }
    }
}
