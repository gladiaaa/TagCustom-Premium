package org.TagCustom.tagCustom.utils;

import org.TagCustom.tagCustom.TagsCustom;
import org.bukkit.entity.Player;
import java.sql.*;

public class DatabaseManager {

    private final TagsCustom plugin;
    private Connection connection;

    public DatabaseManager(TagsCustom plugin) {
        this.plugin = plugin;
    }

    public void connect() throws SQLException {
        String type = plugin.getConfig().getString("database.type", "sqlite");
        String url;

        if (type.equalsIgnoreCase("mysql")) {
            String host = plugin.getConfig().getString("database.mysql.host", "localhost");
            int port = plugin.getConfig().getInt("database.mysql.port", 3306);
            String database = plugin.getConfig().getString("database.mysql.database", "tags");
            String username = plugin.getConfig().getString("database.mysql.username", "root");
            String password = plugin.getConfig().getString("database.mysql.password", "");
            url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
            connection = DriverManager.getConnection(url, username, password);
        } else {
            String sqlitePath = plugin.getDataFolder().getAbsolutePath() + "/tags.db";
            url = "jdbc:sqlite:" + sqlitePath;
            connection = DriverManager.getConnection(url);
        }

        plugin.getLogger().info("🟢 Connecté à la base de données (" + type + ") !");
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("⛔ Déconnexion de la base de données.");
            } catch (SQLException e) {
                plugin.getLogger().severe("❌ Erreur de déconnexion : " + e.getMessage());
            }
        }
    }

    public void initialize() throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS player_tags (" +
                "uuid VARCHAR(36) PRIMARY KEY," +
                "active_tag VARCHAR(64)" +
                ");";
        try (PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
            statement.execute();
        }
    }

    public void saveActiveTag(Player player, String tagId) {
        String query = "INSERT INTO player_tags (uuid, active_tag) VALUES (?, ?) ON DUPLICATE KEY UPDATE active_tag = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, tagId);
            statement.setString(3, tagId);
            statement.executeUpdate();
            plugin.getLogger().info("✅ Tag enregistré en base pour " + player.getName() + " : " + tagId);
        } catch (SQLException e) {
            plugin.getLogger().severe("❌ Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }

    public String getActiveTag(Player player) {
        String query = "SELECT active_tag FROM player_tags WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("active_tag");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("❌ Erreur lors de la récupération du tag : " + e.getMessage());
        }
        return null;
    }

    public void deleteActiveTag(Player player) {
        String query = "DELETE FROM player_tags WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.executeUpdate();
            plugin.getLogger().info("✅ Tag supprimé pour " + player.getName());
        } catch (SQLException e) {
            plugin.getLogger().severe("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }
}
