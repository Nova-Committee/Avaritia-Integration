package committee.nova.mods.avaritia_integration.client.screen;

import committee.nova.mods.avaritia_integration.api.load.IntegrationLoadApi;
import committee.nova.mods.avaritia_integration.api.load.LoadDecision;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ApiStatus.Internal
public class ModuleListWidget extends ObjectSelectionList<ModuleListWidget.ModuleEntry> {

    private final ModuleListScreen screen;

    public ModuleListWidget(ModuleListScreen screen, Minecraft client, int left, int right, int top, int bottom) {
        super(client, right - left, Math.max(36, bottom - top), top, 36);
        this.screen = screen;
        setX(left);
    }

    public void update(String selectedId) {
        clearEntries();
        IntegrationLoadApi.explainAll().values().forEach(decision -> {
            ModuleEntry entry = new ModuleEntry(screen,
                    IntegrationLoadApi.startupDecision(decision.integrationModId()));
            addEntry(entry);
            if (decision.integrationModId().equals(selectedId)) setSelected(entry);
        });
    }

    @Override
    public void setSelected(ModuleEntry entry) {
        super.setSelected(entry);
        screen.updateEnableButton();
    }

    @Override
    public int getRowWidth() {
        return width - 20;
    }

    @Override
    protected int getScrollbarPosition() {
        return getRight() - 6;
    }

    public static class ModuleEntry extends Entry<ModuleEntry> {

        private final Minecraft client = Minecraft.getInstance();
        private final ModuleListScreen screen;
        private final LoadDecision data;

        public ModuleEntry(ModuleListScreen screen, LoadDecision data) {
            this.screen = screen;
            this.data = data;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX,
                           int mouseY, boolean hovered, float tickDelta) {
            graphics.drawString(client.font, client.font.plainSubstrByWidth(data.integrationModId(), entryWidth - 8),
                    x + 4, y + 2, 0xFFFFFF);
            graphics.drawString(client.font,
                    net.minecraft.locale.Language.getInstance()
                            .getVisualOrder(client.font.substrByWidth(screen.currentState(data), entryWidth - 8)),
                    x + 4, y + 12, data.shouldLoad() ? 0x99DD99 : 0xDDCC99);
            graphics.drawString(client.font,
                    net.minecraft.locale.Language.getInstance()
                            .getVisualOrder(client.font.substrByWidth(screen.nextState(data.integrationModId()),
                                    entryWidth - 8)),
                    x + 4, y + 22, 0xCCCCCC);
            if (hovered) {
                screen.setTooltips(List.of(Component.literal(data.integrationModId()), screen.currentState(data),
                        screen.nextState(data.integrationModId()), Component.literal(data.message()),
                        Component.literal(IntegrationLoadApi.explain(data.integrationModId()).message())));
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button != 0) return false;
            list.setSelected(this);
            return true;
        }

        public LoadDecision getData() {
            return data;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.literal(data.integrationModId()).append(" ").append(screen.currentState(data))
                    .append(" ").append(screen.nextState(data.integrationModId()));
        }
    }
}
