package io.github.wckds.wckdhax;

public class Template extends Base{
    public static Template INSTANCE;
    public Template() {
        INSTANCE = this;
        enabled = true; // enabled by default
    }
    // Override getHelpMessage() and getToolTip()
    // Override config() and toggle() if you wanna
}
