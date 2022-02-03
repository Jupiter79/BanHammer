package at.jupiter.banhammer.listeners;

import at.jupiter.banhammer.other.BanPlayer;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

public class PreventMoving implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (BanPlayer.beingBanned.contains(e.getPlayer().getUniqueId())) {
            Location from = e.getFrom();
            Location to = e.getTo();

            double X = to.getX() - from.getX();
            double Y = to.getY() - from.getY();
            double Z = to.getZ() - from.getZ();

            boolean hasEffect = e.getPlayer().getPotionEffect(PotionEffectType.LEVITATION) != null;

            if (hasEffect && (X != 0 || Z != 0)) {
                e.setCancelled(true);
            } else if (!hasEffect && (X != 0 || Y != 0 || Z != 0)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        if (BanPlayer.beingBanned.contains(e.getPlayer().getUniqueId())) e.setCancelled(true);
    }
}
