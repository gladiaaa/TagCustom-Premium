package org.TagCustom.tagCustom.listeners;

import org.TagCustom.tagCustom.TagsCustom;
import org.TagCustom.tagCustom.menu.TagMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuClickListener implements Listener {

    private final TagMenu tagMenu;

    public MenuClickListener(TagsCustom plugin) {
        this.tagMenu = new TagMenu(plugin);
        plugin.getLogger().info("✅ MenuClickListener initialisé avec succès.");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getCurrentItem() == null) {
            return;
        }

        // 📌 Transmet l'événement au menu
        tagMenu.handleMenuClick(event);
    }
}
