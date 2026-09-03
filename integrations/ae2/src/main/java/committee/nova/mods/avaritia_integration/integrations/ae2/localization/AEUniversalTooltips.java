package committee.nova.mods.avaritia_integration.integrations.ae2.localization;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import appeng.core.localization.GuiText;

import java.math.BigInteger;

import static appeng.core.localization.Tooltips.GREEN;
import static appeng.core.localization.Tooltips.colorFromRatio;
import static appeng.core.localization.Tooltips.of;
import static appeng.core.localization.Tooltips.ofUnformattedNumber;
import static appeng.core.localization.Tooltips.ofUnformattedNumberWithRatioColor;

/**
 * 在原有的bytesUsed和typesUsed添加了无限字节的字符支持
 */
public class AEUniversalTooltips {

    public static Component bytesUsed(long bytes, long max) {
        if (max <= 0) {
            // 无限：当前用量按比例着色时取 0（更偏绿），上限显示为绿色“∞”
            MutableComponent inf = Component.literal("∞").withStyle(GREEN);
            return of(GuiText.BytesUsed,
                    of(
                            ofUnformattedNumberWithRatioColor(bytes, 0.0, false),
                            of(" "),
                            of(GuiText.Of),
                            of(" "),
                            inf));
        }

        return of(GuiText.BytesUsed,
                of(
                        ofUnformattedNumberWithRatioColor(bytes, (double) bytes / (double) max, false),
                        of(" "),
                        of(GuiText.Of),
                        of(" "),
                        ofUnformattedNumber(max)));
    }

    public static Component bytesUsed(BigInteger bytes, long max) {
        MutableComponent bytesString = Component.literal(bytes.toString()).withStyle(GREEN)
                .withStyle(colorFromRatio(0.0, false));

        if (max <= 0) {
            MutableComponent inf = Component.literal("∞").withStyle(GREEN);
            return of(GuiText.BytesUsed,
                    of(
                            bytesString,
                            of(" "),
                            of(GuiText.Of),
                            of(" "),
                            inf));
        }

        return of(GuiText.BytesUsed,
                of(
                        bytesString,
                        of(" "),
                        of(GuiText.Of),
                        of(" "),
                        ofUnformattedNumber(max)));
    }

    public static Component typesUsed(long types, long max) {
        if (max <= 0) {
            // 无限：当前类型数按比例着色时取 0（更偏绿），上限显示为绿色“∞”
            MutableComponent inf = Component.literal("∞").withStyle(GREEN);
            return of(
                    ofUnformattedNumberWithRatioColor(types, 0.0, false),
                    of(" "),
                    of(GuiText.Of),
                    of(" "),
                    inf,
                    of(" "),
                    of(GuiText.Types));
        }

        return of(
                ofUnformattedNumberWithRatioColor(types, (double) types / (double) max, false),
                of(" "),
                of(GuiText.Of),
                of(" "),
                ofUnformattedNumber(max),
                of(" "),
                of(GuiText.Types));
    }
}
