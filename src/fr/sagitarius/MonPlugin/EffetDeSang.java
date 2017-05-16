package fr.sagitarius.MonPlugin;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EffetDeSang implements Listener {

	public EffetDeSang(MonPlugin monPlugin) {
	}

	// procedure execute chaque fois qu'une entité recois un coup (nous y compris)
	// et met un effet de desintegration de stone, comme du sang qui gicle
	@EventHandler
	public void onEffetSanguin(EntityDamageByEntityEvent e){
		
		Entity entity = e.getEntity();
		Location entityloc = entity.getLocation();
		
		if(entity.getType() != EntityType.ITEM_FRAME){
			entity.getWorld().playEffect(entityloc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
		}
		
	}
	
}
