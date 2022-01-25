package com.github.ukraine1449.home.commands;

import com.github.ukraine1449.home.Home;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class playerJoinEvent implements Listener {
    Home plugin;

    public playerJoinEvent(Home plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) throws Exception {

        Player player = event.getPlayer();
        plugin.onPlayerJoinSQL(player);
    }
    }
