package committee.nova.mods.avaritia_integration.client.screen;

import committee.nova.mods.avaritia_integration.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class ModuleListWidget extends ObjectSelectionList<ModuleListWidget.ModuleEntry> {
    private final ModuleListScreen screen;

    public ModuleListWidget(ModuleListScreen screen, Minecraft client, int left, int right, int top, int bottom, int entryHeight) {
        super(client, right - left, bottom - top, top, bottom, entryHeight);
        this.screen = screen;
        this.setRenderTopAndBottom(false);
        this.updateSize(left, right, top, bottom);
    }

    public void update() {
        this.clearEntries();
        ModuleManager.getAllModules().forEach(x -> this.addEntry(new ModuleEntry(this.screen, x)));
    }

    @Override
    public void updateSize(int left, int right, int top, int bottom) {
        this.x0 = left;
        this.x1 = right;
        this.y0 = top;
        this.y1 = bottom;
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 30 * 2;
    }

    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 30 + 30;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        ModuleEntry entry = this.getSelected();
        return entry != null && entry.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    public static class ModuleEntry extends Entry<ModuleEntry> {
        private final Minecraft client = Minecraft.getInstance();
        private final ModuleListScreen screen;
        private final ModuleManager.ModuleData data;

        public ModuleEntry(ModuleListScreen screen, ModuleManager.ModuleData data) {
            this.screen = screen;
            this.data = data;
        }

        @Override
        public void render(GuiGraphics context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawString(this.client.font, this.data.getTranslateKey(), x + 32 + 3, y + 1, 16777215, true);
            context.drawString(this.client.font, this.data.getStateKey(), x + 32 + 3, y + 1 + 9, 16777215, true);
            if (this.isMouseOver(mouseX, mouseY)) this.screen.setTooltips(this.data.getErrorTooltip());
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.screen.select(this);
            return false;
        }

        public ModuleManager.ModuleData getData() {
            return this.data;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.nullToEmpty("");
        }
    }
}