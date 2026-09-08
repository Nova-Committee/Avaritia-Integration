package committee.nova.mods.avaritia_integration.integrations.create.content.recipe;

import com.mojang.serialization.Codec;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_burner.ExtremeBlazeBurnerBlock;

import net.createmod.catnip.lang.Lang;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import io.netty.buffer.ByteBuf;

public enum ExtremeHeatCondition implements StringRepresentable {
    NORMAL(0xffffff),
    BLAZE(0xE88300),
    STAR(0x5C93E8);

    public static final Codec<ExtremeHeatCondition> CODEC = StringRepresentable.fromEnum(ExtremeHeatCondition::values);
    public static final StreamCodec<ByteBuf, ExtremeHeatCondition> STREAM_CODEC = ByteBufCodecs
            .idMapper(i -> values()[i], ExtremeHeatCondition::ordinal);

    private final int color;

    ExtremeHeatCondition(int color) {
        this.color = color;
    }

    public boolean testBlazeBurner(ExtremeBlazeBurnerBlock.ExtremeHeatLevel level) {
        if (this == STAR) {
            return level == ExtremeBlazeBurnerBlock.ExtremeHeatLevel.STAR;
        }
        if (this == BLAZE) {
            return level != ExtremeBlazeBurnerBlock.ExtremeHeatLevel.SMOULDERING;
        }
        return true;
    }

    public ExtremeBlazeBurnerBlock.ExtremeHeatLevel visualizeAsBlazeBurner() {
        if (this == STAR) {
            return ExtremeBlazeBurnerBlock.ExtremeHeatLevel.STAR;
        }
        if (this == BLAZE) {
            return ExtremeBlazeBurnerBlock.ExtremeHeatLevel.BLAZE;
        }
        return ExtremeBlazeBurnerBlock.ExtremeHeatLevel.SMOULDERING;
    }

    @Override
    public String getSerializedName() {
        return Lang.asId(name());
    }

    public String serialize() {
        return getSerializedName();
    }

    public String getTranslationKey() {
        return "recipe.extreme_heat_requirement." + serialize();
    }

    public static ExtremeHeatCondition deserialize(String name) {
        for (ExtremeHeatCondition condition : values()) {
            if (condition.serialize().equals(name)) {
                return condition;
            }
        }
        AvaritiaIntegration.LOGGER.warn("Tried to deserialize invalid heat condition: \"{}\"", name);
        return NORMAL;
    }

    public int getColor() {
        return color;
    }
}
