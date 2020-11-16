package net.colonymc.colonyvikingcore.npcs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.colonymc.colonyvikingcore.Main;
import net.colonymc.colonyvikingcore.inventories.EnchantsInventory;
import net.colonymc.colonyvikingcore.inventories.RepairInventory;
import net.colonymc.colonyvikingcore.inventories.UpgradeInventory;

public class NPCListener implements Listener {
	
	static NPC upgradeDwarf;
	static NPC repairDwarf;
	static NPC customEnchants;
	
	public static void setupNPCs() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if(CitizensAPI.getNPCRegistry().getById(58) != null) {
					upgradeDwarf = CitizensAPI.getNPCRegistry().getById(43);
					upgradeDwarf.spawn(new Location(Bukkit.getWorld("hub"), -2, 70, 15));
					upgradeDwarf.setName(ChatColor.translateAlternateColorCodes('&', "&dDurnir"));
					upgradeDwarf.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, "");
					upgradeDwarf.data().setPersistent(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_SIGN_METADATA, "Dj2RFa9Lz+0Bni5vCTh0x15aTjtcmE6O5kjF8rUKS1/IbDO7AhbTk0+7AXoJbJC/FKzu0CJAzenTbwThHCUEFUtv+CLgqdYCg+OihfS7O6Ve+pj9qh7lj758ZX2UpSjbxNJfstu2Su/RrZBr0su475NsB51Nb2/GLrW9D3zM3B7M+ZV1r9Ehh911X1romcxLZxVw7NgX3isTWDFKG7aov+y+jAYKbHPcXvotVJmxfDb/yhVS3gjfr3KoA6VW9in77+303YVo93CUzFzWMvUhiHdc4oBJXPky3naDZ5HChfoN75ueQP3BBtP/LD5jotViR6PHF/1iAhxre7q88M1W/cvV19cX1AiSUliXVXZpT5SXAD7eQ+YZrVNYHPw81aEH+6MJuRdJNxczEMrn22hY2+GE2t4mXR+/WrHNgKpQhaxPvA2sl8eHQaK2hGDf3oLEWgvqwzwix5sNL271o1Lxcywhg0E7brovzwMNwxgPAPkA9U70sshWL5YIzsVB15zwG7VGfqVi1Vhdb4gMN8Cq2/svKTEVJ8237mn0OG7uF+cg+ZQPkZOcfrjJokpsIk6xMYGEHHYcKPUWv4Zs9sgx3br5eJD+wGT2iH6r4yNAsFD3K3Not6kFzuJpasTK8kaKfcKNtnoRnYbP4Gp1uIDKt1rnTVJKSGn8hEdSu4aEfP0=");
					upgradeDwarf.data().setPersistent(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_METADATA, "eyJ0aW1lc3RhbXAiOjE1ODY1MzkwNDM4MTQsInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTljMTlkZDBiMTUxNWJhODRiNjY1ZmQ3NDU5MmEzNzU4ZWY0YjI5NTNjZmQzMWFhNjM3YzA4MmZiMmY3ZjNmMiJ9fX0=");
					repairDwarf = CitizensAPI.getNPCRegistry().getById(51);
					repairDwarf.spawn(new Location(Bukkit.getWorld("hub"), -2, 70, 13));
					repairDwarf.setName(ChatColor.translateAlternateColorCodes('&', "&dBrokkr"));
					repairDwarf.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, "");
					repairDwarf.data().setPersistent(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_SIGN_METADATA, "Mr4wG/tAjFLtzCwOlO2zQcGADCX4goyWvGAFE7Ld794geQDgTxiY7zSl3xVmLaor0GAdHJOzZGm6fobKC8JA6OIoZTrcWBsW0kVUMiSRP+zQHfW/KQGiHAcAsKNEGnQnhl5yYPF5h/mrPDOpnww5m6BuF18nW4D9OPXSrnaDDqvY5WnUGKlMPPg+Q332OUzO8W1JW66gztpBDpbOEUQHWZqOO0Bs7ZZ5SGfBfDFu3Zvf140ZKvoMaYQA4RHwH5b5gbmEaStqR+NgFF5YYT9hC3JCxjjDJ1b9nnuqUQZezy2gp0uPWqc9ACpYJrhVryBbuYZeOkHUMCdyLe06ecRZeFVSEGPvHGhYswSUHSFBJINmcGm+3D372V2nRNWNOVbnnh1YM6vQOxZ/TlTOeRZMnLEabY3oM3dw0Uu/JcKA+tlXbmIMhR0qmGN0/7cU4z230uWGbh1VWsrlVIZ7/XOgYnpt0gDgCPz3ppcS01IXKCHfLoOeLew/cWGeoH1/2ODpSDx2Uw6fcP8/7Y5qjRQzRPjcgvfsXSrECagoNsmavAdgbJK6yopB40Z4PimluFAtC+OH5V/3mAJgyju5uMCJ6HojSQDxcMPLuNiCbiXsTuumvFmeZ4TCcs6ZcsoW8guXIdJ6Jc/FWUODVx303y38qUhsXSihcLXFlv+k315xryc=");
					repairDwarf.data().setPersistent(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_METADATA, "eyJ0aW1lc3RhbXAiOjE1ODYyNTE5ODQ5NDgsInByb2ZpbGVJZCI6IjczODJkZGZiZTQ4NTQ1NWM4MjVmOTAwZjg4ZmQzMmY4IiwicHJvZmlsZU5hbWUiOiJZYU9PUCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGYwNzkwZDk3NThiMTRjZTVmNDE3ODE2YzAxNTcyODc0NWE1ZDhhNDMwNDhhOWQwYTQxZDBlMTY4ZWQyN2Q2MCJ9fX0=");
					customEnchants = CitizensAPI.getNPCRegistry().getById(52);
					customEnchants.spawn(new Location(Bukkit.getWorld("hub"), -2, 70, 10));
					customEnchants.setName(ChatColor.translateAlternateColorCodes('&', "&dGalar"));
					customEnchants.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, "");
					customEnchants.data().setPersistent(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_SIGN_METADATA, "jhjaFexn4bzul12T88PyyA4h1IiY9SLX+uTXMhiSl6SjlGCMRri1JQPmCamRz7Hxgw7BsAvsMlqlPE224U8j3LLJ/pg93EsQN+t86G+aDzNpPaq7hf4UEB/5RgvJus2+zXTJ0+TpNeCBknaxsfBubyQQ2ypR5air/BirJMYHH3YafR9npokYzy56/2qvx6lKCjq+ErUfg1hDangwFk2z6dnOHDvEtW54ACuSO6Bdkl0Ngks0kdFcgEKLff86UrVPnBGbA6WIXr5B+kdvaNrLj5HCjSsY6VWI6ZMPhQYGHXcu7wRnXNGUL0M1q6HFGnfau0HDwtjAIUSzxuS+6uglOo2ymy7khdazbYgS3RCykdbvo6PJuXbnypgoiqkrwqxl/eKNcIEYB+HENwbbceOaxxDOXnPJ4r/tygNZMLf3gD2ujac1Z3UsY1n0oS6neOo6n3mpjFhSHPbLnhI4C0uVAdHcmDsJ/4drMA+5YTy7rSWWz75Nt84hEyCmdgZc2wqfR6ey70ps6Fpfaenku7twavqIfMVx9OtPH898cPgLOM2a8aAG2E/bBMisHY3gzFS7BRPeuW7dv4L0417PLzYXkHXjlzP8ffLo/EiygJBtytt7ePqkr1IG48VjEBZqOdLqrb/Fg8gaqCX4+XGOR4Yq5AXLeLYnv5ZMT2aLHCZzZtw=");
					customEnchants.data().setPersistent(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_METADATA, "eyJ0aW1lc3RhbXAiOjE1NzczMTk0MDMzMDUsInByb2ZpbGVJZCI6Ijc1MTQ0NDgxOTFlNjQ1NDY4Yzk3MzlhNmUzOTU3YmViIiwicHJvZmlsZU5hbWUiOiJUaGFua3NNb2phbmciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRjMmE1ZThlNmM4YjZjMmZhNDgzOWI2M2ZlZWUxNzM2NGM4YzExMmI0ODY4NjI0OGY2MTI3MTliNDRmM2ZkZTQifX19");
					cancel();
				}
			}
		}.runTaskTimer(Main.getInstance(), 0L, 5L);
	}
	
	public static void despawnNPCs() {
		if(upgradeDwarf != null && upgradeDwarf.isSpawned()) {
			upgradeDwarf.despawn();
		}
		if(repairDwarf != null && repairDwarf.isSpawned()) {
			repairDwarf.despawn();
		}
		if(customEnchants != null && customEnchants.isSpawned()) {
			customEnchants.despawn();
		}
	}
	
	@EventHandler
	public void onClick(NPCRightClickEvent e) {
		if(e.getNPC().equals(upgradeDwarf)) {
			Player p = e.getClicker();
			p.openInventory(new UpgradeInventory(p).getInventory());
		}
		else if(e.getNPC().equals(repairDwarf)) {
			Player p = e.getClicker();
			p.openInventory(new RepairInventory(p).getInventory());
		}
		else if(e.getNPC().equals(customEnchants)) {
			Player p = e.getClicker();
			p.openInventory(new EnchantsInventory(p).getInventory());
		}
	}
	
	@EventHandler
	public void onClick(NPCLeftClickEvent e) {
		if(e.getNPC().equals(upgradeDwarf)) {
			Player p = e.getClicker();
			p.openInventory(new UpgradeInventory(p).getInventory());
		}
		else if(e.getNPC().equals(repairDwarf)) {
			Player p = e.getClicker();
			p.openInventory(new RepairInventory(p).getInventory());
		}
		else if(e.getNPC().equals(customEnchants)) {
			Player p = e.getClicker();
			p.openInventory(new EnchantsInventory(p).getInventory());
		}
	}

}
