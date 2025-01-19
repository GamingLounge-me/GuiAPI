package me.gaminglounge.guiapi; 
 
import org.bukkit.plugin.java.JavaPlugin; 
 
public final class GuiApi extends JavaPlugin { 
 
    public static GuiApi INSTANCE; 
 
    @Override
    public void onLoad() {
        INSTANCE = this; 
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
} 
