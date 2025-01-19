package org.TagCustom.tagCustom.utils;

import org.TagCustom.tagCustom.TagsCustom;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TagManager {

    private final TagsCustom plugin;

    public TagManager(TagsCustom plugin) {
        this.plugin = plugin;
    }

    // Méthode pour récupérer tous les tags définis dans la configuration
    public List<String> getAllTags() {
        List<String> tags = new ArrayList<>();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("tags");
        if (section != null) {
            tags.addAll(section.getKeys(false));
        }
        return tags;
    }

    // Méthode pour récupérer le tag par son ID
    public String getTagById(int id) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("tags");
        if (section != null) {
            for (String tag : section.getKeys(false)) {
                if (section.getInt(tag + ".id") == id) {
                    return tag;
                }
            }
        }
        return null;
    }

    // Méthode pour définir le tag actif pour un joueur
    public void setActiveTag(Player player, String tagId) {
        plugin.getConfig().set("players." + player.getUniqueId() + ".activeTag", tagId);
        plugin.saveConfig();
    }

    // Méthode pour récupérer le tag actif d'un joueur
    public String getActiveTag(Player player) {
        return plugin.getConfig().getString("players." + player.getUniqueId() + ".activeTag");
    }

    // Méthode pour vérifier si un joueur a la permission pour un tag
    public boolean hasPermissionForTag(Player player, String tagId) {
        String permission = plugin.getConfig().getString("tags." + tagId + ".permission", "TagsCustom.default");
        return player.hasPermission(permission);
    }
}
