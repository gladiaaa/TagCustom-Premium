package org.TagCustom.tagCustom.utils;

import org.TagCustom.tagCustom.TagsCustom;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TagManager {

    private final TagsCustom plugin;
    private final boolean useFallback;

    public TagManager(TagsCustom plugin, boolean useFallback) {
        this.plugin = plugin;
        this.useFallback = useFallback;
    }

    public List<String> getAllTags() {
        List<String> tags = new ArrayList<>();
        if (plugin.getConfig().getConfigurationSection("tags") != null) {
            tags.addAll(plugin.getConfig().getConfigurationSection("tags").getKeys(false));
        }
        return tags;
    }

    public void setActiveTag(Player player, String tagId) {
        if (useFallback) {
            plugin.getConfig().set("players." + player.getUniqueId() + ".activeTag", tagId);
            plugin.saveConfig();
        } else {
            plugin.getDatabaseManager().saveActiveTag(player, tagId);
        }
    }

    public String getActiveTag(Player player) {
        if (useFallback) {
            return plugin.getConfig().getString("players." + player.getUniqueId() + ".activeTag");
        } else {
            return plugin.getDatabaseManager().getActiveTag(player);
        }
    }

    public void removeActiveTag(Player player) {
        if (useFallback) {
            plugin.getConfig().set("players." + player.getUniqueId() + ".activeTag", null);
            plugin.saveConfig();
        } else {
            plugin.getDatabaseManager().deleteActiveTag(player);
        }
    }
}
