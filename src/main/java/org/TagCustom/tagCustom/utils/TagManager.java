package org.TagCustom.tagCustom.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TagManager {
    private static final Map<Player, String> activeTags = new HashMap<>();

    public static void setActiveTag(Player player, String tag) {
        activeTags.put(player, tag);
    }

    public static String getActiveTag(Player player) {
        return activeTags.getOrDefault(player, null);
    }

    public static void removeActiveTag(Player player) {
        activeTags.remove(player);
    }
}
