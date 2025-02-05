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

public class AdminMenu {
    private final TagsCustom plugin;

    public AdminMenu(TagsCustom plugin) {
        this.plugin = plugin;
    }

    /**
     * Ouvre le menu d'administration pour un joueur.
     * @param player Joueur qui ouvre le menu.
     */
    public void open(Player player) {
        // ✅ Formater le titre du menu avec une couleur
        String formattedTitle = LegacyComponentSerializer.legacySection().serialize(
                MiniMessage.miniMessage().deserialize("<red>⚙️ Gestion des Tags</red>")
        );

        Inventory inv = Bukkit.createInventory(null, 27, formattedTitle);

        // 🔄 Bouton Recharger la configuration
        ItemStack reloadItem = new ItemStack(Material.REDSTONE);
        ItemMeta reloadMeta = reloadItem.getItemMeta();
        String formattedReload = LegacyComponentSerializer.legacySection().serialize(
                MiniMessage.miniMessage().deserialize("<green>🔄 Recharger la configuration</green>")
        );
        reloadMeta.setDisplayName(formattedReload);
        reloadItem.setItemMeta(reloadMeta);
        inv.setItem(11, reloadItem);

        // ➕ Bouton Ajouter un Tag (fonctionnalité future)
        ItemStack addItem = new ItemStack(Material.ANVIL);
        ItemMeta addMeta = addItem.getItemMeta();
        String formattedAdd = LegacyComponentSerializer.legacySection().serialize(
                MiniMessage.miniMessage().deserialize("<gold>➕ Ajouter un Tag (à venir)</gold>")
        );
        addMeta.setDisplayName(formattedAdd);
        addItem.setItemMeta(addMeta);
        inv.setItem(13, addItem);

        // ❌ Bouton Supprimer un Tag (fonctionnalité future)
        ItemStack deleteItem = new ItemStack(Material.BARRIER);
        ItemMeta deleteMeta = deleteItem.getItemMeta();
        String formattedDelete = LegacyComponentSerializer.legacySection().serialize(
                MiniMessage.miniMessage().deserialize("<red>❌ Supprimer un Tag (à venir)</red>")
        );
        deleteMeta.setDisplayName(formattedDelete);
        deleteItem.setItemMeta(deleteMeta);
        inv.setItem(15, deleteItem);

        player.openInventory(inv);
    }
}
