package com.latestion.latestspawn;

import com.latestion.latestspawn.data.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.crypto.Data;
import java.util.*;

public final class Latestspawn extends JavaPlugin implements Listener {

    public List<Chunk> chunks = new ArrayList<>();
    private DataManager data;

    @Override
    public void onEnable() {

        data = new DataManager(this);
        loadData();
        this.getCommand("loadchunk").setExecutor(new CommandRunner(this));
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        saveData();
    }

    public void loadData() {
        List<String> c = data.getConfig().getStringList("chunks");

        for (String key : c) {
            String[] k = key.split(" , ");
            int x = Integer.parseInt(k[0]);
            int z = Integer.parseInt(k[1]);
            List<String> types = data.getConfig().getStringList(key);
            List<EntityType> t = new ArrayList<>();
            for (String s : types) {
                t.add(EntityType.valueOf(s));
            }
            chunks.add(Bukkit.getWorld(k[2]).getChunkAt(x, z));
        }

    }

    public void saveData() {
        List<String> set = new ArrayList<>();
        for (Chunk c : chunks) {
            int x = c.getX();
            int z = c.getZ();
            String world = c.getWorld().getName();
            set.add(x + " , " + z + " , " + world);
        }
        data.getConfig().set("chunks", set);
        data.saveConfig();
    }

    @EventHandler
    public void onTele(EntityPortalExitEvent event) {
        Chunk c = Objects.requireNonNull(event.getTo()).getChunk();
        if (!chunks.contains(c)) return;
        Entity en = event.getEntity();
        if (!(en instanceof LivingEntity)) return;
        LivingEntity e = (LivingEntity) en;
        e.setRemoveWhenFarAway(false);
    }

}
