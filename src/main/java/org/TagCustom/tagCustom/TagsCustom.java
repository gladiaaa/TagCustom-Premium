package org.TagCustom.tagCustom;

import org.TagCustom.tagCustom.commands.TagCommand;
import org.TagCustom.tagCustom.listeners.MenuClickListener;
import org.TagCustom.tagCustom.placeholders.TagPlaceholder;
import org.TagCustom.tagCustom.utils.TagManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TagsCustom extends JavaPlugin {

    private TagManager tagManager; // Instance unique de TagManager

    @Override
    public void onEnable() {
        // Initialiser le TagManager avec une référence au plugin principal
        tagManager = new TagManager(this);

        // Charger la configuration par défaut
        saveDefaultConfig();

        // Enregistrer les commandes
        getCommand("tag").setExecutor(new TagCommand(this));

        // Enregistrer les événements
        getServer().getPluginManager().registerEvents(new MenuClickListener(this), this);

        // Vérifier si PlaceholderAPI est installé et enregistrer le placeholder
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new TagPlaceholder(this).register();
            getLogger().info("PlaceholderAPI détecté. Les placeholders de TagsCustom sont activés.");
        } else {
            getLogger().warning("PlaceholderAPI n'est pas installé. Les placeholders ne fonctionneront pas.");
        }

        getLogger().info("Plugin TagsCustom activé avec succès !");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin TagsCustom désactivé.");
    }

    // Getter pour accéder au TagManager
    public TagManager getTagManager() {
        return tagManager;
    }
}
