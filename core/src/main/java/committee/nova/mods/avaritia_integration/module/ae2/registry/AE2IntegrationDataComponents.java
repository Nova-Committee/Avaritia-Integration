package committee.nova.mods.avaritia_integration.module.ae2.registry;

import appeng.api.stacks.GenericStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class AE2IntegrationDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, AvaritiaIntegration.MOD_ID);

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(
            String name, UnaryOperator<DataComponentType.Builder<T>> builder
    ) {
        return DATA_COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }

    // 已用字节
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> CELL_BYTES_USAGE =
            register("cell_bytes_usage", b -> b
                    .persistent(Codec.LONG)
                    .networkSynchronized(ByteBufCodecs.VAR_LONG)
                    .cacheEncoding()
            );

    // 已用字节-BigInteger版
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BigInteger>> CELL_BYTE_USAGE_BIG =
            register("cell_byte_usage_big", b -> b
                    .persistent(Codec.STRING.comapFlatMap(
                            s -> {
                                try {
                                    return DataResult.success(new BigInteger(s));
                                } catch (NumberFormatException e) {
                                    return DataResult.error(() -> "Invalid BigInteger: " + s);
                                }
                            },
                            BigInteger::toString
                    ))
                    .networkSynchronized(StreamCodec.of(
                            (RegistryFriendlyByteBuf buf, BigInteger v) -> {
                                byte[] arr = v.toByteArray();
                                buf.writeByteArray(arr);
                            },
                            (RegistryFriendlyByteBuf buf) -> {
                                byte[] arr = buf.readByteArray();
                                return new BigInteger(arr);
                            }
                    ))
            );

    // 已用类型
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CELL_TYPES_USAGE =
            register("cell_types_usage", b -> b
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT) // 亦可省略
                    .cacheEncoding()
            );

    // 单元状态（字符串存枚举名）
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> CELL_STATE =
            register("cell_state", b -> b
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                    .cacheEncoding()
            );

    // Tooltip 展示栈：持久化用 AE 的容错 List<GenericStack>，网络用内置 list 编解码 + AE 的 STREAM_CODEC
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<GenericStack>>> CELL_SHOW_TOOLTIP_STACKS =
            register("cell_show_tooltip_stacks", b -> b
                    .persistent(GenericStack.FAULT_TOLERANT_LIST_CODEC)
                    .networkSynchronized(
                            ByteBufCodecs.collection(
                                    ArrayList::new,
                                    GenericStack.STREAM_CODEC
                            )
                    )
                    .cacheEncoding()
            );

    // 组件唯一仓库 UUID
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> CELL_UUID =
            register("cell_uuid", b -> b
                    .persistent(UUIDUtil.CODEC)
                    .networkSynchronized(UUIDUtil.STREAM_CODEC)
                    .cacheEncoding()
            );

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}
