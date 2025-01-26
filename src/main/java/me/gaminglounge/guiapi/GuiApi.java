package me.gaminglounge.guiapi;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import me.gaminglounge.configapi.LoadConfig;

public final class GuiApi extends JavaPlugin {

    public static GuiApi INSTANCE;

    @Override
    public void onLoad() {
        INSTANCE = this;

        Map<String, InputStream> lang = new HashMap<>();
        lang.put("en_US.json", this.getResource("lang/en_US.json"));
        lang.put("de_DE.json", this.getResource("lang/de_DE.json"));
        LoadConfig.registerLanguage(this, lang);

    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}
