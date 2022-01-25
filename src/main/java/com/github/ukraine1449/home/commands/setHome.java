package com.github.ukraine1449.home.commands;

import com.github.ukraine1449.home.Home;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setHome implements CommandExecutor {
    public setHome(Home plugin) {
        this.plugin = plugin;
    }

    Home plugin;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            Location bedLoc = player.getLocation();
            plugin.updateBedPos(player, bedLoc);
            player.sendMessage(ChatColor.GREEN + "Your home location has been updated");
        }

        return false;
    }
}
