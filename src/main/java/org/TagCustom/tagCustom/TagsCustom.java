package org.TagCustom.tagCustom;

import org.TagCustom.tagCustom.commands.TagCommand;
import org.TagCustom.tagCustom.listeners.MenuClickListener;
import org.TagCustom.tagCustom.placeholders.TagPlaceholder;
import org.bukkit.plugin.java.JavaPlugin;

public class TagsCustom extends JavaPlugin {

    @Override
    public void onEnable() {
        // Charger la configuration
        saveDefaultConfig();

        // Enregistrer les commandes
        getCommand("tag").setExecutor(new TagCommand(this));

        // Enregistrer les événements
        getServer().getPluginManager().registerEvents(new MenuClickListener(this), this);

        // Vérifier si PlaceholderAPI est installé
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new TagPlaceholder(this).register();
            getLogger().info("PlaceholderAPI détecté et activé.");
        } else {
            getLogger().warning("PlaceholderAPI n'est pas installé. Les placeholders ne fonctionneront pas.");
        }

        getLogger().info("Plugin TagsCustom activé !");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin TagsCustom désactivé.");
    }
}
