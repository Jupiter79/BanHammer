package at.jupiter.banhammer.listeners;

import at.jupiter.banhammer.BanHammer;
import at.jupiter.banhammer.other.BanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HammerUse implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player victim = (Player) e.getEntity();

            if (BanPlayer.beingBanned.contains(victim.getUniqueId())) e.setCancelled(true);

            if (damager.getInventory().getItemInMainHand().getType() == Material.NETHERITE_AXE) {
                ItemStack item = damager.getEquipment().getItemInMainHand();

                if (item.getItemMeta() != null) {
                    ItemMeta meta = item.getItemMeta();

                    if (meta.getLore() != null && meta.getLore().get(0) != null && meta.getLore().get(0).equals("§4§lBANHAMMER")) {
                        if (damager.hasPermission("banhammer.use")) {
                            if (!BanPlayer.beingBanned.contains(victim.getUniqueId())) {
                                e.setCancelled(true);

                                victim.getLocation().getWorld().playSound(victim.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);

                                Bukkit.getOnlinePlayers().forEach(x -> x.playSound(x.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1));

                                new BanPlayer(victim);
                            } else damager.sendMessage("§cThis player is already being banned!");
                        } else damager.sendMessage("§cYou are not strong enough to use this hammer!");
                    }
                }
            }
        }
    }
}
