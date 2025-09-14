package committee.nova.mods.avaritia_integration.common.blockentity;

import committee.nova.mods.avaritia_integration.init.registry.BotaniaReg;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Nameable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FireworkRocketItem.Shape;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.BotaniaStats;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.decor.TinyPotatoBlock;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class InfinityTinyPotatoBlockEntity extends BlockEntity implements Nameable {
    private static final ResourceLocation BIRTHDAY_ADVANCEMENT = ResourceLocationHelper.prefix("challenge/tiny_potato_birthday");
    private static final boolean IS_BIRTHDAY = isTinyPotatoBirthday();
    private static final String TAG_NAME = "name";
    private static final int JUMP_EVENT = 0;
    private static final Map<String, String> GENDER = new HashMap(Map.ofEntries(Map.entry("girlstater", "daughter"), Map.entry("lesbiabtater", "daughter"), Map.entry("lesbiamtater", "daughter"), Map.entry("lesbiantater", "daughter"), Map.entry("lesbitater", "daughter"), Map.entry("lessbientater", "daughter"), Map.entry("agendertater", "child"), Map.entry("enbytater", "child"), Map.entry("nbtater", "child"), Map.entry("nonbinarytater", "child"), Map.entry("robotater", "child"), Map.entry("wiretater", "child"), Map.entry("eutrotater", "child"), Map.entry("bob", "child"), Map.entry("snences", "child"), Map.entry("genderfluidtater", "child"), Map.entry("taterfluid", "child"), Map.entry("eggtater", "child"), Map.entry("tategg", "child"), Map.entry("transtater", "child"), Map.entry("manytater", "children"), Map.entry("pluraltater", "children"), Map.entry("snorps", "children"), Map.entry("systater", "children"), Map.entry("systemtater", "children"), Map.entry("tomater", "tomato")));
    public int jumpTicks = 0;
    public Component name = Component.literal("");
    private int nextDoIt = 0;
    private int birthdayTick = 0;
    private static final List<Block> ALL_CANDLE_CAKES;
    public InfinityTinyPotatoBlockEntity(BlockPos pos, BlockState state) {
        super(BotaniaReg.INFINITY_TINY_POTATO.get(), pos, state);
    }

    public void interact(Player player, InteractionHand hand, ItemStack stack, Direction side) {
        boolean flag = stack.isEmpty();
        if (level != null && !level.isClientSide) {
            jump();
            if (flag) {
                addEffects(level, worldPosition, player);
            }
            if (this.name.getString().toLowerCase(Locale.ROOT).trim().endsWith("shia labeouf") && this.nextDoIt == 0) {
                this.nextDoIt = 40;
                this.level.playSound(null, this.worldPosition, BotaniaSounds.doit, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            player.awardStat(BotaniaStats.TINY_POTATOES_PETTED);
            PlayerHelper.grantCriterion((ServerPlayer) player, prefix("main/tiny_potato_pet"), "code_triggered");
        }
    }

    /**
     * 给与周围所有生物3分钟2级所有正面BUFF
     * 补满玩家饥饿值
     * @param world 世界
     * @param pos 土豆坐标
     * @param player 右键的玩家
     */
    private void addEffects(Level world, BlockPos pos, Player player){
        int radius = 10;
        int time = 3600;
        int lv = 1;
        AABB bb = new AABB(pos.offset(-radius, -2, -radius), pos.offset(radius, 2, radius));
        List<LivingEntity> entityList = world.getEntitiesOfClass(LivingEntity.class, bb);
        for (LivingEntity living : entityList) {
            double sq = living.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
            if (sq < radius) {
                for (MobEffect next : ForgeRegistries.MOB_EFFECTS.getValues()) {
                    if (next.isBeneficial())
                        living.addEffect(new MobEffectInstance(next, time, lv));
                }
            }
        }

        player.getFoodData().eat(20, 30.0F);
    }

    private void jump() {
        if (this.jumpTicks == 0) {
            this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 0, 20);
        }

    }

    public boolean triggerEvent(int id, int param) {
        if (id == 0) {
            this.jumpTicks = param;
            return true;
        } else {
            return super.triggerEvent(id, param);
        }
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, InfinityTinyPotatoBlockEntity self) {
        if (self.jumpTicks > 0) {
            --self.jumpTicks;
        }

        if (!level.isClientSide) {

            if (self.nextDoIt > 0) {
                --self.nextDoIt;
            }

            if (IS_BIRTHDAY) {
                self.tickBirthday();
            }
        }

    }

    private void tickBirthday() {
        Direction facing = (Direction)this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos facingPos = this.getBlockPos().relative(facing);
        if (this.level.hasChunkAt(facingPos)) {
            BlockState facingState = this.level.getBlockState(facingPos);
            DyeColor cakeColor = getLitCakeColor(facingState, this.level.getRandom());
            List<Player> players = PlayerHelper.getRealPlayersIn(this.level, VecHelper.boxForRange(Vec3.atCenterOf(this.getBlockPos()), 8.0));
            if (cakeColor != null && !players.isEmpty()) {
                ++this.birthdayTick;
                List<Integer> messageTimes = List.of(100, 170, 240, 310, 380);
                int messageIndex = messageTimes.indexOf(this.birthdayTick);
                if (messageIndex != -1) {
                    Object[] args = messageIndex == 1 ? new Object[]{getTinyPotatoAge()} : new int[][]{new int[]{0}};
                    MutableComponent message = Component.literal("<").append(this.getDisplayName()).append("> ").append(Component.translatable("botania.tater_birthday." + messageIndex, args));
                    Iterator var10 = players.iterator();

                    while(var10.hasNext()) {
                        Player player = (Player)var10.next();
                        player.sendSystemMessage(message);
                    }

                    this.jump();
                    TinyPotatoBlock.spawnHearts((ServerLevel)this.level, this.getBlockPos());
                }

                if (messageIndex == messageTimes.size() - 1) {
                    CompoundTag explosion = new CompoundTag();
                    explosion.putByte("Type", (byte) Shape.LARGE_BALL.getId());
                    explosion.putBoolean("Flicker", true);
                    explosion.putBoolean("Trail", true);
                    explosion.putIntArray("Colors", List.of(cakeColor.getFireworkColor(), 13787301, 14987213, 16711422, 5754616));
                    ListTag explosions = new ListTag();
                    explosions.add(explosion);
                    ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
                    CompoundTag rocketFireworks = rocket.getOrCreateTagElement("Fireworks");
                    rocketFireworks.putByte("Flight", (byte)0);
                    rocketFireworks.put("Explosions", explosions);
                    this.level.addFreshEntity(new FireworkRocketEntity(this.level, (double)facingPos.getX() + 0.5, (double)facingPos.getY() + 0.5, (double)facingPos.getZ() + 0.5, rocket));
                    this.level.removeBlock(facingPos, false);
                    this.level.levelEvent(2001, facingPos, Block.getId(facingState));
                    this.level.playSound((Player)null, this.getBlockPos(), SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 1.0F, 0.5F + (float)Math.random() * 0.5F);
                    Iterator var12 = players.iterator();

                    while(var12.hasNext()) {
                        Player player = (Player)var12.next();
                        PlayerHelper.grantCriterion((ServerPlayer)player, BIRTHDAY_ADVANCEMENT, "code_triggered");
                    }
                }
            }
        }

    }

    public void setChanged() {
        super.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
        }

    }


    public @NotNull Component getName() {
        return BotaniaBlocks.tinyPotato.getName();
    }

    public @org.jetbrains.annotations.Nullable Component getCustomName() {
        return this.name.getString().isEmpty() ? null : this.name;
    }

    public @NotNull Component getDisplayName() {
        return this.hasCustomName() ? this.getCustomName() : this.getName();
    }

    private static @Nullable DyeColor getLitCakeColor(BlockState state, RandomSource rand) {
        int idx = ALL_CANDLE_CAKES.indexOf(state.getBlock());
        if (idx == -1) {
            return null;
        } else if (!(Boolean)state.getValue(CandleCakeBlock.LIT)) {
            return null;
        } else {
            return idx == 16 ? DyeColor.byId(rand.nextInt(16)) : DyeColor.byId(idx);
        }
    }

    private static boolean isTinyPotatoBirthday() {
        LocalDateTime now = LocalDateTime.now();
        return now.getMonth() == Month.JULY && now.getDayOfMonth() == 19;
    }

    private static int getTinyPotatoAge() {
        LocalDateTime now = LocalDateTime.now();
        return now.getYear() - 2014;
    }

    static {
        ALL_CANDLE_CAKES = List.of(Blocks.WHITE_CANDLE_CAKE, Blocks.ORANGE_CANDLE_CAKE, Blocks.MAGENTA_CANDLE_CAKE, Blocks.LIGHT_BLUE_CANDLE_CAKE, Blocks.YELLOW_CANDLE_CAKE, Blocks.LIME_CANDLE_CAKE, Blocks.PINK_CANDLE_CAKE, Blocks.GRAY_CANDLE_CAKE, Blocks.LIGHT_GRAY_CANDLE_CAKE, Blocks.CYAN_CANDLE_CAKE, Blocks.PURPLE_CANDLE_CAKE, Blocks.BLUE_CANDLE_CAKE, Blocks.BROWN_CANDLE_CAKE, Blocks.GREEN_CANDLE_CAKE, Blocks.RED_CANDLE_CAKE, Blocks.BLACK_CANDLE_CAKE, Blocks.CANDLE_CAKE);
    }
}