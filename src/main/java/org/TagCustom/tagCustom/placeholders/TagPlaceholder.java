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

        // ✅ Placeholder : %tags_Player_tag%
        if (identifier.equals("Player_tag")) {
            String tagId = plugin.getTagManager().getActiveTag(player);
            if (tagId == null || tagId.isEmpty()) {
                return "<gray>Aucun tag</gray>"; // Si aucun tag actif, renvoyer un message par défaut
            }

            // ✅ Vérifier la catégorie pour récupérer l'affichage exact
            String category = plugin.getTagManager().getCategoryOfTag(tagId);
            if (category == null) {
                return "<gray>Aucun tag</gray>";
            }

            // ✅ Récupérer l'affichage du tag (MiniMessage)
            String miniMessageDisplay = plugin.getConfig().getString("tags." + category + "." + tagId + ".display", "<gray>[Tag]</gray>");

            // ✅ Important : Retourner directement MiniMessage pour être interprété par zEssential
            return miniMessageDisplay;
        }

        // ✅ Placeholder : %tags_id_1% (Permet d'afficher un tag via son ID)
        if (identifier.startsWith("id_")) {
            String id = identifier.split("_")[1]; // Récupère l'ID
            for (String category : plugin.getConfig().getConfigurationSection("tags").getKeys(false)) {
                for (String tag : plugin.getConfig().getConfigurationSection("tags." + category).getKeys(false)) {
                    if (plugin.getConfig().getInt("tags." + category + "." + tag + ".id") == Integer.parseInt(id)) {
                        return plugin.getConfig().getString("tags." + category + "." + tag + ".display", "<gray>[Tag]</gray>");
                    }
                }
            }
            return "<gray>Tag introuvable</gray>";
        }

        return null; // Placeholder non reconnu
    }
}
