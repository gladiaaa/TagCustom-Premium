package org.TagCustom.tagCustom.menu;

import org.TagCustom.tagCustom.TagsCustom;
import org.TagCustom.tagCustom.utils.TagManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class TagMenu {

    private final TagsCustom plugin;
    private final TagManager tagManager;

    public TagMenu(TagsCustom plugin) {
        this.plugin = plugin;
        this.tagManager = plugin.getTagManager();
        plugin.getLogger().info("✅ TagMenu initialisé avec succès.");
    }

    // 📌 Ouvre le menu des catégories
    public void openCategoryMenu(Player player) {
        plugin.getLogger().info("📌 Ouverture du menu des catégories pour " + player.getName());
        int rows = plugin.getConfig().getInt("menu.rows", 6);
        String title = plugin.getConfig().getString("menu.title", "Choisissez une catégorie");
        Inventory inventory = Bukkit.createInventory(null, rows * 9, title);

        List<String> categories = tagManager.getAllCategories();
        if (categories.isEmpty()) {
            player.sendMessage("⚠️ Aucune catégorie trouvée !");
            return;
        }

        int index = 0;
        for (String category : categories) {
            ItemStack item = new ItemStack(Material.BOOK);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6" + category);
            item.setItemMeta(meta);

            if (index < inventory.getSize()) {
                inventory.setItem(index, item);
                index++;
            }
        }

        player.openInventory(inventory);
    }

    // 📌 Ouvre le menu des tags d'une catégorie
    public void openTagMenu(Player player, String category) {
        plugin.getLogger().info("📌 Ouverture du menu des tags pour " + player.getName() + " (Catégorie : " + category + ")");
        int rows = plugin.getConfig().getInt("menu.rows", 6);
        String title = "Tags : " + category;
        Inventory inventory = Bukkit.createInventory(null, rows * 9, title);

        List<String> tags = tagManager.getTagsByCategory(category);
        if (tags.isEmpty()) {
            player.sendMessage("⚠️ Aucun tag trouvé pour cette catégorie !");
            return;
        }

        int index = 0;
        for (String tag : tags) {
            ItemStack item = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§e" + tag);
            item.setItemMeta(meta);

            if (index < inventory.getSize()) {
                inventory.setItem(index, item);
                index++;
            }
        }

        player.openInventory(inventory);
    }

    // 📌 Gère les clics dans le menu
    public void handleMenuClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        String title = event.getView().getTitle();

        // 📌 Gestion du menu des catégories
        if (title.equals(plugin.getConfig().getString("menu.title", "Choisissez une catégorie"))) {
            if (clickedItem.getType() == Material.BOOK) {
                String category = clickedItem.getItemMeta().getDisplayName().substring(2); // Retire les codes de couleur
                plugin.getLogger().info(player.getName() + " a sélectionné la catégorie : " + category);
                openTagMenu(player, category);
            }
            return;
        }

        // 📌 Gestion du menu des tags
        if (title.contains("Tags : ")) {
            String category = title.split("Tags : ")[1];
            String tagName = clickedItem.getItemMeta().getDisplayName().substring(2); // Retire les codes de couleur

            // 📌 Équipe le tag
            if (tagManager.tagExists(category, tagName)) {
                tagManager.setActiveTag(player, tagName);
                player.sendMessage("✅ Vous avez équipé le tag : " + tagManager.getTagDisplay(category, tagName));
            } else {
                player.sendMessage("❌ Tag introuvable !");
            }
            player.closeInventory();
        }
    }
}
