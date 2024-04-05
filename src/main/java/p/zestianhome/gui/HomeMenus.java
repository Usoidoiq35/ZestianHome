package p.zestianhome.gui;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import p.zestianhome.ZestianHome;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeMenus {
    public static void openHomes(Player player) {
        Map<String, Location> homes = null;

        try {
            homes = ZestianHome.getInstance().getDatabase().getHomes(player);
        } catch (SQLException e) {
            player.sendMessage(ZestianHome.getInstance().getMessage("gui.error"));
            return;
        }

        if (homes.isEmpty()) {
            player.sendMessage(ZestianHome.getInstance().getMessage("gui.no-homes"));
            return;
        }

        PagedGui gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# . . . . . . . #",
                        "# . . . . . . . #",
                        "# . . . . . . . #",
                        "# # # # # # # # #"
                )
                .setContent(homes.entrySet().stream()
                        .map(home -> new HomeItem(ZestianHome.getInstance(), home.getKey(), home.getValue()))
                        .collect(Collectors.toList()))
                .addIngredient('#', new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL).build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle("Homes")
                .setGui(gui)
                .build();

        window.open();
    }
}
