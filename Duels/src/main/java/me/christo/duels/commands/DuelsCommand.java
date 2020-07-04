package me.christo.duels.commands;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.events.DisallowedPVPEvent;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.christo.duels.Main;
import me.christo.duels.Utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Copy Right ©
 * This code is private
 * Owner: Christo
 * From: 10/22/19-2023
 * Any attempts to use these program(s) may result in a penalty of up to $1,000 USD
 **/

public class DuelsCommand implements CommandExecutor, Listener {



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;






        Player targetPlayer = Bukkit.getPlayer(args[0]);



        List<String> allowed = Main.getInstance().getConfig().getStringList("AllowedRegions")
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        ApplicableRegionSet ars = WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
        boolean any = StreamSupport.stream(ars.spliterator(), false)
                .anyMatch(region -> allowed.contains(region.getId().toLowerCase()));

        if (args.length == 2)
            if(args.length == 1) {
                p.sendMessage(Util.chat(Main.getInstance().getConfig().getString("InvalidUsage")));
            }

            if(args[0].equalsIgnoreCase("accept")) {
                if (Main.getInstance().getPlayerHash().values().contains(p)) {
                    if(Main.getInstance().getPlayerHash().get(Bukkit.getPlayer(args[1])).equals(p)) {
                        p.sendMessage(Util.chat(Main.getInstance().getConfig().getString("AcceptedDuel").replace("{player}", Bukkit.getPlayer(args[1]).getName())));


                        Player sendPlayer = Bukkit.getPlayer(args[1]);

                        new BukkitRunnable() {
                            int i = 3;
                            @Override
                            public void run() {
                                p.sendMessage(Util.chat(Main.getInstance().getConfig().getString("3")));
                                sendPlayer.sendMessage(Util.chat(Main.getInstance().getConfig().getString("3")));
                            }

                        }.runTaskLaterAsynchronously(Main.getInstance(), 20L);

                        new BukkitRunnable() {
                            int i = 2;
                            @Override
                            public void run() {
                                p.sendMessage(Util.chat(Main.getInstance().getConfig().getString("2")));
                                sendPlayer.sendMessage(Util.chat(Main.getInstance().getConfig().getString("2")));
                            }

                        }.runTaskLaterAsynchronously(Main.getInstance(), 40L);

                        new BukkitRunnable() {
                            int i = 1;
                            @Override
                            public void run() {
                                p.sendMessage(Util.chat(Main.getInstance().getConfig().getString("1")));
                                sendPlayer.sendMessage(Util.chat(Main.getInstance().getConfig().getString("1")));
                            }

                        }.runTaskLaterAsynchronously(Main.getInstance(), 60L);

                        new BukkitRunnable() {
                            int i = 1;
                            @Override
                            public void run() {
                                p.sendMessage(Util.chat(Main.getInstance().getConfig().getString("FightMsg")));
                                sendPlayer.sendMessage(Util.chat(Main.getInstance().getConfig().getString("FightMsg")));
                                Main.getInstance().getPvPHash().put(Bukkit.getPlayer(args[1]), p);
                                Main.getInstance().getPvPHash().put(p, Bukkit.getPlayer(args[1]));
                            }

                        }.runTaskLaterAsynchronously(Main.getInstance(), 80L);



                        return true;
                    }
                }
                return true;
            }


            if (any) {
                if(Main.getInstance().getPlayerHash().values().contains(targetPlayer)) {
                    p.sendMessage(Util.chat(Main.getInstance().getConfig().getString("PlayerInDuel")).replace("{player}", targetPlayer.getName()));
                    return true;
                }
                p.sendMessage(Util.chat(Main.getInstance().getConfig().getString("DuelRequest").replace("{player}", targetPlayer.getName())));
                //Main.getInstance().getPlayerHash().put(targetPlayer, targetPlayer.getUniqueId());
                targetPlayer.sendMessage(Util.chat(Main.getInstance().getConfig().getString("DuelRequestConfirmation").replace("{player}", p.getName())));

                Main.getInstance().getPlayerHash().put(p, targetPlayer);
                Main.getInstance().getPlayerHash().put(targetPlayer, p);
                return true;
            } else {
                p.sendMessage(Util.chat(Main.getInstance().getConfig().getString("DisabledRegionMsg")));
            }
        return false;
    }
}


