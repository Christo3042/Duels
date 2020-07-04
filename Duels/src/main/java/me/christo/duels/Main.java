package me.christo.duels;

import me.christo.duels.commands.DuelsCommand;
import me.christo.duels.listener.PvPListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {


    private static Main instance;

    @Override
    public void onEnable() {

        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists())
            saveDefaultConfig();

        getCommand("duel").setExecutor((CommandExecutor) new DuelsCommand());
        this.getServer().getPluginManager().registerEvents(new PvPListener(), this);
        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return instance;
    }

    public HashMap<Player, Player> getPlayerHash() {
        return playerHash;
    }

    HashMap<Player, Player> playerHash = new HashMap<>();

    public HashMap<Player, Player> getPvPHash() {
        return pvpHash;
    }

    HashMap<Player, Player> pvpHash = new HashMap<>();
}
