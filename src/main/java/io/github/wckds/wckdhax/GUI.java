package io.github.wckds.wckdhax;

import io.github.wckds.wckdhax.gui.MainScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class GUI extends Base{
    public static GUI INSTANCE;
    public GUI() {
        INSTANCE = this;
    }

    public boolean toggle() {
        if (enabled) {
            enabled = false;
            MinecraftClient.getInstance().setScreen(null);
        } else {
            enabled = true;
            MinecraftClient.getInstance().setScreen(new MainScreen(Text.of("WckdHax")));
        }
        return true;
    }

    @Override
    public String getHelpMessage() {
        return "GUI - Helps you manage your modules effectively.";
    }

    @Override
    public String getToolTip() {
        return "Manage your modules";
    }
}
