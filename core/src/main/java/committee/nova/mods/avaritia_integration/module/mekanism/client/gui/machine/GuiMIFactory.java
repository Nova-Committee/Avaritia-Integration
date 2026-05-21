package committee.nova.mods.avaritia_integration.module.mekanism.client.gui.machine;

import committee.nova.mods.avaritia_integration.module.mekanism.client.gui.element.tab.GuiMISortingTab;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.factory.TileEntityChemicalToItemMIFactory;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.factory.TileEntityItemToItemMIFactory;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.factory.TileEntityMIFactory;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.tier.FactoryTier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongSupplier;

public class GuiMIFactory extends GuiConfigurableTile<TileEntityMIFactory<?>, MekanismTileContainer<TileEntityMIFactory<?>>> {

    public GuiMIFactory(MekanismTileContainer<TileEntityMIFactory<?>> container, Inventory inv, Component title) {
        super(container, inv, title);
        if (tile instanceof TileEntityChemicalToItemMIFactory<?>) imageHeight += 13;
        inventoryLabelY = tile instanceof TileEntityChemicalToItemMIFactory<?> ? 88 : 75;
        if (tile.tier == FactoryTier.ULTIMATE) {
            imageWidth += 34;
            inventoryLabelX = 26;
        }
        if (isEMLoadAndTierOrdinalAboveOverLocked()) {
            int index = tile.tier.ordinal() - 4;
            imageWidth += (36 * (index + 2)) + (2 * index);
            inventoryLabelX = (22 * (index + 2)) - (3 * index);
        }
        titleLabelY = 4;
        dynamicSlots = true;
    }

    private boolean isEMLoadAndTierOrdinalAboveOverLocked() {
        if (ModList.get().isLoaded("evolvedmekanism")) {
            return tile.tier.ordinal() >= EMFactoryTier.OVERCLOCKED.ordinal();
        }
        return false;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        if (tile instanceof TileEntityItemToItemMIFactory<?>) {
            addRenderableWidget(new GuiMISortingTab(this, tile));
        }
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), imageWidth - 12, 16, tile instanceof TileEntityChemicalToItemMIFactory ? 65 : 52))
                .warning(WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY, 0));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), (LongSupplier) tile::getLastUsage));

        if (tile instanceof TileEntityChemicalToItemMIFactory<?> factory) {
            for (int i = 0; i < tile.tier.processes; i++) {
                int index = i;
                addRenderableWidget(new GuiChemicalGauge(() -> factory.inputGasTanks.get(index), () -> factory.getChemicalTanks(null), GaugeType.SMALL, this, factory.getXPos(index) - 1, 13))
                        .warning(WarningType.NO_MATCHING_RECIPE, factory.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT, index));
            }
        }

        for (int i = 0; i < tile.tier.processes; i++) {
            int cacheIndex = i;
            addRenderableWidget(new GuiProgress(() -> tile.getScaledProgress(1, cacheIndex), ProgressType.DOWN, this, 4 + tile.getXPos(i), getProgressYPos()))
                    .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT, cacheIndex));
        }
    }

    private int getProgressYPos() {
        return tile instanceof TileEntityChemicalToItemMIFactory<?> ? 46 : 33;
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        renderInventoryText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
