package org.TagCustom.tagCustom.commands;

import org.TagCustom.tagCustom.TagsCustom;
import org.TagCustom.tagCustom.menu.TagMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TagCommand implements CommandExecutor {

    private final TagsCustom plugin;
    private final TagMenu tagMenu;

    public TagCommand(TagsCustom plugin) {
        this.plugin = plugin;
        this.tagMenu = new TagMenu(plugin);
        plugin.getLogger().info("✅ TagCommand initialisé avec succès.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.getLogger().info("🔹 Commande exécutée : " + label + " par " + sender.getName());

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "🚫 Cette commande est réservée aux joueurs !");
            plugin.getLogger().warning("❌ Commande utilisée par un non-joueur.");
            return true;
        }

        Player player = (Player) sender;

        // 📂 Commande : /tag menu (ouvrir le menu des catégories)
        if (args.length == 0 || args[0].equalsIgnoreCase("menu")) {
            plugin.getLogger().info("📂 Ouverture du menu des catégories pour " + player.getName());
            tagMenu.openCategoryMenu(player);
            return true;
        }

        // 📜 Commande : /tag list (Lister toutes les catégories et leurs tags)
        if (args[0].equalsIgnoreCase("list")) {
            plugin.getLogger().info("📜 Affichage de la liste des tags pour " + player.getName());
            player.sendMessage(ChatColor.GREEN + "📌 Liste des tags disponibles par catégorie :");

            List<String> categories = plugin.getTagManager().getAllCategories();
            if (categories.isEmpty()) {
                player.sendMessage(ChatColor.RED + "🚫 Aucune catégorie disponible !");
                plugin.getLogger().warning("⚠️ Aucune catégorie trouvée dans la configuration.");
                return true;
            }

            for (String category : categories) {
                player.sendMessage(ChatColor.YELLOW + "📂 Catégorie : " + category);
                List<String> tags = plugin.getTagManager().getTagsByCategory(category);
                for (String tag : tags) {
                    String display = plugin.getTagManager().getTagDisplay(category, tag);
                    player.sendMessage("  ➜ " + ChatColor.translateAlternateColorCodes('&', display));
                }
            }
            return true;
        }

        // ✅ Commande : /tag equip <catégorie> <nom_tag> (Équiper un tag manuellement)
        if (args[0].equalsIgnoreCase("equip")) {
            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "🚫 Utilisation : /tag equip <catégorie> <nom_tag>");
                plugin.getLogger().warning("⚠️ " + player.getName() + " a mal utilisé la commande equip.");
                return true;
            }

            String category = args[1];
            String tagName = args[2];

            plugin.getLogger().info("✅ " + player.getName() + " tente d'équiper le tag : " + tagName + " dans la catégorie : " + category);

            // 🛑 Vérifie si la catégorie et le tag existent
            if (!plugin.getTagManager().getAllCategories().contains(category)) {
                player.sendMessage(ChatColor.RED + "🚫 Catégorie introuvable !");
                plugin.getLogger().warning("❌ Catégorie inexistante : " + category + " pour " + player.getName());
                return true;
            }
            if (!plugin.getTagManager().tagExists(category, tagName)) {
                player.sendMessage(ChatColor.RED + "🚫 Tag introuvable dans cette catégorie !");
                plugin.getLogger().warning("❌ Tag inexistant : " + tagName + " dans la catégorie : " + category);
                return true;
            }

            // ✅ Équipe le tag
            plugin.getTagManager().setActiveTag(player, tagName);
            String display = plugin.getTagManager().getTagDisplay(category, tagName);
            player.sendMessage(ChatColor.GREEN + "✅ Vous avez équipé le tag : " + ChatColor.translateAlternateColorCodes('&', display));
            plugin.getLogger().info("✅ " + player.getName() + " a équipé le tag : " + tagName);
            return true;
        }

        // 🔄 Commande : /tag reload (Recharger la configuration)
        if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("TagsCustom.admin")) {
                player.sendMessage(ChatColor.RED + "🚫 Vous n'avez pas la permission !");
                plugin.getLogger().warning("⚠️ " + player.getName() + " a tenté de recharger la configuration sans permission.");
                return true;
            }
            plugin.reloadConfig();
            player.sendMessage(ChatColor.YELLOW + "🔄 Configuration rechargée !");
            plugin.getLogger().info("🔄 Configuration rechargée par " + player.getName());
            return true;
        }

        // ❌ Commande invalide
        player.sendMessage(ChatColor.RED + "🚫 Commande invalide. Utilisez : /tag <menu|list|equip|reload>.");
        plugin.getLogger().warning("❌ " + player.getName() + " a utilisé une commande invalide : " + String.join(" ", args));
        return true;
    }
}
