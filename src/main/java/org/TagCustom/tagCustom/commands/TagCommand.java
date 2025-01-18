package org.TagCustom.tagCustom.commands;

import org.TagCustom.tagCustom.TagsCustom;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCommand implements CommandExecutor {

    private final TagsCustom plugin;

    public TagCommand(TagsCustom plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Cette commande est réservée aux joueurs !");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0 || args[0].equalsIgnoreCase("list")) {
            // Liste des tags
            player.sendMessage(ChatColor.GREEN + "Voici la liste des tags disponibles :");
            plugin.getConfig().getConfigurationSection("tags").getKeys(false).forEach(tag -> {
                String display = plugin.getConfig().getString("tags." + tag + ".display");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', display));
            });
            return true;
        }

        if (args[0].equalsIgnoreCase("equip")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /tag equip <nom_tag>");
                return true;
            }
            String tagName = args[1];
            if (plugin.getConfig().contains("tags." + tagName)) {
                if (player.hasPermission(plugin.getConfig().getString("tags." + tagName + ".permission"))) {
                    String display = plugin.getConfig().getString("tags." + tagName + ".display");
                    player.sendMessage(ChatColor.GREEN + "Vous avez équipé le tag : " + ChatColor.translateAlternateColorCodes('&', display));
                    // Stocker le tag actif (à implémenter)
                } else {
                    player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission pour ce tag.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Tag introuvable !");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("TagsCustom.admin")) {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission !");
                return true;
            }
            plugin.reloadConfig();
            player.sendMessage(ChatColor.YELLOW + "Configuration rechargée !");
            return true;
        }

        player.sendMessage(ChatColor.RED + "Commande invalide. Utilisez /tag <equip|list|reload>.");
        return true;
    }
}
