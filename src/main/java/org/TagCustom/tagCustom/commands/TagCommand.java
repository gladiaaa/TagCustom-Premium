package org.TagCustom.tagCustom.commands;

import org.TagCustom.tagCustom.TagsCustom;
import org.TagCustom.tagCustom.menu.MainMenu;
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
            sender.sendMessage(ChatColor.RED + "🚫 Cette commande est réservée aux joueurs !");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0 || args[0].equalsIgnoreCase("menu")) {
            new MainMenu(plugin).open(player);
            return true;
        }

        player.sendMessage(ChatColor.RED + "🚫 Commande invalide. Utilisez : /tag menu");
        return true;
    }
}
