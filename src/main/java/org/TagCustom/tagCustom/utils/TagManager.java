package org.TagCustom.tagCustom.utils;

import org.TagCustom.tagCustom.TagsCustom;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import java.util.ArrayList;
import java.util.List;

public class TagManager {

    private final TagsCustom plugin;
    private final boolean useFallback;

    public TagManager(TagsCustom plugin, boolean useFallback) {
        this.plugin = plugin;
        this.useFallback = useFallback;
        plugin.getLogger().info("📌 TagManager initialisé (Mode Fallback : " + useFallback + ")");
    }

    // 📌 Récupérer toutes les catégories
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        if (plugin.getConfig().getConfigurationSection("tags") != null) {
            categories.addAll(plugin.getConfig().getConfigurationSection("tags").getKeys(false));
            plugin.getLogger().info("🔹 Catégories détectées : " + categories);
        } else {
            plugin.getLogger().warning("⚠️ Aucune catégorie trouvée !");
        }
        return categories;
    }

    // 📌 Récupérer tous les tags d'une catégorie
    public List<String> getTagsByCategory(String category) {
        List<String> tags = new ArrayList<>();
        if (plugin.getConfig().getConfigurationSection("tags." + category) != null) {
            tags.addAll(plugin.getConfig().getConfigurationSection("tags." + category).getKeys(false));
            plugin.getLogger().info("🔹 Tags trouvés dans '" + category + "' : " + tags);
        } else {
            plugin.getLogger().warning("⚠️ Catégorie vide ou introuvable : " + category);
        }
        return tags;
    }

    // 📌 Vérifier si un tag existe dans une catégorie
    public boolean tagExists(String category, String tagId) {
        boolean exists = plugin.getConfig().contains("tags." + category + "." + tagId);
        if (!exists) {
            plugin.getLogger().warning("⚠️ Le tag '" + tagId + "' n'existe pas dans '" + category + "'");
        }
        return exists;
    }

    // 📌 Récupérer l'affichage d'un tag (ex: MiniMessage)
    public String getTagDisplay(String category, String tagId) {
        return plugin.getConfig().getString("tags." + category + "." + tagId + ".display", "<gray>[Tag]</gray>");
    }

    // 📌 Définir un tag actif pour un joueur
    public void setActiveTag(Player player, String tagId) {
        if (useFallback) {
            plugin.getConfig().set("players." + player.getUniqueId() + ".activeTag", tagId);
            plugin.saveConfig();
        } else {
            plugin.getDatabaseManager().saveActiveTag(player, tagId);
        }

        plugin.getLogger().info("✅ Tag actif défini pour " + player.getName() + " : " + tagId);
        applyTagToPlayer(player); // Mise à jour du pseudo du joueur avec le tag
    }

    // 📌 Récupérer le tag actif d'un joueur
    public String getActiveTag(Player player) {
        String activeTag = useFallback ?
                plugin.getConfig().getString("players." + player.getUniqueId() + ".activeTag") :
                plugin.getDatabaseManager().getActiveTag(player);

        plugin.getLogger().info("🔍 Tag actif récupéré pour " + player.getName() + " : " + (activeTag != null ? activeTag : "Aucun tag"));

        return activeTag != null ? activeTag : "";
    }

    // 📌 Appliquer le tag sur le joueur (ex: mettre à jour le pseudo)
    public void applyTagToPlayer(Player player) {
        String activeTag = getActiveTag(player);
        if (!activeTag.isEmpty()) {
            player.setDisplayName(ChatColor.translateAlternateColorCodes('&', activeTag) + " " + player.getName());
            plugin.getLogger().info("🎭 Nouveau pseudo pour " + player.getName() + " : " + player.getDisplayName());
        }
    }

    // 📌 Supprimer le tag actif d'un joueur
    public void removeActiveTag(Player player) {
        if (useFallback) {
            plugin.getConfig().set("players." + player.getUniqueId() + ".activeTag", null);
            plugin.saveConfig();
        } else {
            plugin.getDatabaseManager().deleteActiveTag(player);
        }
        plugin.getLogger().info("🗑️ Tag supprimé pour " + player.getName());
        applyTagToPlayer(player); // Mise à jour du pseudo après suppression
    }
    public String getCategoryOfTag(String tagId) {
        if (plugin.getConfig().getConfigurationSection("tags") != null) {
            for (String category : plugin.getConfig().getConfigurationSection("tags").getKeys(false)) {
                if (plugin.getConfig().contains("tags." + category + "." + tagId)) {
                    return category;
                }
            }
        }
        return null;
    }

}
