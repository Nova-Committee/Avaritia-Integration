package committee.nova.mods.avaritia_integration.integrations.mekanism.common;

import committee.nova.mods.avaritia_integration.integrations.mekanism.common.content.blocktype.MekIntegrationFactoryType;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.registries.MekIntegrationBlocks;

import net.minecraft.world.item.ItemStack;

import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.text.BooleanStateDisplay.YesNo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MekIntegrationMachineItemContainerTest {

    @Test
    void compressorRegistersItemCreatorsAndSurvivesInventoryTooltip() {
        ItemStack stack = new ItemStack(MekIntegrationBlocks.SINGULARITY_COMPRESSOR);
        assertItemCreators(stack, 3);
    }

    @Test
    void collectorRegistersItemAndChemicalCreatorsAndSurvivesInventoryTooltip() {
        ItemStack stack = new ItemStack(MekIntegrationBlocks.NEUTRON_COLLECTOR);
        assertChemicalCreators(stack, 1);
        assertItemCreators(stack, 3);
    }

    @Test
    void compressingFactoryRegistersItemCreatorsAndSurvivesInventoryTooltip() {
        int processes = FactoryTier.BASIC.processes;
        ItemStack stack = new ItemStack(MekIntegrationBlocks.getMekIntegrationFactory(FactoryTier.BASIC,
                MekIntegrationFactoryType.SINGULARITY_COMPRESSING));
        assertItemCreators(stack, processes * 2 + 1);
    }

    @Test
    void collectingFactoryRegistersItemAndChemicalCreatorsAndSurvivesInventoryTooltip() {
        int processes = FactoryTier.BASIC.processes;
        ItemStack stack = new ItemStack(MekIntegrationBlocks.getMekIntegrationFactory(FactoryTier.BASIC,
                MekIntegrationFactoryType.NEUTRON_COLLECTING));
        assertChemicalCreators(stack, processes);
        assertItemCreators(stack, processes + 1);
    }

    private static void assertItemCreators(ItemStack stack, int itemCount) {
        assertEquals(itemCount, ContainerType.ITEM.getContainerCount(stack));
        assertNotNull(ContainerType.ITEM.createContainer(stack, 0));
        stack.set(MekanismDataComponents.ATTACHED_ITEMS, ContainerType.ITEM.createNewAttachment(stack));
        YesNo.hasInventory(stack);
    }

    private static void assertChemicalCreators(ItemStack stack, int chemicalCount) {
        assertEquals(chemicalCount, ContainerType.CHEMICAL.getContainerCount(stack));
        assertNotNull(ContainerType.CHEMICAL.createContainer(stack, 0));
    }
}
