package org.TagCustom.tagCustom.utils;

import org.TagCustom.tagCustom.TagsCustom;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final TagsCustom plugin;

    public ConfigManager(TagsCustom plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
    }

    public void saveDefaultConfig() {
        plugin.saveDefaultConfig();
    }
}
