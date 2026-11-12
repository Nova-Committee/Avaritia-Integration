package committee.nova.mods.avaritia_integration.module.botania.entity;


import com.google.common.base.Predicates;
import com.mojang.blaze3d.systems.RenderSystem;
import committee.nova.mods.avaritia_integration.module.botania.block.InfinityManaPoolBlock;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlockEntities;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.ManaDissolvable;
import vazkii.botania.api.mana.*;
import vazkii.botania.api.mana.spark.ManaSpark;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.api.recipe.ManaInfusionRecipe;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntity;
import vazkii.botania.common.block.block_entity.mana.BellowsBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ThrottledPacket;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.handler.ManaNetworkHandler;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.ManaTabletItem;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfinityManaPoolBlockEntity extends BotaniaBlockEntity implements ManaPool, KeyLocked, SparkAttachable, ThrottledPacket, Wandable {
    private boolean outputting = false;
    private Optional<DyeColor> legacyColor = Optional.empty();
    public int mana;
    public int manaCap = -1;
    private int soundTicks = 0;
    private boolean canAccept = true;
    private boolean canSpare = true;
    boolean isDoingTransfer = false;
    int ticksDoingTransfer = 0;
    private String inputKey = "";
    private int ticks = 0;
    private boolean sendPacket = false;
    private final Int2ObjectMap<MutableInt> chargingParticles = new Int2ObjectOpenHashMap();
    private final Int2ObjectMap<MutableInt> drainingParticles = new Int2ObjectOpenHashMap();

    public InfinityManaPoolBlockEntity(BlockPos pos, BlockState state) {
        super(BotaniaIntegrationBlockEntities.INFINITY_MANA_POOL.get(), pos, state);
    }

    public boolean isFull() {
        BlockState stateBelow = this.level.getBlockState(this.worldPosition.below());
        return !stateBelow.is(BotaniaBlocks.manaVoid) && this.getCurrentMana() >= this.getMaxMana();
    }

    public void receiveMana(int mana) {
        int old = this.mana;
        this.mana = (int) Math.max(0L, Math.min((long) this.getCurrentMana() + mana, this.getMaxMana()));
        if (old != this.mana) {
            this.setChanged();
            this.markDispatchable();
        }

    }

    public void setRemoved() {
        super.setRemoved();
        BotaniaAPI.instance().getManaNetworkInstance().fireManaNetworkEvent(this, ManaBlockType.POOL, ManaNetworkAction.REMOVE);
    }

    public static int calculateComparatorLevel(int mana, int max) {
        int val = (int) ((double) mana / (double) max * (double) 15.0F);
        if (mana > 0) {
            val = Math.max(val, 1);
        }

        return val;
    }

    public ManaInfusionRecipe getMatchingRecipe(@NotNull ItemStack stack, @NotNull BlockState state) {
        List<ManaInfusionRecipe> matchingNonCatRecipes = new ArrayList();
        List<ManaInfusionRecipe> matchingCatRecipes = new ArrayList();

        for (ManaInfusionRecipe recipe : BotaniaRecipeTypes.getRecipes(this.level, BotaniaRecipeTypes.MANA_INFUSION_TYPE).values()) {
            if (recipe.matches(stack)) {
                if (recipe.getRecipeCatalyst() == null) {
                    matchingNonCatRecipes.add(recipe);
                } else if (recipe.getRecipeCatalyst().test(state)) {
                    matchingCatRecipes.add(recipe);
                }
            }
        }

        return !matchingCatRecipes.isEmpty() ? (ManaInfusionRecipe) matchingCatRecipes.get(0) : (!matchingNonCatRecipes.isEmpty() ? (ManaInfusionRecipe) matchingNonCatRecipes.get(0) : null);
    }

    public boolean collideEntityItem(ItemEntity item) {
        if (!this.level.isClientSide && item.isAlive() && !item.getItem().isEmpty()) {
            ItemStack stack = item.getItem();
            Item manaItem = stack.getItem();
            if (manaItem instanceof ManaDissolvable) {
                ManaDissolvable dissolvable = (ManaDissolvable) manaItem;
                dissolvable.onDissolveTick(this, item);
            }

            if (XplatAbstractions.INSTANCE.itemFlagsComponent(item).manaInfusionSpawned) {
                return false;
            } else {
                ManaInfusionRecipe recipe = this.getMatchingRecipe(stack, this.level.getBlockState(this.worldPosition.below()));
                if (recipe != null) {
                    int mana = recipe.getManaToConsume();
                    if (this.getCurrentMana() >= mana) {
                        this.receiveMana(-mana);
                        ItemStack output = recipe.getRecipeOutput(this.level.registryAccess(), stack);
                        EntityHelper.shrinkItem(item);
                        item.setOnGround(false);
                        ItemEntity outputItem = new ItemEntity(this.level, (double) this.worldPosition.getX() + (double) 0.5F, (double) this.worldPosition.getY() + (double) 1.5F, (double) this.worldPosition.getZ() + (double) 0.5F, output);
                        XplatAbstractions.INSTANCE.itemFlagsComponent(outputItem).manaInfusionSpawned = true;
                        Entity var8 = item.getOwner();
                        if (var8 instanceof Player) {
                            Player player = (Player) var8;
                            player.triggerRecipeCrafted(recipe, List.of(output));
                            output.onCraftedBy(this.level, player, output.getCount());
                        }

                        this.level.addFreshEntity(outputItem);
                        this.craftingEffect(true);
                        return true;
                    }
                }

                return false;
            }
        } else {
            return false;
        }
    }

    public void craftingEffect(boolean playSound) {
        if (playSound && this.soundTicks == 0) {
            this.level.playSound((Player) null, this.worldPosition, BotaniaSounds.manaPoolCraft, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.soundTicks = 6;
        }

        this.level.gameEvent((Entity) null, GameEvent.BLOCK_ACTIVATE, this.getBlockPos());
        this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 0, 0);
    }

    public boolean triggerEvent(int event, int param) {
        switch (event) {
            case 0:
                if (this.level.isClientSide) {
                    for (int i = 0; i < 25; ++i) {
                        float red = (float) Math.random();
                        float green = (float) Math.random();
                        float blue = (float) Math.random();
                        SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 10);
                        this.level.addParticle(data, (double) this.worldPosition.getX() + (double) 0.5F + Math.random() * 0.4 - 0.2, (double) this.worldPosition.getY() + (double) 0.75F, (double) this.worldPosition.getZ() + (double) 0.5F + Math.random() * 0.4 - 0.2, (double) 0.0F, (double) 0.0F, (double) 0.0F);
                    }
                }

                return true;
            case 1:
                if (this.level.isClientSide && BotaniaConfig.common().chargingAnimationEnabled()) {
                    ((MutableInt) this.chargingParticles.computeIfAbsent(param, (ix) -> new MutableInt(15))).setValue(15);
                }

                return true;
            case 2:
                if (this.level.isClientSide && BotaniaConfig.common().chargingAnimationEnabled()) {
                    ((MutableInt) this.drainingParticles.computeIfAbsent(param, (ix) -> new MutableInt(15))).setValue(15);
                }

                return true;
            default:
                return super.triggerEvent(event, param);
        }
    }

    public void initManaCapAndNetwork() {
        if (getMaxMana() == -1) {
            this.manaCap = Integer.MAX_VALUE;
        }
        if (!ManaNetworkHandler.instance.isPoolIn(level, this) && !isRemoved()) {
            BotaniaAPI.instance().getManaNetworkInstance().fireManaNetworkEvent(this, ManaBlockType.POOL, ManaNetworkAction.ADD);
        }
    }

    public static void clientTick(Level level, BlockPos worldPosition, BlockState state, InfinityManaPoolBlockEntity self) {
        self.initManaCapAndNetwork();
        double particleChance = (double) 1.0F - (double) self.getCurrentMana() / (double) self.getMaxMana() * 0.1;
        if (Math.random() > particleChance) {
            WispParticleData data = WispParticleData.wisp((float) Math.random() / 3.0F, 0.0F, 0.7764706F, 1.0F, 2.0F);
            level.addParticle(data, (double) worldPosition.getX() + 0.3 + Math.random() * (double) 0.5F, (double) worldPosition.getY() + 0.6 + Math.random() * (double) 0.25F, (double) worldPosition.getZ() + Math.random(), (double) 0.0F, (double) ((float) Math.random() / 25.0F), (double) 0.0F);
        }

        if (self.getCurrentMana() == 0) {
            self.chargingParticles.clear();
        } else {
            displayChargingParticles(level, worldPosition, self, self.chargingParticles, true);
        }

        displayChargingParticles(level, worldPosition, self, self.drainingParticles, false);
    }

    private static void displayChargingParticles(Level level, BlockPos worldPosition, InfinityManaPoolBlockEntity self, Int2ObjectMap<MutableInt> particles, boolean charging) {
        int bellowCount = charging ? getBellowCount(level, worldPosition, self) : 0;
        float relativeMana = (float) self.getCurrentMana() / (float) self.getMaxMana();
        ObjectIterator<Int2ObjectMap.Entry<MutableInt>> particlesIterator = particles.int2ObjectEntrySet().iterator();

        while (particlesIterator.hasNext()) {
            Int2ObjectMap.Entry<MutableInt> entry = (Int2ObjectMap.Entry) particlesIterator.next();
            int ticksRemaining = ((MutableInt) entry.getValue()).decrementAndGet();
            if (ticksRemaining % 2 == 0) {
                int encodedPos = entry.getIntKey();
                Vec3 itemPosRelBase = decodeRelativeItemPosition(encodedPos, relativeMana);
                if (charging) {
                    for (int i = 0; i <= bellowCount; ++i) {
                        Vec3 itemPosRel = randomizeItemPos(itemPosRelBase);
                        Vec3 poolPosRel = new Vec3(0.1 + 0.8 * Math.random(), 0.1 + 0.4 * (double) relativeMana, 0.1 + 0.8 * Math.random());
                        addManaFlowParticle(level, worldPosition, poolPosRel, itemPosRel);
                    }
                } else {
                    Vec3 itemPosRel = randomizeItemPos(itemPosRelBase);
                    Vec3 poolPosRel = new Vec3(0.05 + 0.9 * Math.random(), 0.35 * (double) relativeMana, 0.05 + 0.9 * Math.random());
                    addManaFlowParticle(level, worldPosition, itemPosRel, poolPosRel);
                }
            }

            if (ticksRemaining <= 0) {
                particlesIterator.remove();
            }
        }

    }

    private static @NotNull Vec3 randomizeItemPos(Vec3 itemPosRelBase) {
        return itemPosRelBase.add(0.1 * Math.random() - 0.05, 0.1 * Math.random() + (double) 0.25F, 0.1 * Math.random() - 0.05);
    }

    private static int getBellowCount(Level level, BlockPos worldPosition, InfinityManaPoolBlockEntity self) {
        int bellowCount = 0;

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockEntity tile = level.getBlockEntity(worldPosition.relative(dir));
            if (tile instanceof BellowsBlockEntity bellows) {
                if (bellows.getLinkedTile() == self) {
                    ++bellowCount;
                }
            }
        }

        return bellowCount;
    }

    private static void addManaFlowParticle(Level level, BlockPos worldPosition, Vec3 startPos, Vec3 endPos) {
        double maxHeight = Math.max(startPos.y, endPos.y) - endPos.y + 0.05 * Math.random();
        Vec3 horizontalDiff = new Vec3(endPos.x - startPos.x, (double) 0.0F, endPos.z - startPos.z);
        double horizontalDistance = horizontalDiff.horizontalDistance();
        Vec3 horizontalDir = horizontalDiff.scale((double) 1.0F / horizontalDistance);
        double startHeight = startPos.y - endPos.y;
        double vY0Squared = (double) 0.006F * (maxHeight - startHeight);
        double vY0 = Math.sqrt(vY0Squared);
        double lifetime = (vY0 + Math.sqrt(vY0Squared + (double) 0.006F * startHeight)) / (double) 0.003F;
        double vX0 = horizontalDistance / lifetime;
        Vec3 v0 = horizontalDir.scale(vX0).with(Direction.Axis.Y, vY0);
        WispParticleData data = WispParticleData.wisp(0.1F, 0.0F, 0.7764706F, 1.0F, (float) (0.025 * lifetime), 0.003F).withNoClip(true);
        level.addParticle(data, (double) worldPosition.getX() + startPos.x, (double) worldPosition.getY() + startPos.y, (double) worldPosition.getZ() + startPos.z, v0.x, v0.y, v0.z);
    }

    public static void serverTick(Level level, BlockPos worldPosition, BlockState state, InfinityManaPoolBlockEntity self) {
        if (self.legacyColor.isPresent()) {
            self.setColor(self.legacyColor);
            self.legacyColor = Optional.empty();
        }

        self.initManaCapAndNetwork();
        boolean wasDoingTransfer = self.isDoingTransfer;
        self.isDoingTransfer = false;
        if (self.soundTicks > 0) {
            --self.soundTicks;
        }

        if (self.sendPacket && self.ticks % 10 == 0) {
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(self);
            self.sendPacket = false;
        }

        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, new AABB(worldPosition, worldPosition.offset(1, 1, 1)))) {
            if (item.isAlive()) {
                ItemStack stack = item.getItem();
                ManaItem mana = XplatAbstractions.INSTANCE.findManaItem(stack);
                if (!stack.isEmpty() && mana != null && (self.outputting && mana.canReceiveManaFromPool(self) || !self.outputting && mana.canExportManaToPool(self))) {
                    boolean didSomething = false;
                    int transfRate = Integer.MAX_VALUE;
                    if (self.outputting) {
                        if (self.canSpare) {
                            if (self.getCurrentMana() > 0 && mana.getMana() < mana.getMaxMana()) {
                                didSomething = true;
                            }

                            int manaVal = Math.min(transfRate, Math.min(self.getCurrentMana(), mana.getMaxMana() - mana.getMana()));
                            mana.addMana(manaVal);
                            self.receiveMana(-manaVal);
                        }
                    } else if (self.canAccept) {
                        if (mana.getMana() > 0 && !self.isFull()) {
                            didSomething = true;
                        }

                        int manaVal = Math.min(transfRate, Math.min(self.getMaxMana() - self.getCurrentMana(), mana.getMana()));
                        if (manaVal == 0 && self.level.getBlockState(worldPosition.below()).is(BotaniaBlocks.manaVoid)) {
                            manaVal = Math.min(transfRate, mana.getMana());
                        }

                        mana.addMana(-manaVal);
                        self.receiveMana(manaVal);
                    }

                    if (didSomething) {
                        if (BotaniaConfig.common().chargingAnimationEnabled() && self.ticks % 10 == 0) {
                            level.blockEvent(worldPosition, state.getBlock(), self.outputting ? 1 : 2, encodeRelativeItemPosition(worldPosition, item));
                        }

                        EntityHelper.syncItem(item);
                        self.isDoingTransfer = self.outputting;
                    }
                }
            }
        }

        if (self.isDoingTransfer) {
            ++self.ticksDoingTransfer;
        } else {
            self.ticksDoingTransfer = 0;
            if (wasDoingTransfer) {
                VanillaPacketDispatcher.dispatchTEToNearbyPlayers(self);
            }
        }

        ++self.ticks;
    }

    private static int encodeRelativeItemPosition(BlockPos worldPosition, ItemEntity item) {
        double relX = Mth.clamp(item.position().x() - (double) worldPosition.getX(), (double) 0.0F, (double) 1.0F);
        double relY = Mth.clamp((double) 0.125F + (double) 0.875F * (item.position().y() - (double) worldPosition.getY()), (double) 0.125F, 0.9);
        double relZ = Mth.clamp(item.position().z() - (double) worldPosition.getZ(), (double) 0.0F, (double) 1.0F);
        int compressedX = (int) Math.round((double) 7.0F * relX);
        int compressedY = 4 - Mth.ceillog2(14 - (int) ((double) 14.0F * relY));
        int compressedZ = (int) Math.round((double) 7.0F * relZ);
        return compressedX | compressedY << 3 | compressedZ << 5;
    }

    private static Vec3 decodeRelativeItemPosition(int param, float relativeMana) {
        int compressedX = param & 7;
        int compressedY = param >> 3 & 3;
        int compressedZ = param >> 5 & 7;
        double relX = (double) compressedX / (double) 7.0F;
        double relY = (double) 1.0F - (double) 0.875F / (double) (1 << compressedY);
        double relZ = (double) compressedZ / (double) 7.0F;
        return new Vec3(relX, Math.max(relY, (double) 0.5F * (double) relativeMana), relZ);
    }

    public void writePacketNBT(CompoundTag cmp) {
        cmp.putInt("mana", this.getCurrentMana());
        cmp.putBoolean("outputting", this.outputting);
        cmp.putInt("manaCap", this.getMaxMana());
        cmp.putBoolean("canAccept", this.canAccept);
        cmp.putBoolean("canSpare", this.canSpare);
        cmp.putString("inputKey", this.inputKey);
        cmp.putString("outputKey", "");
    }

    public void readPacketNBT(CompoundTag cmp) {
        this.mana = cmp.getInt("mana");
        this.outputting = cmp.getBoolean("outputting");
        if (cmp.contains("color")) {
            DyeColor color = DyeColor.byId(cmp.getInt("color"));
            if (color != DyeColor.WHITE) {
                this.legacyColor = Optional.of(color);
            } else {
                this.legacyColor = Optional.empty();
            }
        }

        if (cmp.contains("manaCap")) {
            this.manaCap = cmp.getInt("manaCap");
        }

        if (cmp.contains("canAccept")) {
            this.canAccept = cmp.getBoolean("canAccept");
        }

        if (cmp.contains("canSpare")) {
            this.canSpare = cmp.getBoolean("canSpare");
        }

        if (cmp.contains("inputKey")) {
            this.inputKey = cmp.getString("inputKey");
        }

        if (cmp.contains("outputKey")) {
            this.inputKey = cmp.getString("outputKey");
        }

    }

    public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
        if (player == null || player.isShiftKeyDown()) {
            this.outputting = !this.outputting;
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
        }

        return true;
    }

    public boolean canReceiveManaFromBursts() {
        return true;
    }

    public boolean isOutputtingPower() {
        return this.outputting;
    }

    public Level getManaReceiverLevel() {
        return this.getLevel();
    }

    public BlockPos getManaReceiverPos() {
        return this.getBlockPos();
    }

    public int getCurrentMana() {
        if (getBlockState().getBlock() instanceof InfinityManaPoolBlock) {
            return this.mana;
        }
        return 0;
    }

    public int getMaxMana() {
        return this.manaCap;
    }

    public String getInputKey() {
        return this.inputKey;
    }

    public String getOutputKey() {
        return "";
    }

    public boolean canAttachSpark(ItemStack stack) {
        return true;
    }

    public ManaSpark getAttachedSpark() {
        List<Entity> sparks = this.level.getEntitiesOfClass(Entity.class, new AABB(this.worldPosition.above(), this.worldPosition.above().offset(1, 1, 1)), Predicates.instanceOf(ManaSpark.class));
        if (sparks.size() == 1) {
            Entity e = sparks.get(0);
            return (ManaSpark) e;
        } else {
            return null;
        }
    }

    public boolean areIncomingTranfersDone() {
        return false;
    }

    public int getAvailableSpaceForMana() {
        int space = Math.max(0, this.getMaxMana() - this.getCurrentMana());
        if (space > 0) {
            return space;
        } else {
            return this.level.getBlockState(this.worldPosition.below()).is(BotaniaBlocks.manaVoid) ? this.getMaxMana() : 0;
        }
    }

    public Optional<DyeColor> getColor() {
        return this.getBlockState().getValue(BotaniaStateProperties.OPTIONAL_DYE_COLOR).toDyeColor();
    }

    public void setColor(Optional<DyeColor> color) {
        this.level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(BotaniaStateProperties.OPTIONAL_DYE_COLOR, BotaniaStateProperties.OptionalDyeColor.fromOptionalDyeColor(color)));
    }

    public void markDispatchable() {
        this.sendPacket = true;
    }

    public static class WandHud implements WandHUD {
        private final InfinityManaPoolBlockEntity pool;

        public WandHud(InfinityManaPoolBlockEntity pool) {
            this.pool = pool;
        }

        public void renderHUD(GuiGraphics gui, Minecraft mc) {
            ItemStack poolStack = new ItemStack(this.pool.getBlockState().getBlock());
            String name = poolStack.getHoverName().getString();
            int centerX = mc.getWindow().getGuiScaledWidth() / 2;
            int centerY = mc.getWindow().getGuiScaledHeight() / 2;
            int width = Math.max(102, mc.font.width(name)) + 4;
            RenderHelper.renderHUDBox(gui, centerX - width / 2, centerY + 8, centerX + width / 2, centerY + 48);
            BotaniaAPIClient.instance().drawSimpleManaHUD(gui, 38399, this.pool.getCurrentMana(), this.pool.getMaxMana(), name);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(770, 771);
            int arrowU = this.pool.outputting ? 22 : 0;
            int arrowV = 38;
            RenderHelper.drawTexturedModalRect(gui, HUDHandler.manaBar, centerX - 11, centerY + 30, arrowU, arrowV, 22, 15);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            ItemStack tablet = new ItemStack(BotaniaItems.manaTablet);
            ManaTabletItem.setStackCreative(tablet);
            gui.renderItem(tablet, centerX - 31, centerY + 30);
            gui.renderItem(poolStack, centerX + 15, centerY + 30);
            RenderSystem.disableBlend();
        }
    }
}

