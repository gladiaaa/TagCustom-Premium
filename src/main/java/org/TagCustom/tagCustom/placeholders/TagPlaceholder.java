package org.TagCustom.tagCustom.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.TagCustom.tagCustom.TagsCustom;

public class TagPlaceholder extends PlaceholderExpansion {

    private final TagsCustom plugin;

    public TagPlaceholder(TagsCustom plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("Player_tag")) {
            // Récupérer le tag actif (à implémenter)
            return "Aucun tag";
        }
        return null;
    }

    @Override
    public String getIdentifier() {
        return "tags";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
