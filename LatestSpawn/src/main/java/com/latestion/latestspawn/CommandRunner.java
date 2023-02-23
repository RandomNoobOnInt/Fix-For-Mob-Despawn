package com.latestion.latestspawn;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRunner implements CommandExecutor {


    private Latestspawn plugin;

    public CommandRunner(Latestspawn main) {
        this.plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        /*
        help
        list
        add
        remove : chunk
         */

        if (!(player.hasPermission("loadchunk.use") || player.isOp()))
            return false;

        if (args.length == 0) return false;

        if (args[0].equalsIgnoreCase("help")) {
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "/loadchunk: " + ChatColor.RESET + "" + ChatColor.RED
                    + "help, list, add, remove") ;
            return false;
        }

        if (args[0].equalsIgnoreCase("list")) {
            int i = 1;
            for (Chunk c : plugin.chunks) {
                int x = c.getX();
                int z = c.getZ();
                String world = c.getWorld().getName();
                player.sendMessage(i + " " + ChatColor.GREEN + "X: " + x + " || Z: " + z + " || World: " + world);
                i++;
            }
        }

        if (args[0].equalsIgnoreCase("add")) {
            Chunk c = player.getLocation().getChunk();
            if (plugin.chunks.contains(c)) {
                player.sendMessage(ChatColor.RED + "Chunk is already registered.");
                return false;
            }
            plugin.chunks.add(c);
            player.sendMessage(ChatColor.GREEN + "Chunk loaded successfully.");
            return false;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            Chunk c = player.getLocation().getChunk();
            if (!plugin.chunks.contains(c)) {
                player.sendMessage(ChatColor.RED + "Chunk is not registered.");
                return false;
            }
            plugin.chunks.remove(c);
            player.sendMessage(ChatColor.GREEN + "Chunk unloaded successfully.");
            return false;
        }

        return false;
    }
}
