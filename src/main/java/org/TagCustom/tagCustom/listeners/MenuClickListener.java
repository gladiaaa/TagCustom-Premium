package org.TagCustom.tagCustom.listeners;

import org.TagCustom.tagCustom.TagsCustom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuClickListener implements Listener {

    private final TagsCustom plugin;

    /**
     * Constructeur prenant une référence au plugin principal.
     *
     * @param plugin L'instance principale du plugin TagsCustom
     */
    public MenuClickListener(TagsCustom plugin) {
        this.plugin = plugin;
    }

    /**
     * Gère les clics dans l'inventaire.
     *
     * @param event L'événement de clic dans un inventaire
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Vérifier si le titre de l'inventaire correspond à votre menu
        if (event.getView().getTitle().equalsIgnoreCase("Choisissez un tag")) {
            event.setCancelled(true); // Empêcher toute interaction avec l'inventaire

            // Vérifier si l'élément cliqué est valide
            if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) {
                return;
            }

            String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

            // Exemple : Gérer les clics sur les tags
            if (itemName.startsWith("Tag")) {
                event.getWhoClicked().sendMessage("Vous avez sélectionné : " + itemName);
                // Logique supplémentaire pour équiper le tag peut être ajoutée ici
            }

            // Exemple : Gérer la navigation
            if (itemName.equalsIgnoreCase("<< Précédent")) {
                event.getWhoClicked().sendMessage("Page précédente sélectionnée.");
                // Ajoutez ici votre logique pour changer de page
            } else if (itemName.equalsIgnoreCase("Suivant >>")) {
                event.getWhoClicked().sendMessage("Page suivante sélectionnée.");
                // Ajoutez ici votre logique pour changer de page
            }
        }
    }
}
