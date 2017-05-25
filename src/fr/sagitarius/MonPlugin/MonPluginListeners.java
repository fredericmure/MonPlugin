package fr.sagitarius.MonPlugin;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class MonPluginListeners implements Listener {

	@EventHandler										// Affiche les coordonnees du joueur qui viens de mourir, et uniquement a lui...
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity().getPlayer();		// Recupere le joueur
        System.out.println("Le joueur "+player.getDisplayName()+" est mort...");	// est affiche sa mort sur la console...
        Location LocPlayer = player.getLocation();		// coordonnee du joueur mort, et ensuite les affiche sur chat du joueur et les sauvegarder...
        MonPlugin.getInstance().getConfig().set("Sauvegarde."+player.getName()+"_Mort.Monde", LocPlayer.getWorld().getName());		// Ajoute le Monde dans le fichier de config du joueur
        MonPlugin.getInstance().getConfig().set("Sauvegarde."+player.getName()+"_Mort.LocX", LocPlayer.getBlockX());		// Ajoute LocX dans le fichier de config du joueur mort
        MonPlugin.getInstance().getConfig().set("Sauvegarde."+player.getName()+"_Mort.LocY", LocPlayer.getBlockY());		// Ajoute LocY dans le fichier de config du joueur mort
        MonPlugin.getInstance().getConfig().set("Sauvegarde."+player.getName()+"_Mort.LocZ", LocPlayer.getBlockZ());		// Ajoute LocZ dans le fichier de config du joueur mort
		MonPlugin.getInstance().saveConfig();
        player.sendMessage("�b["+player.getDisplayName()+"] �2Mort en "+LocPlayer.getBlockX()+" , "+LocPlayer.getBlockY()+" , "+LocPlayer.getBlockZ());
    }
	
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {			// PlayerJoinEvent est l'�v�nement de connexion d'un joueur sur le serveur
	
		Player player = e.getPlayer();							// On r�cup�re le joueur qui vient de se connecter � partir de l'�v�nement
		player.sendMessage("Bienvenue sur le serveur FK de Clement !");				// On lui envoie un message
		if(!player.hasMetadata("Team")){																	// On v�rifie si d�ja TAG pour  Couleur de Base
			String CouleurTeam = MonPlugin.getInstance().getConfig().getString(player.getName());			// Pas encore de TAG on vas donc chercher si Joueur existe dans fichier de config
			if (CouleurTeam != null){				
				player.sendMessage("votre couleur de Base est le "+CouleurTeam);							// Si c'est le cas, on lui TAG la couleur d�fini
				player.setMetadata("Team", new FixedMetadataValue(MonPlugin.getInstance(), CouleurTeam));
			} else {																						// Sinon il vas falloir d�finir une Couleur de Base
				CouleurTeam = MonPlugin.getInstance().getConfig().getString("CouleurDefault");				// R�cup�re la couleur par defaut
				MonPlugin.getInstance().getConfig().set(player.getName(), CouleurTeam);						// Ajoute le joueur dans la Config
				MonPlugin.getInstance().saveConfig();														// sauvegarde le fichier de config.
				player.sendMessage("Vous n'avez pas de Couleur de Team pr�d�fini... La nouvelle est le "+CouleurTeam);
				player.setMetadata("Team", new FixedMetadataValue(MonPlugin.getInstance(), CouleurTeam));	// Tage le joueur avec la couleur...
			}
		} else {
			player.sendMessage("DEJA TAG Couleur de Base...");								// Le joueur � d�ja le TAG, d�co/Reco...
		}
	}
	
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){			// PlayerQuitEvent est l'�v�nement de fin de connexion d'un joueur sur le serveur
		Player player = e.getPlayer();
		player.sendMessage("A Bient�t sur le serveur FK de Clement !");					// On lui envoie un message prive
		System.out.println("Le joueur "+player.getDisplayName()+" a quite le jeux !");	// est aussi sur la console...
	}
	
	
	@EventHandler
	public void onEffetSanguin(EntityDamageByEntityEvent e){
		
		Entity entity = e.getEntity();
		Location entityloc = entity.getLocation();
		
		if(entity.getType() != EntityType.ITEM_FRAME){
			entity.getWorld().playEffect(entityloc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
		}
		
	}
	
}
