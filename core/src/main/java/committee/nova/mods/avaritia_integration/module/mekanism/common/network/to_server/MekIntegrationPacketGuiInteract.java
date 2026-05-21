package committee.nova.mods.avaritia_integration.module.mekanism.common.network.to_server;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.factory.TileEntityMIFactory;
import io.netty.buffer.ByteBuf;
import mekanism.api.functions.TriConsumer;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public class MekIntegrationPacketGuiInteract implements IMekanismPacket {

    public static final CustomPacketPayload.Type<MekIntegrationPacketGuiInteract> TYPE = new CustomPacketPayload.Type<>(AvaritiaIntegration.rl("mek_integration_gui_interact"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MekIntegrationPacketGuiInteract> STREAM_CODEC = StreamCodec.composite(
            Type.STREAM_CODEC, packet -> packet.interactionType,
            MekIntegrationGuiInteraction.STREAM_CODEC, packet -> packet.interaction,
            BlockPos.STREAM_CODEC, packet -> packet.tilePosition,
            ByteBufCodecs.VAR_INT, packet -> packet.extra,
            MekIntegrationPacketGuiInteract::new
    );

    private final Type interactionType;

    private final MekIntegrationGuiInteraction interaction;
    private final BlockPos tilePosition;
    private final int extra;

    public MekIntegrationPacketGuiInteract(MekIntegrationGuiInteraction interaction, BlockEntity tile) {
        this(interaction, tile.getBlockPos());
    }

    public MekIntegrationPacketGuiInteract(MekIntegrationGuiInteraction interaction, BlockPos tilePosition) {
        this(interaction, tilePosition, 0);
    }

    public MekIntegrationPacketGuiInteract(MekIntegrationGuiInteraction interaction, BlockPos tilePosition, int extra) {
        this(Type.INT, interaction, tilePosition, extra);
    }

    private MekIntegrationPacketGuiInteract(Type interactionType, MekIntegrationGuiInteraction interaction, BlockPos tilePosition, int extra) {
        this.interactionType = interactionType;
        this.interaction = interaction;
        this.tilePosition = tilePosition;
        this.extra = extra;
    }

    @NotNull
    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        TileEntityMekanism tile = WorldUtils.getTileEntity(TileEntityMekanism.class, player.level(), tilePosition);
        if (tile != null && interactionType == Type.INT) {
            interaction.consume(tile, player, extra);
        }
    }

    public enum MekIntegrationGuiInteraction {
        AUTO_SORT_BUTTON((tile, player, extra) -> {
            if (tile instanceof TileEntityMIFactory<?> factory) {
                factory.toggleSorting();
            }
        });

        public static final IntFunction<MekIntegrationGuiInteraction> BY_ID = ByIdMap.continuous(MekIntegrationGuiInteraction::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        public static final StreamCodec<ByteBuf, MekIntegrationGuiInteraction> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, MekIntegrationGuiInteraction::ordinal);

        private final TriConsumer<TileEntityMekanism, Player, Integer> consumerForTile;

        MekIntegrationGuiInteraction(TriConsumer<TileEntityMekanism, Player, Integer> consumerForTile) {
            this.consumerForTile = consumerForTile;
        }

        public void consume(TileEntityMekanism tile, Player player, int extra) {
            consumerForTile.accept(tile, player, extra);
        }
    }

    private enum Type {
        INT;

        public static final IntFunction<Type> BY_ID = ByIdMap.continuous(Type::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Type::ordinal);
    }
}
