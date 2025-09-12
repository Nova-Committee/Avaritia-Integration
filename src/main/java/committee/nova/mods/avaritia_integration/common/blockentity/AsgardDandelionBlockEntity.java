package committee.nova.mods.avaritia_integration.common.blockentity;

import committee.nova.mods.avaritia_integration.init.registry.ModBlockEntities;
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

    public AsgardDandelionBlockEntity( BlockPos pos, BlockState state) {
        super(ModBlockEntities.ASGARD_DANDELION.get(), pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (level == null || level.isClientSide) return;

        addMana(2147483647);

        if (getMana() > 0) {
            for (int dx = -RANGE; dx <= RANGE; dx++) {
                for (int dy = -RANGE; dy <= RANGE; dy++) {
                    for (int dz = -RANGE; dz <= RANGE; dz++) {
                        BlockPos pos = getBlockPos().offset(dx, dy, dz);
                        BlockEntity be = level.getBlockEntity(pos);

                        if (be instanceof ManaPool pool) {
                            if (!pool.isFull()) {
                                int manaToSend = Math.min(getMana(), 32000);
                                if (manaToSend > 0) {
                                    pool.receiveMana(manaToSend);
                                    addMana(-manaToSend);
                                    sync();
                                    if (getMana() <= 0) {
                                        break;
                                    }
                                }
                            }
                        }

                        else if (be instanceof ManaReceiver receiver && receiver.canReceiveManaFromBursts()) {

                            int manaToSend = Math.min(getMana(), 32000);
                            if (manaToSend > 0) {
                                receiver.receiveMana(manaToSend);
                                addMana(-manaToSend);
                                sync();
                                if (getMana() <= 0) {
                                    break;
                                }
                            }
                        }
                    }
                    if (getMana() <= 0) {
                        break;
                    }
                }
                if (getMana() <= 0) {
                    break;
                }
            }
        }

        double particleChance = 1F - (double) getMana() / (double) getMaxMana() / 3.5F;

        if (Math.random() > particleChance) {
            long gameTime = level.getGameTime();
            float hue = (gameTime % 100) / 100.0f;

            int color = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f);
            float red = (color >> 16 & 0xFF) / 255F;
            float green = (color >> 8 & 0xFF) / 255F;
            float blue = (color & 0xFF) / 255F;

            Vec3 offset = level.getBlockState(getBlockPos()).getOffset(level, getBlockPos());
            double x = getBlockPos().getX() + offset.x;
            double y = getBlockPos().getY() + offset.y;
            double z = getBlockPos().getZ() + offset.z;
            BotaniaAPI.instance().sparkleFX(level, x + 0.3 + Math.random() * 0.5, y + 0.5 + Math.random() * 0.5, z + 0.3 + Math.random() * 0.5, red, green, blue, (float) Math.random(), 5);
        }
    }

    @Override
    public @Nullable RadiusDescriptor getRadius() {
        return Rectangle.square(getBlockPos(), RANGE);
    }

    @Override
    public int getMaxMana() {
        return 2147483647;
    }

    @Override
    public int getColor() {
        long gameTime = level != null ? level.getGameTime() : 0;
        float hue = (gameTime % 100) / 100.0f;
        return Color.HSBtoRGB(hue, 1.0f, 1.0f) & 0xFFFFFF;
    }

    @Override
    public boolean isOvergrowthAffected() {
        return true;
    }
}
