package com.github.ukraine1449.home.commands;

import com.github.ukraine1449.home.Home;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class goHome implements CommandExecutor {
Home plugin;

    public goHome(Home plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(plugin.preferStored){

                if(!(player.getBedSpawnLocation() == null)){
                    Location playerBedLoc = player.getBedSpawnLocation();
                    player.teleport(playerBedLoc);
                    player.sendMessage(ChatColor.GREEN + "You have been teleported to your bed location");
                }else{
                    player.sendMessage(ChatColor.RED + "You do not have a bed or a home set.");
                }
            }

        }
        return false;
    }
}
