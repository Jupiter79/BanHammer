package at.jupiter.banhammer.commands;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class BanHammer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = (Player) commandSender;

        if (commandSender != null) {
            if (p.hasPermission("banhammer.give")) {
                ItemStack hammer = new ItemStack(Material.NETHERITE_AXE, 1);

                ArrayList<String> lore = new ArrayList<>();
                lore.add("§4§lBANHAMMER");
                lore.add("§e§lBANS RULE BREAKERS WITH POWER");

                ItemMeta meta = hammer.getItemMeta();
                meta.setDisplayName("§4§LBAN HAMMER");
                meta.setLore(lore);

                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                hammer.setItemMeta(meta);

                hammer.addEnchantment(Enchantment.DURABILITY, 1);

                p.getInventory().addItem(hammer);

                p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, SoundCategory.MASTER, 1, 1);

                p.sendMessage("§4§lPunch players with this hammer to ban them forever in an epic way!");
            } else p.sendMessage("§cYou have no permission to use that command!");
        }
        return false;
    }
}
