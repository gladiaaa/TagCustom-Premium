package org.TagCustom.tagCustom.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.TagCustom.tagCustom.TagsCustom;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class TagMenu {
    private final TagsCustom plugin;
    private final String category;

    public TagMenu(TagsCustom plugin, String category) {
        this.plugin = plugin;
        this.category = category;
    }

    /**
     * Ouvre le menu des tags pour une catégorie sélectionnée.
     * @param player Joueur qui ouvre le menu.
     */
    public void open(Player player) {
        // ✅ Formater le titre du menu avec une couleur
        String formattedTitle = LegacyComponentSerializer.legacySection().serialize(
                MiniMessage.miniMessage().deserialize("📜 <gold>Tags : " + category + "</gold>")
        );

        Inventory inv = Bukkit.createInventory(null, 54, formattedTitle);

        List<String> tags = plugin.getTagManager().getTagsByCategory(category);
        if (tags.isEmpty()) {
            player.sendMessage("⚠️ Aucun tag trouvé pour cette catégorie !");
            return;
        }

        int slot = 0;
        for (String tag : tags) {
            ItemStack tagItem = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = tagItem.getItemMeta();

            // ✅ Convertir MiniMessage en texte coloré utilisable dans Minecraft
            String tagDisplay = plugin.getTagManager().getTagDisplay(category, tag);
            String formattedTag = LegacyComponentSerializer.legacySection().serialize(
                    MiniMessage.miniMessage().deserialize(tagDisplay)
            );

            meta.setDisplayName(formattedTag);
            tagItem.setItemMeta(meta);
            inv.setItem(slot++, tagItem);
        }

        player.openInventory(inv);
    }
}
