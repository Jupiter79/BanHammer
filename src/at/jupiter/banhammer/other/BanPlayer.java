package at.jupiter.banhammer.other;

import at.jupiter.banhammer.BanHammer;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanPlayer {
    public static List<UUID> beingBanned = new ArrayList<>();

    private final List<Guardian> guardians = new ArrayList<>();
    private final Player victim;

    public BanPlayer(Player victim) {
        beingBanned.add(victim.getUniqueId());

        Bukkit.broadcastMessage("§c" + victim.getName() + " §4is being banned! Say §lgoodbye §r§4to him!");

        this.victim = victim;

        this.spawnGuardians(victim.getLocation());

        this.giveEffects();

        this.rotatePlayer();

        this.lightnings();
    }

    final int GUARDIAN_RADIUS = 7;
    final int GUARDIAN_COUNT = 11;
    private void spawnGuardians(Location loc) {
        List<Location> locs = getCircle(loc, GUARDIAN_RADIUS, GUARDIAN_COUNT);

        locs.forEach(x -> {
            Guardian guardian = (Guardian) loc.getWorld().spawnEntity(x, EntityType.GUARDIAN);

            guardian.setTarget(victim);
            guardian.setAI(false);
            guardian.setInvulnerable(true);
            guardian.setLaser(true);

            this.guardians.add(guardian);
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                rotateGuardians();
            }
        }.runTaskTimer(BanHammer.plugin, 0, 2);
    }

    private double no = 0;
    private void rotateGuardians() {
        for (int i = 0; i < GUARDIAN_COUNT; i++) {
            double angle = 2 * Math.PI * i / GUARDIAN_COUNT;
            Location point = victim.getLocation().clone().add(GUARDIAN_RADIUS * Math.sin(angle + no), 0.0d, GUARDIAN_RADIUS * Math.cos(angle + no));

            guardians.get(i).teleport(point);
        }

        no += 0.07;
    }

    private void giveEffects() {
        victim.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * 15, 1));
    }

    private void rotatePlayer() {
        final int[] count = {1};

        new BukkitRunnable() {
            @Override
            public void run() {
                if (victim.isOnline()) {
                    Location loc = victim.getLocation();

                    loc.setYaw(count[0] * 7);
                    loc.setPitch(0);

                    victim.teleport(loc);

                    count[0]++;
                } else this.cancel();
            }
        }.runTaskTimer(BanHammer.plugin, 20 * 15, 1);
    }

    private void spawnRemains() {
        victim.getWorld().dropItem(victim.getLocation(), new ItemStack(Material.STRING, 3));
        victim.getWorld().dropItem(victim.getLocation(), new ItemStack(Material.GRAY_DYE, 2));
    }

    int delay = 20 * 4;
    private void lightnings() {
        new BukkitRunnable() {
            @Override
            public void run() {
                victim.getWorld().strikeLightningEffect(victim.getLocation());

                if (delay > 1) {
                    delay -= delay * 0.05;

                    lightnings();
                } else {
                    enforceBan();
                    killGuardians();

                    this.cancel();
                }
            }
        }.runTaskLater(BanHammer.plugin, this.delay);
    }

    private void killGuardians() {
        this.guardians.forEach(Entity::remove);
    }

    private void enforceBan() {
        for (int i = 0; i < 3000; i++) {
            this.victim.getWorld().strikeLightningEffect(this.victim.getLocation());
        }

        beingBanned.remove(this.victim.getUniqueId());

        this.victim.kickPlayer("§4§lYOU HAVE BEEN §NPERMANENTLY BANNED!");

        Bukkit.getBanList(BanList.Type.NAME).addBan(this.victim.getName(), "§4§lYOU HAVE BEEN §NPERMANENTLY BANNED", null, "banhammer");

        this.spawnRemains();
    }

    private static List<Location> getCircle(Location origin, int radius, int count) {
        List<Location> locs = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count;
            Location point = origin.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle));

            locs.add(point);
        }

        return locs;
    }
}
