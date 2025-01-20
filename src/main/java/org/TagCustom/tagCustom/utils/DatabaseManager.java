package org.TagCustom.tagCustom.utils;

import org.TagCustom.tagCustom.TagsCustom;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private final TagsCustom plugin;
    private Connection connection;

    public DatabaseManager(TagsCustom plugin) {
        this.plugin = plugin;
    }

    /**
     * Connexion à la base de données (MySQL ou SQLite).
     */
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

        plugin.getLogger().info("Connecté à la base de données (" + type + ") avec succès !");
    }

    /**
     * Déconnexion propre de la base de données.
     */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("Connexion à la base de données fermée.");
            } catch (SQLException e) {
                plugin.getLogger().severe("Erreur lors de la fermeture de la base de données : " + e.getMessage());
            }
        }
    }

    /**
     * Initialisation des tables dans la base de données.
     */
    public void initialize() throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS player_tags (" +
                "uuid VARCHAR(36) PRIMARY KEY," +
                "active_tag VARCHAR(64)" +
                ");";

        try (PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
            statement.execute();
        }
    }

    /**
     * Enregistrer le tag actif pour un joueur.
     * Compatible MySQL et SQLite.
     */
    public void saveActiveTag(Player player, String tagId) {
        String type = plugin.getConfig().getString("database.type", "sqlite");
        String query;

        if (type.equalsIgnoreCase("mysql")) {
            // Requête pour MySQL (supporte ON DUPLICATE KEY UPDATE)
            query = "INSERT INTO player_tags (uuid, active_tag) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE active_tag = ?";
        } else {
            // Requête pour SQLite (utilise INSERT OR REPLACE)
            query = "INSERT OR REPLACE INTO player_tags (uuid, active_tag) VALUES (?, ?)";
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, tagId);

            // Ajouter le troisième paramètre pour MySQL
            if (type.equalsIgnoreCase("mysql")) {
                statement.setString(3, tagId);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Erreur lors de l'enregistrement du tag actif : " + e.getMessage());
        }
    }

    /**
     * Récupérer le tag actif pour un joueur.
     */
    public String getActiveTag(Player player) {
        String query = "SELECT active_tag FROM player_tags WHERE uuid = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("active_tag");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Erreur lors de la récupération du tag actif : " + e.getMessage());
        }
        return null;
    }

    /**
     * Supprimer le tag actif pour un joueur.
     */
    public void deleteActiveTag(Player player) {
        String query = "DELETE FROM player_tags WHERE uuid = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Erreur lors de la suppression du tag actif : " + e.getMessage());
        }
    }
}
