package committee.nova.mods.avaritia_integration.module.botania.item.block.entity;

import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.block_entity.RadiusDescriptor.Rectangle;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.api.mana.ManaReceiver;

import java.awt.*;

public class AsgardDandelionBlockEntity extends GeneratingFlowerBlockEntity {
    private static final int RANGE = 8;

    public AsgardDandelionBlockEntity(BlockPos pos, BlockState state) {
        super(BotaniaIntegrationBlockEntities.ASGARD_DANDELION.get(), pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (this.level == null || this.level.isClientSide) return;

        this.addMana(2147483647);

        if (this.getMana() > 0) {
            for (int dx = -RANGE; dx <= RANGE; dx++) {
                for (int dy = -RANGE; dy <= RANGE; dy++) {
                    for (int dz = -RANGE; dz <= RANGE; dz++) {
                        BlockPos pos = this.getBlockPos().offset(dx, dy, dz);
                        BlockEntity be = this.level.getBlockEntity(pos);

                        if (be instanceof ManaPool pool) {
                            if (!pool.isFull()) {
                                int manaToSend = Math.min(this.getMana(), 32000);
                                if (manaToSend > 0) {
                                    pool.receiveMana(manaToSend);
                                    this.addMana(-manaToSend);
                                    this.sync();
                                    if (this.getMana() <= 0) {
                                        break;
                                    }
                                }
                            }
                        } else if (be instanceof ManaReceiver receiver && receiver.canReceiveManaFromBursts()) {

                            int manaToSend = Math.min(this.getMana(), 32000);
                            if (manaToSend > 0) {
                                receiver.receiveMana(manaToSend);
                                this.addMana(-manaToSend);
                                this.sync();
                                if (this.getMana() <= 0) {
                                    break;
                                }
                            }
                        }
                    }
                    if (this.getMana() <= 0) {
                        break;
                    }
                }
                if (this.getMana() <= 0) {
                    break;
                }
            }
        }

        double particleChance = 1F - (double) this.getMana() / (double) this.getMaxMana() / 3.5F;

        if (Math.random() > particleChance) {
            long gameTime = this.level.getGameTime();
            float hue = (gameTime % 100) / 100.0f;

            int color = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f);
            float red = (color >> 16 & 0xFF) / 255F;
            float green = (color >> 8 & 0xFF) / 255F;
            float blue = (color & 0xFF) / 255F;

            Vec3 offset = this.level.getBlockState(this.getBlockPos()).getOffset(this.level, this.getBlockPos());
            double x = this.getBlockPos().getX() + offset.x;
            double y = this.getBlockPos().getY() + offset.y;
            double z = this.getBlockPos().getZ() + offset.z;
            BotaniaAPI.instance().sparkleFX(this.level, x + 0.3 + Math.random() * 0.5, y + 0.5 + Math.random() * 0.5, z + 0.3 + Math.random() * 0.5, red, green, blue, (float) Math.random(), 5);
        }
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return Rectangle.square(this.getBlockPos(), RANGE);
    }

    @Override
    public int getMaxMana() {
        return 2147483647;
    }

    @Override
    public int getColor() {
        long gameTime = this.level != null ? this.level.getGameTime() : 0;
        float hue = (gameTime % 100) / 100.0f;
        return Color.HSBtoRGB(hue, 1.0f, 1.0f) & 0xFFFFFF;
    }

    @Override
    public boolean isOvergrowthAffected() {
        return true;
    }
}
