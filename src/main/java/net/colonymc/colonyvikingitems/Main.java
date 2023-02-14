package net.colonymc.colonyvikingitems;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.colonymc.colonyvikingitems.enchants.EnchantBookListeners;
import net.colonymc.colonyvikingitems.inventories.EnchantsListener;
import net.colonymc.colonyvikingitems.inventories.RepairListener;
import net.colonymc.colonyvikingitems.inventories.UpgradeListener;
import net.colonymc.colonyvikingitems.items.ItemType;
import net.colonymc.colonyvikingitems.items.SpecialBookCommand;
import net.colonymc.colonyvikingitems.items.SpecialItemCommand;
import net.colonymc.colonyvikingitems.items.hoenirshopper.HoenirsHopperBlock;
import net.colonymc.colonyvikingitems.items.hoenirshopper.HoenirsHopperListeners;
import net.colonymc.colonyvikingitems.items.runnables.HeimdallsSwordRunnable;
import net.colonymc.colonyvikingitems.items.runnables.OdinsSpearRunnable;
import net.colonymc.colonyvikingitems.npcs.NPCListener;
import net.colonymc.colonyvikingitems.utils.UtilListeners;
import net.colonymc.colonyvikingitems.voth.VothListeners;

public class Main extends JavaPlugin {
	
	static Main instance;
	final File data = new File(this.getDataFolder(), "data.yml");
	static FileConfiguration conf;
	
	public void onEnable() {
		setInstance(this);
		setupConfigFiles();
		initializeVikingCore();
		//setupRunnables();
		System.out.println(" » ColonyVikingItems has been enabled successfully!");
	}
	
	public void onDisable() {
		endRunnables();
		NPCListener.despawnNPCs();
		System.out.println(" » ColonyVikingItems has been disabled successfully!");
	}
	
	private void setupConfigFiles() {
		if(!data.exists()) {
			data.getParentFile().mkdirs();
			saveResource("data.yml", false);
		}
		conf = new YamlConfiguration();
		try {
			conf.load(data);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void initializeVikingCore() {
		NPCListener.setupNPCs();
		Bukkit.getPluginManager().registerEvents(new NPCListener(), this);
		Bukkit.getPluginManager().registerEvents(new UtilListeners(), this);
		Bukkit.getPluginManager().registerEvents(new VothListeners(), this);
		Bukkit.getPluginManager().registerEvents(new UpgradeListener(), this);
		Bukkit.getPluginManager().registerEvents(new EnchantsListener(), this);
		Bukkit.getPluginManager().registerEvents(new RepairListener(), this);
		Bukkit.getPluginManager().registerEvents(new EnchantBookListeners(), this);
		this.getCommand("vikingitem").setExecutor(new SpecialItemCommand());
		this.getCommand("vikingbook").setExecutor(new SpecialBookCommand());
		try {
			for(ItemType t : ItemType.values()) {
				Class<?> cl = Class.forName("net.colonymc.colonyvikingitems.items." + t.encodedName + "." + t.className + "Listeners");
				Bukkit.getPluginManager().registerEvents((Listener) cl.newInstance(), this);
			}
		} catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public void setupRunnables() {
		FileConfiguration conf = Main.getInstance().getDataFileConf();
		if(conf.getConfigurationSection("hoenirsHoppers") != null) {
			for(String s : conf.getConfigurationSection("hoenirsHoppers").getKeys(false)) {
				String world = s.substring(0, s.indexOf(','));
				int x = Integer.parseInt(s.substring(s.indexOf(',') + 1, StringUtils.ordinalIndexOf(s, ",", 2)));
				int y = Integer.parseInt(s.substring(StringUtils.ordinalIndexOf(s, ",", 2) + 1, StringUtils.ordinalIndexOf(s, ",", 3)));
				int z = Integer.parseInt(s.substring(StringUtils.ordinalIndexOf(s, ",", 3) + 1));
				Location loc = new Location(Bukkit.getWorld(world), x, y, z);
				if(loc.getBlock().getType() == Material.HOPPER) {
					HoenirsHopperBlock block = new HoenirsHopperBlock(loc.getBlock(), conf.getInt("hoenirsHoppers." + s));
					HoenirsHopperListeners.hoppers.put(loc.getBlock(), block);
					block.startJob();
				}
				else {
					conf.set("hoenirsHoppers." + s, null);
				}
			}
		}
	}
	
	public void endRunnables() {
		//Odins Spear Runnables
		for(Player p : OdinsSpearRunnable.thrownSpears.keySet()) {
			OdinsSpearRunnable runnable = OdinsSpearRunnable.thrownSpears.get(p);
			runnable.as.remove();
			p.getInventory().addItem(runnable.spear.getItemStack());
			runnable.cancel();
			OdinsSpearRunnable.thrownSpears.remove(p);
		}
		//Heimdalls Sword Runnables
		for(Player p : HeimdallsSwordRunnable.supercharged.keySet()) {
			HeimdallsSwordRunnable runnable = HeimdallsSwordRunnable.supercharged.get(p);
			runnable.toggleItemInHand(p);
			HeimdallsSwordRunnable.supercharged.remove(p);
			runnable.cancel();
		}
		for(HoenirsHopperBlock block : HoenirsHopperListeners.hoppers.values()) {
			block.endJob(null);
			FileConfiguration conf = Main.getInstance().getDataFileConf();
			conf.set("hoenirsHoppers." + block.getLocation().getWorld().getName() + "," + block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ(), block.getTotal());
			saveDataFile(conf);
		}
		
	}

	public static Main getInstance() {
		return instance;
	}
	
	private void setInstance(Main instance) {
		Main.instance = instance;
	}
	
	public FileConfiguration getDataFileConf() {
		return conf;
	}
	
	public  void saveDataFile(FileConfiguration config) {
		try {
			conf = config;
			conf.save(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
