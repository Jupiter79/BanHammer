package at.jupiter.banhammer;

import at.jupiter.banhammer.listeners.HammerUse;
import at.jupiter.banhammer.listeners.PreventMoving;
import at.jupiter.banhammer.listeners.PreventRejoin;
import at.jupiter.banhammer.stats.Metrics;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BanHammer extends JavaPlugin {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getServer().getConsoleSender().sendMessage("§aBanHammer v" + getDescription().getVersion() + " has been successfully enabled!");

        getCommand("banhammer").setExecutor(new at.jupiter.banhammer.commands.BanHammer());

        getServer().getPluginManager().registerEvents(new HammerUse(), this);
        getServer().getPluginManager().registerEvents(new PreventMoving(), this);
        getServer().getPluginManager().registerEvents(new PreventRejoin(), this);

        //Metrics
        new Metrics(this, 14134);
    }
}
