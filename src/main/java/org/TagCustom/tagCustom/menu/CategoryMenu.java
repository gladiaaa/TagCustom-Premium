package org.TagCustom.tagCustom.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.TagCustom.tagCustom.TagsCustom;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class CategoryMenu {
    private final TagsCustom plugin;

    public CategoryMenu(TagsCustom plugin) {
        this.plugin = plugin;
    }

    /**
     * Ouvre le menu des catégories pour un joueur.
     * @param player Joueur qui ouvre le menu.
     */
    public void open(Player player) {
        FileConfiguration config = plugin.getConfig();

        // ✅ Formater le titre du menu avec une couleur
        String formattedTitle = LegacyComponentSerializer.legacySection().serialize(
                MiniMessage.miniMessage().deserialize("📂 <gold>Choisissez une catégorie</gold>")
        );

        Inventory inv = Bukkit.createInventory(null, 54, formattedTitle);

        List<String> categories = plugin.getTagManager().getAllCategories();
        if (categories.isEmpty()) {
            player.sendMessage("⚠️ Aucune catégorie trouvée !");
            return;
        }

        int slot = 0;
        for (String category : categories) {
            // 🔄 Récupérer l'item de la catégorie dans la config
            String materialName = config.getString("category_items." + category, "BOOK");
            Material material = Material.matchMaterial(materialName);

            // 🔄 Si l'item est invalide, utiliser BOOK par défaut
            if (material == null) {
                material = Material.BOOK;
                plugin.getLogger().warning("⚠️ Item invalide pour la catégorie '" + category + "', utilisation de BOOK.");
            }

            ItemStack categoryItem = new ItemStack(material);
            ItemMeta meta = categoryItem.getItemMeta();

            // ✅ Convertir le nom de la catégorie en couleur avec MiniMessage
            String formattedCategory = LegacyComponentSerializer.legacySection().serialize(
                    MiniMessage.miniMessage().deserialize("📁 <yellow>" + category + "</yellow>")
            );

            meta.setDisplayName(formattedCategory);
            categoryItem.setItemMeta(meta);
            inv.setItem(slot++, categoryItem);
        }

        player.openInventory(inv);
    }
}
