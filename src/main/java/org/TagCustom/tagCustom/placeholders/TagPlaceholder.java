package org.TagCustom.tagCustom.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.TagCustom.tagCustom.TagsCustom;
import org.bukkit.entity.Player;

public class TagPlaceholder extends PlaceholderExpansion {

    private final TagsCustom plugin;

    public TagPlaceholder(TagsCustom plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "tags";
    }

    @Override
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // Placeholder persistant après un reload
    }

    @Override
    public boolean canRegister() {
        return true; // Placeholder peut être enregistré
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        // Gestion du placeholder Player_tag
        if (identifier.equals("Player_tag")) {
            String tagId = plugin.getTagManager().getActiveTag(player);
            if (tagId == null) {
                return "<gray>Aucun tag</gray>";
            }

            // Récupérer le texte MiniMessage depuis la configuration
            String miniMessageDisplay = plugin.getConfig().getString("tags." + tagId + ".display", "<gray>[Tag]</gray>");

            // Retourner le texte tel quel pour une interprétation par zEssential
            return miniMessageDisplay;
        }

        // Placeholder par ID : %tags_id_1%
        if (identifier.startsWith("id_")) {
            String id = identifier.split("_")[1]; // Récupère l'ID
            String tagId = plugin.getConfig().getConfigurationSection("tags").getKeys(false).stream()
                    .filter(tag -> plugin.getConfig().getInt("tags." + tag + ".id") == Integer.parseInt(id))
                    .findFirst().orElse(null);

            if (tagId != null) {
                return plugin.getConfig().getString("tags." + tagId + ".display", "<gray>[Tag]</gray>");
            } else {
                return "<gray>Tag introuvable</gray>";
            }
        }

        return null; // Placeholder non reconnu
    }
}
