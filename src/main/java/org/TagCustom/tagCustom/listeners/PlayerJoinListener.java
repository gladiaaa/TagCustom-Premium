package org.TagCustom.tagCustom.listeners;

import org.TagCustom.tagCustom.TagsCustom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final TagsCustom plugin;

    public PlayerJoinListener(TagsCustom plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getTagManager().applyTagToPlayer(event.getPlayer());
    }
}
