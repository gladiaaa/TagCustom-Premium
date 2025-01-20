package org.TagCustom.tagCustom;

import org.TagCustom.tagCustom.commands.TagCommand;
import org.TagCustom.tagCustom.listeners.MenuClickListener;
import org.TagCustom.tagCustom.placeholders.TagPlaceholder;
import org.TagCustom.tagCustom.utils.DatabaseManager;
import org.TagCustom.tagCustom.utils.TagManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TagsCustom extends JavaPlugin {

    private static final String GREEN = "\u001B[32m"; // Couleur verte
    private static final String RED = "\u001B[31m";   // Couleur rouge
    private static final String YELLOW = "\u001B[33m"; // Couleur jaune
    private static final String RESET = "\u001B[0m";  // Réinitialise la couleur

    private TagManager tagManager; // Instance unique de TagManager
    private DatabaseManager databaseManager; // Instance unique de DatabaseManager
    private boolean useFallback; // Indique si le mode fallback (config.yml) est activé

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Initialiser le DatabaseManager
        databaseManager = new DatabaseManager(this);

        try {
            databaseManager.connect();
            databaseManager.initialize();
            useFallback = false; // Base de données disponible, pas de fallback
            getLogger().info(GREEN + "Connecté à la base de données avec succès !" + RESET);
        } catch (Exception e) {
            getLogger().severe(RED + "Erreur lors de la connexion à la base de données. Utilisation du fichier config.yml par défaut !" + RESET);
            e.printStackTrace();
            useFallback = true; // Basculer en mode fallback
        }

        // Initialiser le TagManager
        tagManager = new TagManager(this, useFallback);

        // Enregistrer les commandes
        getCommand("tag").setExecutor(new TagCommand(this));

        // Enregistrer les événements
        getServer().getPluginManager().registerEvents(new MenuClickListener(this), this);

        // Vérifier si PlaceholderAPI est installé et enregistrer le placeholder
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new TagPlaceholder(this).register();
            getLogger().info(YELLOW + "PlaceholderAPI détecté. Les placeholders de TagsCustom sont activés." + RESET);
        } else {
            getLogger().warning(RED + "PlaceholderAPI n'est pas installé. Les placeholders ne fonctionneront pas." + RESET);
        }

        getLogger().info(GREEN + "Plugin TagsCustom activé avec succès !" + RESET);
    }

    @Override
    public void onDisable() {
        // Fermer la connexion à la base de données si elle est active
        if (databaseManager != null && !useFallback) {
            databaseManager.disconnect();
        }

        getLogger().info(YELLOW + "Plugin TagsCustom désactivé." + RESET);
    }

    // Getters pour accéder aux gestionnaires
    public TagManager getTagManager() {
        return tagManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public boolean isUsingFallback() {
        return useFallback;
    }
}
