package committee.nova.mods.avaritia_integration.common.net;

import committee.nova.mods.avaritia_integration.util.SwordUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientEntityRemovePacket {
    public final int uuid;
    public final int player;

    public ClientEntityRemovePacket(int uuid, int player) {
        this.uuid = uuid;
        this.player = player;
    }

    public ClientEntityRemovePacket(FriendlyByteBuf buffer) {
        this.uuid = buffer.readInt();
        this.player = buffer.readInt();
    }

    public static void encode(ClientEntityRemovePacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.uuid);
        buf.writeInt(msg.player);
    }

    public static void handle(ClientEntityRemovePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->  {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                h(msg);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void h(ClientEntityRemovePacket msg) {
        if (Minecraft.getInstance().level != null) {
            Entity entity = Minecraft.getInstance().level.getEntity(msg.uuid);
            Entity player = Minecraft.getInstance().level.getEntity(msg.player);
            if (entity instanceof LivingEntity living && player instanceof Player player1)
                SwordUtil.killEntity(living, player1);
        }
    }

}