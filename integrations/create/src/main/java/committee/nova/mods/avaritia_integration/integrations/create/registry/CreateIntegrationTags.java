package committee.nova.mods.avaritia_integration.integrations.create.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CreateIntegrationTags {
    public enum ItemTags {
        BLAZE_BURNER_FUEL_BLAZE(AvaritiaIntegration.MOD_ID, "blaze_burner_fuel/blaze"),
        BLAZE_BURNER_FUEL_STAR(AvaritiaIntegration.MOD_ID, "blaze_burner_fuel/star");

        public final TagKey<Item> tag;

        ItemTags(String namespace, String pathOverride) {
            this.tag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, pathOverride));
        }

        public boolean matches(Item item) {
            return item.builtInRegistryHolder().is(this.tag);
        }

        public boolean matches(ItemStack stack) {
            return stack.is(this.tag);
        }
    }
}
