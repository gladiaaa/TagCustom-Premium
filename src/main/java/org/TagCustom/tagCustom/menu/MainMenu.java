package org.TagCustom.tagCustom.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.TagCustom.tagCustom.TagsCustom;

public class MainMenu {
    private final TagsCustom plugin;

    public MainMenu(TagsCustom plugin) {
        this.plugin = plugin;
    }

    /**
     * Ouvre le menu principal pour un joueur.
     * @param player Joueur qui ouvre le menu.
     */
    public void open(Player player) {
        // Création de l'inventaire avec un titre
        Inventory inv = Bukkit.createInventory(null, 27, "📌 Menu Principal");

        // 🔹 Bouton pour accéder aux tags
        ItemStack tagsItem = new ItemStack(Material.BOOK);
        ItemMeta tagsMeta = tagsItem.getItemMeta();
        tagsMeta.setDisplayName("📂 Tags Disponibles");
        tagsItem.setItemMeta(tagsMeta);
        inv.setItem(11, tagsItem); // Ajoute l'item à l'inventaire

        // 🔹 Bouton pour l'administration (visible uniquement si admin)
        if (player.hasPermission("TagsCustom.admin")) {
            ItemStack adminItem = new ItemStack(Material.COMMAND_BLOCK);
            ItemMeta adminMeta = adminItem.getItemMeta();
            adminMeta.setDisplayName("⚙️ Gestion des Tags (Admin)");
            adminItem.setItemMeta(adminMeta);
            inv.setItem(15, adminItem);
        }

        // Ouvre le menu pour le joueur
        player.openInventory(inv);
    }
}
