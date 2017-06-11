package fr.sagitarius.MonPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class MonPluginListeners implements Listener {
	
	public int Nbr;


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
        player.sendMessage("§b["+player.getDisplayName()+"] §2Mort en "+LocPlayer.getBlockX()+" , "+LocPlayer.getBlockY()+" , "+LocPlayer.getBlockZ());
    }
	
	
	// -------------------------------------------------------------------------------------------------------------------------------
	//		Syteme de QUETE pour jouer a celui qui termine les quetes avant les autres joeurs ...
	// -------------------------------------------------------------------------------------------------------------------------------
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {				// Appelé lorsq'un inventaire quelconque est fermer...
		
		if(MonPlugin.getInstance().isPartieActive()) {					// si pas en cours de jeux, alors ne verifie pas les Quetes...
			
			if(e.getInventory().getName().equalsIgnoreCase("quete")){		// vérifie que c'est le coffre de la quete...
				Player player = (Player) e.getPlayer(); 
				player.sendMessage(ChatColor.GREEN+"Ceci est le Coffre de la quete...");	// c'est le bon coffre, on vérifie le contenue...
				ItemStack[] inventaire = e.getInventory().getContents();
				int case_inv = 0;
				for( ItemStack invQete : inventaire) {
					if(invQete != null){									// regarde que si la case n'est pas nulle...
						VerifQuete(invQete, case_inv, player, e.getInventory());
					}
					case_inv++;
				}
			}
		}
	}
	
	private void VerifQuete(ItemStack invQete, int case_inv, Player player, Inventory inventory){		// Verifie les divers Quetes...
		
		Location LocQuete = inventory.getLocation();							// emplacement d'un des coffres pour les Quetes...
		String playercouleur = player.getMetadata("Team").get(0).asString();	// récupère couleur du joueur
		
		if(playercouleur.equalsIgnoreCase("bleu")) {							// traitement si team BLEU
			for(Quete_Bleu qt : Quete_Bleu.values()){							// on boucle pour chaqune de nos Quetes... ==BLEU==
				if(invQete.getType().equals(qt.getMaterial())) {
					if(qt.getActuel()>=qt.getMini()){							// on vérifi si la quete n'est déja pas termine...
						player.sendMessage(ChatColor.GREEN+"Cette quete est deja termine...");
						inventory.clear(case_inv);								// on vide cet inventaire la pour ne pas le laisser aux autres joueurs...
						return;													// la qete est deja faite, on continu sans rien faire...
					}
					player.getWorld().playEffect(LocQuete, Effect.STEP_SOUND , Material.LAPIS_BLOCK);	// petit effet de lapis qui eclate...
					Nbr = qt.getActuel()+invQete.getAmount();					// pas deja termine, alors on fait le cumul... 
					qt.setActuel(Nbr);
					inventory.clear(case_inv);									// vide la case contenant le PORK...
					if(Nbr>=qt.getMini()){ 										// regarde si la Quete est termine...
						player.sendMessage(ChatColor.BOLD+""+ChatColor.GREEN+qt.getMessage()+" --> REUSSI, félicitation...");
						qt.setTermine(true);									// precise que cette quete est termine
					} else {
						player.sendMessage(ChatColor.GREEN+""+Nbr+" "+qt.name()+" sur "+qt.getMini()+"...");
						qt.setTermine(false);									// ou ne l'est pas...
					}
				}
			}
		} else {																// Sinon traitement pour team ROUGE
			for(Quete_Rouge qt : Quete_Rouge.values()){							// on boucle pour chaqune de nos Quetes... ==ROUGE==
				if(invQete.getType().equals(qt.getMaterial())) {
					if(qt.getActuel()>=qt.getMini()){							// on vérifi si la quete n'est déja pas termine...
						player.sendMessage(ChatColor.GREEN+"Cette quete est deja termine...");
						inventory.clear(case_inv);								// on vide cet inventaire la pour ne pas le laisser aux autres joueurs...
						return;													// la qete est deja faite, on continu sans rien faire...
					}
					player.getWorld().playEffect(LocQuete, Effect.STEP_SOUND , Material.LAPIS_BLOCK);	// petit effet de lapis qui eclate...
					Nbr = qt.getActuel()+invQete.getAmount();					// pas deja termine, alors on fait le cumul... 
					qt.setActuel(Nbr);
					inventory.clear(case_inv);									// vide la case contenant le PORK...
					if(Nbr>=qt.getMini()){ 										// regarde si la Quete est termine...
						player.sendMessage(ChatColor.BOLD+""+ChatColor.GREEN+qt.getMessage()+" --> REUSSI, félicitation...");
						qt.setTermine(true);									// precise que cette quete est termine
					} else {
						player.sendMessage(ChatColor.GREEN+""+Nbr+" "+qt.name()+" sur "+qt.getMini()+"...");
						qt.setTermine(false);									// ou ne l'est pas...
					}
				}
			}
		}
	}
	
	@EventHandler
	private void onBlockBreakEvent(BlockBreakEvent e){			// Evenement qui empeche de detruire le coffre de Quete...
		
		if(MonPlugin.getInstance().isPartieActive()) {			// si pas en cours de jeux, alors pas possible de cassé le coffre 'Quete'
			if(e.getBlock().getType().equals(Material.CHEST)){
				Chest chest = (Chest) e.getBlock().getState();
				if(chest.getInventory().getName().equalsIgnoreCase("quete")){	// Verifie si c'est le Nom du coffre de Quete
					e.setCancelled(true);							// interdit de casse le coffre 'quete' et uniquement celui la...
				}
			}
		}
		
	}
	
	// -------------------------------------------------------------------------------------------------------------------------------
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {			// PlayerJoinEvent est l'évènement de connexion d'un joueur sur le serveur
	
		Player player = e.getPlayer();						// On récupère le joueur qui vient de se connecter à partir de l'évènement
		player.sendMessage("Bienvenue sur le serveur FK de Clement !");				// On lui envoie un message
		if(!player.hasMetadata("Team")){																	// On vérifie si déja TAG pour  Couleur de Base
			String CouleurTeam = MonPlugin.getInstance().getConfig().getString(player.getName());			// Pas encore de TAG on vas donc chercher si Joueur existe dans fichier de config
			if (CouleurTeam != null){				
				player.sendMessage("votre couleur de Base est le "+CouleurTeam);							// Si c'est le cas, on lui TAG la couleur défini
				player.setMetadata("Team", new FixedMetadataValue(MonPlugin.getInstance(), CouleurTeam));
			} else {																						// Sinon il vas falloir définir une Couleur de Base
				CouleurTeam = MonPlugin.getInstance().getConfig().getString("CouleurDefault");				// Récupère la couleur par defaut
				MonPlugin.getInstance().getConfig().set(player.getName(), CouleurTeam);						// Ajoute le joueur dans la Config
				MonPlugin.getInstance().saveConfig();														// sauvegarde le fichier de config.
				player.sendMessage("Vous n'avez pas de Couleur de Team prédéfini... La nouvelle est le "+CouleurTeam);
				player.setMetadata("Team", new FixedMetadataValue(MonPlugin.getInstance(), CouleurTeam));	// Tage le joueur avec la couleur...
			}
		} else {
			player.sendMessage("DEJA TAG Couleur de Base...");								// Le joueur à déja le TAG, déco/Reco...
		}
	}
	
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){			// PlayerQuitEvent est l'évènement de fin de connexion d'un joueur sur le serveur
		Player player = e.getPlayer();
		player.sendMessage("A Bientôt sur le serveur FK de Clement !");					// On lui envoie un message prive
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
	
	@EventHandler
	public void onChangeGameMode(PlayerGameModeChangeEvent e){				// interdit de passer en gamemode si la partie est active
		e.setCancelled(MonPlugin.getInstance().isPartieActive());
	}
	
}
