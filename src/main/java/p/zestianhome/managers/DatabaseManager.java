package p.zestianhome.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private final Connection connection;

    public DatabaseManager(String url) throws SQLException {
        connection = DriverManager.getConnection(url);

        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS homes (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "player VARCHAR(36) NOT NULL," +
                        "name TEXT NOT NULL," +
                        "world TEXT NOT NULL," +
                        "x DOUBLE NOT NULL," +
                        "y DOUBLE NOT NULL," +
                        "z DOUBLE NOT NULL);"
        );
        statement.close();
    }

    public Map<String, Location> getHomes(Player player) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM homes WHERE player = ?");
        statement.setString(1, player.getUniqueId().toString());

        ResultSet result = statement.executeQuery();

        Map<String, Location> homes = new HashMap<>();

        while (result.next()) {
            Location location = new Location(
                    Bukkit.getWorld(result.getString("world")),
                    result.getDouble("x"),
                    result.getDouble("y"),
                    result.getDouble("z")
            );
            homes.put(result.getString("name"), location);
        }

        result.close();
        statement.close();

        return homes;
    }

    public void addHome(Player player, String name, Location location) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO homes (player, name, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?);");

        statement.setString(1, player.getUniqueId().toString());
        statement.setString(2, name);
        statement.setString(3, location.getWorld().getName());
        statement.setDouble(4, location.getX());
        statement.setDouble(5, location.getY());
        statement.setDouble(6, location.getZ());

        statement.executeUpdate();
        statement.close();
    }

    public void removeHome(Player player, String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM homes WHERE player = ? AND name = ?");

        statement.setString(1, player.getUniqueId().toString());
        statement.setString(2, name);

        statement.executeUpdate();
        statement.close();
    }
}