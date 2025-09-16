package committee.nova.mods.avaritia_integration.client.screen;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.ModuleManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ApiStatus.Internal
public class ModuleListScreen extends Screen {
    private final Screen parent;
    private ModuleListWidget widget;
    private Button enableButton;
    private boolean initialized = false;
    private List<Component> tooltips = List.of();

    public ModuleListScreen(Screen parent) {
        super(Component.translatable("screen.%s.module.title".formatted(AvaritiaIntegration.MOD_ID)));
        this.parent = parent;
    }

    @Override
    public void init() {
        super.init();
        if (this.initialized) this.widget.updateSize(40, this.width - 40, 64, this.height - 32);
        else {
            this.initialized = true;
            this.widget = new ModuleListWidget(this, this.minecraft, 40, this.width - 40, 64, this.height - 32, 24);
            this.widget.update();
        }
        this.addWidget(this.widget);
        this.addRenderableWidget(Button.builder(Component.translatable("screen.%s.module.back".formatted(AvaritiaIntegration.MOD_ID)), button -> this.onClose()).bounds(40, 40, 100, 20).build());
        this.enableButton = this.addRenderableWidget(Button.builder(Component.translatable("screen.%s.module.enable".formatted(AvaritiaIntegration.MOD_ID)), button -> {
            ModuleListWidget.ModuleEntry entry = this.widget.getSelected();
            if (entry != null) ModuleManager.switchEnableState(entry.getData());
            this.updateEnableButton();
        }).bounds(150, 40, 100, 20).build());
        this.enableButton.active = this.widget.getSelected() != null;
    }

    public void updateEnableButton() {
        ModuleListWidget.ModuleEntry entry = this.widget.getSelected();
        if (entry != null) {
            this.enableButton.active = true;
            this.enableButton.setMessage(Component.translatable("screen.%s.module.%s".formatted(AvaritiaIntegration.MOD_ID, entry.getData().getEnableState() != ModuleManager.EnableState.DISABLED ? "disable" : "enable")));
        } else this.enableButton.active = false;
    }

    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context);
        this.tooltips = List.of();
        this.widget.render(context, mouseX, mouseY, delta);
        assert this.minecraft != null;
        if (!this.tooltips.isEmpty())
            context.renderComponentTooltip(this.minecraft.font, this.tooltips, mouseX, mouseY);
        context.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);
        context.drawCenteredString(this.font, Component.translatable("screen.avaritia_integration.module.warning"), this.width / 2, 30, 16777215);
        super.render(context, mouseX, mouseY, delta);
    }

    public void select(ModuleListWidget.ModuleEntry entry) {
        this.widget.setSelected(entry);
        this.updateEnableButton();
    }

    @Override
    public void onClose() {
        super.onClose();
        assert this.minecraft != null;
        this.minecraft.setScreen(this.parent);
    }

    void setTooltips(List<Component> tooltips) {
        this.tooltips = tooltips;
    }
}
