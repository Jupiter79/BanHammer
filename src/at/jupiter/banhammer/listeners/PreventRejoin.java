package at.jupiter.banhammer.listeners;

import at.jupiter.banhammer.other.BanPlayer;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PreventRejoin implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (BanPlayer.beingBanned.contains(e.getPlayer().getUniqueId())) {
            Bukkit.getBanList(BanList.Type.NAME).addBan(e.getPlayer().getName(), "§4§lYOU HAVE BEEN §NPERMANENTLY BANNED", null, "banhammer");
        }
    }
}
