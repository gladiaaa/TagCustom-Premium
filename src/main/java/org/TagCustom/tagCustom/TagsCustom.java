package org.TagCustom.tagCustom;

import org.TagCustom.tagCustom.commands.TagCommand;
import org.TagCustom.tagCustom.listeners.MenuClickListener;
import org.TagCustom.tagCustom.placeholders.TagPlaceholder;
import org.TagCustom.tagCustom.utils.DatabaseManager;
import org.TagCustom.tagCustom.utils.TagManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TagsCustom extends JavaPlugin {

    private TagManager tagManager;
    private DatabaseManager databaseManager;
    private boolean useFallback;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // 🔵 Initialisation du DatabaseManager
        databaseManager = new DatabaseManager(this);
        try {
            databaseManager.connect();
            databaseManager.initialize();
            useFallback = false;
            getLogger().info("🟢 Mode actuel : Base de données (MySQL/SQLite)");
        } catch (Exception e) {
            getLogger().severe("🔴 Erreur de connexion à la base de données. Utilisation du mode fallback !");
            e.printStackTrace();
            useFallback = true;
        }

        // 🔵 Initialisation du TagManager
        tagManager = new TagManager(this, useFallback);

        // 🔵 Enregistrement des commandes
        getCommand("tag").setExecutor(new TagCommand(this));

        // 🔵 Enregistrement des événements
        getServer().getPluginManager().registerEvents(new MenuClickListener(this), this);

        // 🔵 Vérifier si PlaceholderAPI est installé
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new TagPlaceholder(this).register();
            getLogger().info("🟡 PlaceholderAPI détecté. Activation des placeholders.");
        } else {
            getLogger().warning("⚠️ PlaceholderAPI non trouvé. Les placeholders ne fonctionneront pas.");
        }

        getLogger().info("✅ Plugin TagsCustom activé avec succès !");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null && !useFallback) {
            databaseManager.disconnect();
        }
        getLogger().info("⛔ Plugin TagsCustom désactivé.");
    }

    // Getters
    public TagManager getTagManager() { return tagManager; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public boolean isUsingFallback() { return useFallback; }
}
