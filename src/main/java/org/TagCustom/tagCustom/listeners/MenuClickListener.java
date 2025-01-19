package org.TagCustom.tagCustom.listeners;

import org.TagCustom.tagCustom.TagsCustom;
import org.TagCustom.tagCustom.menu.TagMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuClickListener implements Listener {

    private final TagsCustom plugin;
    private final TagMenu tagMenu;

    public MenuClickListener(TagsCustom plugin) {
        this.plugin = plugin;
        this.tagMenu = new TagMenu(plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (title.startsWith(plugin.getConfig().getString("menu.title", "Sélectionnez un tag"))) {
            int page = Integer.parseInt(title.split("Page ")[1]); // Récupérer le numéro de page
            tagMenu.handleMenuClick(event, page);
        }
    }
}
