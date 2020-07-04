package me.christo.duels.listener;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.events.DisallowedPVPEvent;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.christo.duels.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Copy Right ©
 * This code is private
 * Owner: Christo
 * From: 10/22/19-2023
 * Any attempts to use these program(s) may result in a penalty of up to $1,000 USD
 **/

public class PvPListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void PlayerHitEvent(DisallowedPVPEvent e) {


        List<String> allowed = Main.getInstance().getConfig().getStringList("AllowedRegions")
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        ApplicableRegionSet ars = WGBukkit.getRegionManager(e.getAttacker().getWorld()).getApplicableRegions(e.getAttacker().getLocation());
        boolean any = StreamSupport.stream(ars.spliterator(), false)
                .anyMatch(region -> allowed.contains(region.getId().toLowerCase()));

        if(any) {
            if(!(Main.getInstance().getPvPHash().values().contains(e.getAttacker()))) {
                return;
            }
            if (Main.getInstance().getPvPHash().values().contains(e.getDefender())) {
                if (Main.getInstance().getPvPHash().get(e.getAttacker()).equals(e.getDefender())) {
                    if (Main.getInstance().getPvPHash().get(e.getDefender()).equals(e.getAttacker())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(!Main.getInstance().getPvPHash().values().contains(e.getEntity())) {
            return;
        }
        if (Main.getInstance().getPvPHash().values().contains(e.getEntity())) {
            Main.getInstance().getPvPHash().remove(e.getEntity());
            Main.getInstance().getPvPHash().remove(e.getEntity().getKiller());
        }
    }
}


