package org.TagCustom.tagCustom.commands;

import org.TagCustom.tagCustom.TagsCustom;
import org.TagCustom.tagCustom.menu.TagMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCommand implements CommandExecutor {

    private final TagsCustom plugin;
    private final TagMenu tagMenu;

    public TagCommand(TagsCustom plugin) {
        this.plugin = plugin;
        this.tagMenu = new TagMenu(plugin); // Gestionnaire du menu des tags
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Cette commande est réservée aux joueurs !");
            return true;
        }

        Player player = (Player) sender;

        // Si aucune commande ou "menu" est tapée, ouvrir le menu
        if (args.length == 0 || args[0].equalsIgnoreCase("menu")) {
            tagMenu.openMenu(player, 1); // Ouvre la première page du menu
            return true;
        }

        // Commande pour lister tous les tags
        if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(ChatColor.GREEN + "Liste des tags disponibles :");
            plugin.getConfig().getConfigurationSection("tags").getKeys(false).forEach(tag -> {
                String display = plugin.getConfig().getString("tags." + tag + ".display");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', display));
            });
            return true;
        }

        // Commande pour équiper un tag
        if (args[0].equalsIgnoreCase("equip")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /tag equip <nom_tag>");
                return true;
            }
            String tagName = args[1];
            if (plugin.getConfig().contains("tags." + tagName)) {
                plugin.getTagManager().setActiveTag(player, tagName);
                String display = plugin.getConfig().getString("tags." + tagName + ".display", tagName);
                player.sendMessage(ChatColor.GREEN + "Vous avez équipé le tag : " +
                        ChatColor.translateAlternateColorCodes('&', display));
            } else {
                player.sendMessage(ChatColor.RED + "Tag introuvable !");
            }
            return true;
        }

        // Commande pour recharger la configuration
        if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("TagsCustom.admin")) {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission !");
                return true;
            }
            plugin.reloadConfig();
            player.sendMessage(ChatColor.YELLOW + "Configuration rechargée !");
            return true;
        }

        // Message d'erreur pour une commande invalide
        player.sendMessage(ChatColor.RED + "Commande invalide. Utilisez /tag <menu|list|equip|reload>.");
        return true;
    }
}
