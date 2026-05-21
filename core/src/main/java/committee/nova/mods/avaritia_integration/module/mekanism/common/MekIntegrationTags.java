package committee.nova.mods.avaritia_integration.module.mekanism.common;

import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class MekIntegrationTags {

    private MekIntegrationTags() {}

    public static final class Items {
        public static final TagKey<Item> ALLOYS = mekanismItem("alloys");
        public static final TagKey<Item> ALLOYS_INFINITY = mekanismItem("alloys/infinity");
        public static final TagKey<Item> ALLOYS_NEUTRON = mekanismItem("alloys/neutron");
        public static final TagKey<Item> ENRICHED = mekanismItem("enriched");
        public static final TagKey<Item> ENRICHED_INFINITY = mekanismItem("enriched/infinity");
        public static final TagKey<Item> ENRICHED_NEUTRON = mekanismItem("enriched/neutron");

        private Items() {}
    }

    public static final class Chemicals {
        public static final TagKey<Chemical> INFINITY = mekanismChemical("infinity");
        public static final TagKey<Chemical> NEUTRON = mekanismChemical("neutron");

        private Chemicals() {}
    }

    private static TagKey<Item> mekanismItem(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MekanismAPI.MEKANISM_MODID, path));
    }

    private static TagKey<Chemical> mekanismChemical(String path) {
        return TagKey.create(MekanismAPI.CHEMICAL_REGISTRY_NAME, ResourceLocation.fromNamespaceAndPath(MekanismAPI.MEKANISM_MODID, path));
    }
}
