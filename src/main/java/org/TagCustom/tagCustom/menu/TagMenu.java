package org.TagCustom.tagCustom.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TagMenu {

    public static Inventory createMenu(Player player, int page, List<String> tags) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Choisissez un tag - Page " + page);

        int slot = 0;
        for (String tag : tags) {
            ItemStack item = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', tag));
            item.setItemMeta(meta);
            inventory.setItem(slot++, item);

            if (slot >= 45) break; // Limite à 45 items par page
        }

        // Navigation
        ItemStack previous = new ItemStack(Material.ARROW);
        ItemMeta prevMeta = previous.getItemMeta();
        prevMeta.setDisplayName(ChatColor.RED + "<< Précédent");
        previous.setItemMeta(prevMeta);
        inventory.setItem(45, previous);

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName(ChatColor.GREEN + "Suivant >>");
        next.setItemMeta(nextMeta);
        inventory.setItem(53, next);

        return inventory;
    }
}
