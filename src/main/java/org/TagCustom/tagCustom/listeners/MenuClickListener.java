package org.TagCustom.tagCustom.listeners;

import org.TagCustom.tagCustom.TagsCustom;
import org.TagCustom.tagCustom.menu.CategoryMenu;
import org.TagCustom.tagCustom.menu.AdminMenu;
import org.TagCustom.tagCustom.menu.TagMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;


public class MenuClickListener implements Listener {
    private final TagsCustom plugin;

    public MenuClickListener(TagsCustom plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Vérifie si l'inventaire cliqué est valide
        if (event.getClickedInventory() == null || event.getCurrentItem() == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        String title = event.getView().getTitle();

        // Empêche les joueurs de déplacer les items dans les menus
        event.setCancelled(true);

        // 📌 Gérer le clic dans le bon menu
        handleMenuClick(player, title, clickedItem);
    }

    /**
     * Gère les clics dans les menus du plugin.
     * @param player Le joueur qui a cliqué
     * @param title Le titre du menu ouvert
     * @param clickedItem L'item sur lequel le joueur a cliqué
     */
    private void handleMenuClick(Player player, String title, ItemStack clickedItem) {
        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        // 📌 Gestion du Menu Principal
        if (title.equals("📌 Menu Principal")) {
            switch (clickedItem.getItemMeta().getDisplayName()) {
                case "📂 Tags Disponibles":
                    new CategoryMenu(plugin).open(player);
                    break;
                case "⚙️ Gestion des Tags (Admin)":
                    if (player.hasPermission("TagsCustom.admin")) {
                        new AdminMenu(plugin).open(player);
                    } else {
                        player.sendMessage("🚫 Vous n'avez pas la permission !");
                    }
                    break;
                default:
                    break;
            }
        }

        // 📌 Gestion du Menu des Catégories
        if (ChatColor.stripColor(title).equalsIgnoreCase("📂 Choisissez une catégorie")) {
            String category = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).replace("📁 ", "").trim();

            plugin.getLogger().info("📌 CategoryMenu Click Detected: " + category);

            // Vérification si la catégorie existe
            if (plugin.getTagManager().getAllCategories().contains(category)) {
                new TagMenu(plugin, category).open(player);
                plugin.getLogger().info("✅ Ouverture du menu des tags pour la catégorie : " + category);
            } else {
                player.sendMessage(ChatColor.RED + "❌ Catégorie introuvable !");
                plugin.getLogger().warning("⚠️ Catégorie inconnue cliquée par " + player.getName() + ": " + category);
            }
        }


        // 📌 Gestion du Menu des Tags
        if (ChatColor.stripColor(title).startsWith("📜 Tags : ")) {
            String category = ChatColor.stripColor(title).replace("📜 Tags : ", "").trim();
            String displayedTagName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).trim();

            plugin.getLogger().info("📌 TagMenu Click Detected: " + displayedTagName + " (Catégorie : " + category + ")");

            // 🔍 Vérifier la correspondance entre le nom affiché et l'ID brut du tag
            for (String tag : plugin.getTagManager().getTagsByCategory(category)) {
                String tagDisplay = plugin.getTagManager().getTagDisplay(category, tag).trim();

                if (ChatColor.stripColor(tagDisplay).equalsIgnoreCase(displayedTagName)) {
                    plugin.getLogger().info("✅ Tag trouvé : " + tag + " (Catégorie : " + category + ")");

                    // 🔥 Équiper le tag
                    plugin.getTagManager().setActiveTag(player, tag);

                    // ✅ Convertir le tag en couleur visible en jeu
                    String formattedTag = LegacyComponentSerializer.legacySection().serialize(
                            MiniMessage.miniMessage().deserialize(tagDisplay)
                    );

                    // Envoyer le message avec le tag coloré
                    player.sendMessage(ChatColor.GREEN + "✅ Vous avez équipé le tag : " + formattedTag);
                    return;
                }
            }

            player.sendMessage(ChatColor.RED + "❌ Tag introuvable !");
            plugin.getLogger().warning("⚠️ Aucun tag correspondant trouvé pour " + displayedTagName);
        }


// 📌 Gestion du Menu Admin
        if (ChatColor.stripColor(title).equalsIgnoreCase("⚙️ Gestion des Tags")) {
            String clickedItemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).trim();

            plugin.getLogger().info("📌 AdminMenu Click Detected: " + clickedItemName);

            switch (clickedItemName) {
                case "🔄 Recharger la configuration":
                    plugin.reloadConfig();
                    player.sendMessage(ChatColor.GREEN + "✅ Configuration rechargée !");
                    plugin.getLogger().info("✅ Configuration rechargée par " + player.getName());
                    break;

                case "➕ Ajouter un Tag (à venir)":
                    player.sendMessage(ChatColor.YELLOW + "⚠️ Cette fonctionnalité sera ajoutée dans une future mise à jour !");
                    break;

                case "❌ Supprimer un Tag (à venir)":
                    player.sendMessage(ChatColor.RED + "⚠️ Cette fonctionnalité sera ajoutée dans une future mise à jour !");
                    break;

                default:
                    plugin.getLogger().warning("⚠️ Option inconnue cliquée par " + player.getName() + ": " + clickedItemName);
                    break;
            }

            player.closeInventory();
        }

    }
}
