package committee.nova.mods.avaritia_integration.client.screen;

import committee.nova.mods.avaritia_integration.api.load.IntegrationLoadApi;
import committee.nova.mods.avaritia_integration.api.load.LoadDecision;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ApiStatus.Internal
public class ModuleListScreen extends Screen {

    private static final String PREFIX = "screen.avaritia_integration.module.";
    private final Screen parent;
    private final Map<String, Boolean> saved = new LinkedHashMap<>();
    private final Map<String, Boolean> draft = new LinkedHashMap<>();
    private String revision;
    private ModuleListWidget widget;
    private Button toggleButton;
    private Button saveButton;
    private Component status = Component.empty();
    private List<Component> tooltips = List.of();
    private boolean initialized;

    public ModuleListScreen(Screen parent) {
        super(text("title"));
        this.parent = parent;
    }

    @Override
    public void init() {
        if (!initialized) {
            readConfiguration();
            initialized = true;
        }
        String selected = widget == null || widget.getSelected() == null ? null :
                widget.getSelected().getData().integrationModId();
        double scroll = widget == null ? 0 : widget.getScrollAmount();
        widget = addRenderableWidget(new ModuleListWidget(this, minecraft, 16, width - 16, 78, height - 62));
        widget.update(selected);
        widget.setScrollAmount(scroll);
        int buttonWidth = Math.min(150, (width - 40) / 2);
        addRenderableWidget(Button.builder(text("reload"), button -> {
            readConfiguration();
            widget.update(selectedId());
            updateEnableButton();
        }).bounds(width / 2 - buttonWidth - 4, 50, buttonWidth, 20).build());
        toggleButton = addRenderableWidget(Button.builder(text("enable"), button -> {
            String id = selectedId();
            if (id != null) draft.put(id, !isEnabled(id));
            updateEnableButton();
        }).bounds(width / 2 + 4, 50, buttonWidth, 20).build());
        saveButton = addRenderableWidget(Button.builder(text("save"), button -> save())
                .bounds(width / 2 - buttonWidth - 4, height - 28, buttonWidth, 20).build());
        addRenderableWidget(Button.builder(text("cancel"), button -> onClose())
                .bounds(width / 2 + 4, height - 28, buttonWidth, 20).build());
        updateEnableButton();
    }

    private void readConfiguration() {
        IntegrationLoadApi.reload();
        revision = IntegrationLoadApi.revision();
        saved.clear();
        IntegrationLoadApi.configuredRules().forEach((id, rule) -> {
            if (rule != null) saved.put(id, rule.isEnabled());
        });
        draft.clear();
        draft.putAll(saved);
        status = IntegrationLoadApi.loadError() == null ? Component.empty() :
                text("read_failed").withStyle(ChatFormatting.RED);
    }

    private void save() {
        Map<String, Boolean> changes = new LinkedHashMap<>();
        draft.forEach((id, enabled) -> {
            if (!Objects.equals(saved.get(id), enabled)) changes.put(id, enabled);
        });
        try {
            String warning = IntegrationLoadApi.saveEnabled(revision, changes);
            revision = IntegrationLoadApi.revision();
            saved.clear();
            saved.putAll(draft);
            status = warning == null ? text("saved").withStyle(ChatFormatting.GREEN) :
                    Component.literal(warning).withStyle(ChatFormatting.YELLOW);
        } catch (IOException | IllegalArgumentException e) {
            status = text("save_failed", e.getMessage()).withStyle(ChatFormatting.RED);
        }
        updateEnableButton();
    }

    public void updateEnableButton() {
        if (toggleButton == null || saveButton == null) return;
        String id = selectedId();
        toggleButton.active = id != null && draft.containsKey(id) && IntegrationLoadApi.loadError() == null;
        toggleButton.setMessage(text(id != null && isEnabled(id) ? "disable" : "enable"));
        saveButton.active = !saved.equals(draft) && IntegrationLoadApi.loadError() == null;
    }

    private String selectedId() {
        return widget.getSelected() == null ? null : widget.getSelected().getData().integrationModId();
    }

    boolean isEnabled(String id) {
        return draft.getOrDefault(id, true);
    }

    Component nextState(String id) {
        return text("next", text(isEnabled(id) ? "enabled" : "disabled"))
                .append(!Objects.equals(saved.get(id), draft.get(id)) ? text("unsaved") :
                        IntegrationLoadApi.needsRestart(id, isEnabled(id)) ? text("pending") : Component.empty());
    }

    Component currentState(LoadDecision decision) {
        return text("current", Component.translatable("screen.avaritia_integration.load_state." +
                decision.state().name().toLowerCase(java.util.Locale.ROOT)));
    }

    static net.minecraft.network.chat.MutableComponent text(String key, Object... args) {
        return Component.translatable(PREFIX + key, args);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        tooltips = List.of();
        super.render(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(font, title, width / 2, 12, 0xFFFFFF);
        graphics.drawCenteredString(font, text("warning"), width / 2, 28, 0xAAAAAA);
        graphics.drawCenteredString(font, text("local_only"), width / 2, 39, 0xAAAAAA);
        graphics.drawString(font,
                net.minecraft.locale.Language.getInstance().getVisualOrder(font.substrByWidth(status, width - 32)), 16,
                height - 51, 0xFFFFFF);
        if (!tooltips.isEmpty()) graphics.renderComponentTooltip(font, tooltips, mouseX, mouseY);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    void setTooltips(List<Component> tooltips) {
        this.tooltips = tooltips;
    }
}
