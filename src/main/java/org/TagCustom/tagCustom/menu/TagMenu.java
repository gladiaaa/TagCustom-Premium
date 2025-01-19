package org.TagCustom.tagCustom.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
    }

    public void openMenu(Player player, int page) {
        int rows = plugin.getConfig().getInt("menu.rows", 6);
        String title = plugin.getConfig().getString("menu.title", "Choisissez un tag");
        Inventory inventory = Bukkit.createInventory(null, rows * 9, title + " - Page " + page);

        List<String> tags = tagManager.getAllTags();
        int maxItemsPerPage = (rows - 2) * 9; // Dernière ligne réservée pour navigation et tag actif
        int startIndex = (page - 1) * maxItemsPerPage;
        int endIndex = Math.min(startIndex + maxItemsPerPage, tags.size());

        // Ajouter les tags dans le menu
        for (int i = startIndex; i < endIndex; i++) {
            String tagId = tags.get(i);
            String miniMessageDisplay = plugin.getConfig().getString("tags." + tagId + ".display", "[Tag]");

            // Convertir MiniMessage en legacy pour afficher dans le menu
            Component component = MiniMessage.miniMessage().deserialize(miniMessageDisplay);
            String legacyDisplay = LegacyComponentSerializer.legacySection().serialize(component);

            ItemStack item = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(legacyDisplay); // Texte formaté
            meta.setLore(List.of("ID : " + tagId)); // Ajouter l'ID du tag dans la description
            item.setItemMeta(meta);

            inventory.addItem(item);
        }

        // Ajouter navigation
        if (page > 1) {
            ItemStack previous = createNavigationItem(Material.ARROW, plugin.getConfig().getString("menu.navigation.previous", "<< Précédent"));
            inventory.setItem((rows * 9) - 9, previous);
        }

        if (endIndex < tags.size()) {
            ItemStack next = createNavigationItem(Material.ARROW, plugin.getConfig().getString("menu.navigation.next", "Suivant >>"));
            inventory.setItem((rows * 9) - 1, next);
        }

        // Ajouter un item pour afficher/retirer le tag actif
        String activeTagId = tagManager.getActiveTag(player);
        String activeTagDisplay = (activeTagId != null)
                ? plugin.getConfig().getString("tags." + activeTagId + ".display", "<gray>Pas de tag actif")
                : "<gray>Pas de tag actif";
        Component activeTagComponent = MiniMessage.miniMessage().deserialize(activeTagDisplay);
        String legacyActiveTag = LegacyComponentSerializer.legacySection().serialize(activeTagComponent);

        ItemStack activeTagItem = new ItemStack(Material.PAPER); // Exemple : item papier
        ItemMeta activeMeta = activeTagItem.getItemMeta();
        activeMeta.setDisplayName(legacyActiveTag);
        activeMeta.setLore(List.of("§7Cliquez pour retirer le tag actif"));
        activeTagItem.setItemMeta(activeMeta);
        inventory.setItem((rows * 9) - 5, activeTagItem); // Mettre au centre de la dernière ligne

        // Remplir avec des items de remplissage
        ItemStack filler = createFillerItem();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, filler);
            }
        }

        player.openInventory(inventory);
    }

    private ItemStack createNavigationItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createFillerItem() {
        String fillerMaterial = plugin.getConfig().getString("menu.filler", "GRAY_STAINED_GLASS_PANE");
        Material material = Material.matchMaterial(fillerMaterial);
        if (material == null) material = Material.GRAY_STAINED_GLASS_PANE;
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }

    public void handleMenuClick(InventoryClickEvent event, int page) {
        if (event.getClickedInventory() == null || !event.getView().getTitle().contains("Choisissez un tag")) {
            return; // Ne rien faire si ce n'est pas notre menu
        }

        event.setCancelled(true); // Empêcher les interactions

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return; // Ignore les clics sur des cases vides
        }

        if (clickedItem.getType() == Material.ARROW) {
            // Navigation entre les pages
            String itemName = clickedItem.getItemMeta().getDisplayName();
            if (itemName.equals(plugin.getConfig().getString("menu.navigation.previous", "<< Précédent"))) {
                openMenu(player, page - 1);
            } else if (itemName.equals(plugin.getConfig().getString("menu.navigation.next", "Suivant >>"))) {
                openMenu(player, page + 1);
            }
            return;
        }

        if (clickedItem.getType() == Material.PAPER) {
            // Retirer le tag actif
            tagManager.setActiveTag(player, null);
            player.sendMessage("§eVous avez retiré votre tag actif.");
            openMenu(player, page); // Réafficher le menu
            return;
        }

        if (clickedItem.getType() == Material.NAME_TAG) {
            // Récupérer l'ID depuis la description de l'item
            List<String> lore = clickedItem.getItemMeta().getLore();
            if (lore == null || lore.isEmpty() || !lore.get(0).startsWith("ID : ")) {
                player.sendMessage("§cErreur : Impossible d'identifier le tag sélectionné.");
                return;
            }

            String selectedTagId = lore.get(0).substring(5); // Extraire l'ID du tag
            if (selectedTagId != null && plugin.getConfig().contains("tags." + selectedTagId)) {
                // Équiper le tag
                tagManager.setActiveTag(player, selectedTagId);

                String message = "<green>Vous avez équipé le tag : </green>" + plugin.getConfig().getString("tags." + selectedTagId + ".display", "[Tag]");
                Component formattedMessage = MiniMessage.miniMessage().deserialize(message);
                player.sendMessage(LegacyComponentSerializer.legacySection().serialize(formattedMessage));

                player.closeInventory();
            } else {
                player.sendMessage("§cErreur : Tag introuvable ou non valide.");
            }
        }
    }
}
