package fr.sagitarius.MonPlugin;

import java.util.Set;

import javax.swing.JFrame;

// import javax.swing.JFrame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commandes implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(sender instanceof Player){	// ne gère que les commandes faites par des joueurs et non par la console...
			Player player = (Player)sender;
			
			// commande /START
			// qui demare l'affichage de la base
			if(cmd.getName().equalsIgnoreCase("start")){					// Démarrage pour affichage BASE
				player.sendMessage("§bCde START");
				double BleuX = MonPlugin.getInstance().getConfig().getDouble("BleuX");		// Récupère les coordonnées de la base bleu
				double BleuZ = MonPlugin.getInstance().getConfig().getDouble("BleuZ");
				double RougeX = MonPlugin.getInstance().getConfig().getDouble("RougeX");	// Récupère les coordonnées de la base rouge
				double RougeZ = MonPlugin.getInstance().getConfig().getDouble("RougeZ");
				double NetherX = MonPlugin.getInstance().getConfig().getDouble("NetherX");	// Récupère les coordonnées du Nether
				double NetherZ = MonPlugin.getInstance().getConfig().getDouble("NetherZ");
				double TheEndX = MonPlugin.getInstance().getConfig().getDouble("TheEndX");	// Récupère les coordonnées de l'End
				double TheEndZ = MonPlugin.getInstance().getConfig().getDouble("TheEndZ");
				MonPlugin.getInstance().setPartieActive(true);								// commence affichage de la BASE
				MonPlugin.getInstance().AfficheDistanceBase(BleuX, BleuZ,RougeX, RougeZ, NetherX, NetherZ, TheEndX, TheEndZ);	// Affichage bousole base qui se terminera quand setPartieActive=false
				
				return true;
			}

			// commande /END
			// qui arrete l'affichage de la base
			if(cmd.getName().equalsIgnoreCase("end")){
				player.sendMessage("§bAffichage de la position de la base desactive...");
				player.sendMessage("§bet remise à zéro des Quetes...");
				MonPlugin.getInstance().setPartieActive(false);				// termine affichage de la fleche BASE
				// ---------------------------------------------------------------------------------------
				// ici boucle pour supprimer tous les TAG des joueurs En Ligne ou Hors Ligne.... si besoins....
				// ---------------------------------------------------------------------------------------
				for(Quete_Bleu qt : Quete_Bleu.values()){	// **********************************************************
					qt.setTermine(false);					// **** remet a zero les quetes joueurs de la Team BLEU  ****
					qt.setActuel(0);						// **********************************************************
				}
				for(Quete_Rouge qt : Quete_Rouge.values()){	// **********************************************************
					qt.setTermine(false);					// **** remet a zero les quetes joueurs de la Team ROUGE ****
					qt.setActuel(0);						// **********************************************************
				}
				return true;					
			}
			
			// commande /gui
			// qui affiche une interface graphique, en cours de TEST
			if(cmd.getName().equalsIgnoreCase("gui")){
				player.sendMessage("§bAffichage l'interface graphique... (en TEST)");
				
				@SuppressWarnings("unused")
				JFrame gui = new GUI(player);				// Test d'une fenetre windows classique...

				return true;			
			}
			
			// commande /quete
			// qui affiche une Liste des Quetes, en rouge (pas fait) et vert (Fait)
			if(cmd.getName().equalsIgnoreCase("quete")){
				String playercouleur = player.getMetadata("Team").get(0).asString();			// récupère couleur du joueur
				ChatColor codecouleur = ChatColor.RED;											// par defaut c'est rouge
				if(playercouleur.equalsIgnoreCase("bleu")) codecouleur = ChatColor.BLUE;		// mais vérifie si pas bleu
				player.sendMessage("§bListe des Quetes de la Team de couleur "+codecouleur+playercouleur);
				player.sendMessage("§cRouge§b = à faire   et  §aVert§b = déja fait");
				//   TRAITEMENT et Affichage liste de la quetes et mise en couleurs
				
				if(playercouleur.equalsIgnoreCase("bleu")) {									// traitement si team BLEU
					for(Quete_Bleu qt : Quete_Bleu.values()){			// on prend tous les éléments de la Quete...
						if(qt.isTermine()){					// on regarde si l'élément est terminer...
							player.sendMessage(ChatColor.GREEN+qt.getMessage());
						} else {							// ou si il est pas terminer
							player.sendMessage(ChatColor.RED+qt.getMessage()+ChatColor.BLUE+" ("+qt.getActuel()+"/"+qt.getMini()+")");
						}
					}
				} else {																		// traitement si team ROUGE
					for(Quete_Rouge qt : Quete_Rouge.values()){			// on prend tous les éléments de la Quete...
						if(qt.isTermine()){					// on regarde si l'élément est terminer...
							player.sendMessage(ChatColor.GREEN+qt.getMessage());
						} else {							// ou si il est pas terminer
							player.sendMessage(ChatColor.RED+qt.getMessage()+ChatColor.BLUE+" ("+qt.getActuel()+"/"+qt.getMini()+")");
						}
					}
				}
				
				return true;			
			}
			
			// commande /SAVE
			// qui sauvegarde une coordonnee dans le fichier de config en loc manuelle ou automatique
			if(cmd.getName().equalsIgnoreCase("save")){
				if(args.length==4) {								// /save MonNom LocX LocY LocZ   de args[0] a args[3]
					player.sendMessage("§bSauvegarde de "+args[0]+" ...");
					MonPlugin.getInstance().getConfig().set("Sauvegarde."+args[0]+".Monde", player.getWorld().getName());	// Ajoute le MonNom et Monde dans le fichier de config
					MonPlugin.getInstance().getConfig().set("Sauvegarde."+args[0]+".LocX", Integer.parseInt(args[1]));		// Ajoute le MonNom et LocX dans le fichier de config
					MonPlugin.getInstance().getConfig().set("Sauvegarde."+args[0]+".LocY", Integer.parseInt(args[2]));		// Ajoute le MonNom et LocY dans le fichier de config
					MonPlugin.getInstance().getConfig().set("Sauvegarde."+args[0]+".LocZ", Integer.parseInt(args[3]));		// Ajoute le MonNom et LocZ dans le fichier de config
					MonPlugin.getInstance().saveConfig();
				} else {
					if(args.length==1){								// /save MonNom     de args[0]
						player.sendMessage("§bSauvegarde de "+args[0]+" en loc automatique ...");
						MonPlugin.getInstance().getConfig().set("Sauvegarde."+args[0]+".Monde", player.getWorld().getName());		// Ajoute le MonNom et Monde dans le fichier de config
						MonPlugin.getInstance().getConfig().set("Sauvegarde."+args[0]+".LocX", player.getLocation().getBlockX());	// Ajoute le MonNom et LocX dans le fichier de config
						MonPlugin.getInstance().getConfig().set("Sauvegarde."+args[0]+".LocY", player.getLocation().getBlockY());	// Ajoute le MonNom et LocY dans le fichier de config
						MonPlugin.getInstance().getConfig().set("Sauvegarde."+args[0]+".LocZ", player.getLocation().getBlockZ());	// Ajoute le MonNom et LocZ dans le fichier de config
						MonPlugin.getInstance().saveConfig();
					} else {
						player.sendMessage("§cUsage: /save <NomSauve> <ValeurX> <ValeurY> <ValeurZ>");
						player.sendMessage("§cUsage: /save <NomSauve> (sans loc = la loc actuelle du joueur automatiquement)");
					}
				}
				return true;				// termine la commande en indiquent que nous l'avons traiter...
			}
			
			
			// commande /REMOVE
			// qui supprime une coordonnee dans le fichier de config
			if(cmd.getName().equalsIgnoreCase("remove")){			// /remove MonNom    de args[0]
				if(args.length==1) {
					player.sendMessage("§bSupression de la sauvegarde "+args[0]+" ...");
					MonPlugin.getInstance().getConfig().set("Sauvegarde."+args[0], null);		// Suprime le MonNom dans le fichier de config
					MonPlugin.getInstance().saveConfig();	
				} else {
					player.sendMessage("§cUsage: /remove <NomSauve>");
				}
				return true;				// termine la commande en indiquant que nous l'avons traite...
			}
			
			
			// commande /LIST
			// qui affiche les coordonnee du fichier de config
			if(cmd.getName().equalsIgnoreCase("list")){
				player.sendMessage("§bListe Sauvegarde(s) :");
				Set<String> ListeSauvegarde = MonPlugin.getInstance().getConfig().getConfigurationSection("Sauvegarde").getKeys(false);	// pointe sur la structure "sauvegarde"
				for(String NomSauvegarde : ListeSauvegarde) {											// boucle qui recupere chaque "sauvegarde"
					player.sendMessage("§3   - "+NomSauvegarde+									// affiche les divers element si il y en a...
							" "+MonPlugin.getInstance().getConfig().getInt("Sauvegarde."+NomSauvegarde.toString()+".LocX")+
							" "+MonPlugin.getInstance().getConfig().getInt("Sauvegarde."+NomSauvegarde.toString()+".LocY")+
							" "+MonPlugin.getInstance().getConfig().getInt("Sauvegarde."+NomSauvegarde.toString()+".LocZ"));
				}
				return true;				// termine la commande en indiquent que nous l'avons traiter...
			}
			
			// commande /TPMORT
			// qui TP le joueur aux coordonnees de sa mort qui est sauvegarder dans le fichier de config
			if(cmd.getName().equalsIgnoreCase("tpmort")){
				if (MonPlugin.getInstance().getConfig().isSet("Sauvegarde."+player.getName()+"_Mort")){	// regarde si deja une mort d'enregistree...
					Location LocMortPlayer = new Location(												// si oui alors on creer la coordonnee de tp 
						Bukkit.getWorld(MonPlugin.getInstance().getConfig().getString("Sauvegarde."+player.getName()+"_Mort.Monde")),	// recupere le monde de notre mort et le transforme en type WORLD
						MonPlugin.getInstance().getConfig().getDouble("Sauvegarde."+player.getName()+"_Mort.LocX"),		// Recupere le X
						MonPlugin.getInstance().getConfig().getDouble("Sauvegarde."+player.getName()+"_Mort.LocY"),		// puis le Y
						MonPlugin.getInstance().getConfig().getDouble("Sauvegarde."+player.getName()+"_Mort.LocZ"));	// puis le Z
					player.teleport(LocMortPlayer);														// et enfin teleporte le joueur à la coordonnee
					player.sendMessage("§bTP au lieu de votre dernière mort...");						// affiche un message dans le chat du joeur
					System.out.println("Le joueur "+player.getDisplayName()+" retourne sur le lieu de sa mort...");	// affiche un message sur la console...
				} else {
					player.sendMessage("§bPas de mort enregistree dernierement...");	// si non alors on ne fait rien, previens le joueur dans son chat...
				}
				return true;				// termine la commande en indiquent que nous l'avons traiter...
			}
			
			
			// commande /TPW
			// qui TP le joueur aux coordonnees du <NomSauvegarde> qui est sauvegarder dans le fichier de config
			if(cmd.getName().equalsIgnoreCase("/tpw")){												// TPW <NomSauvegarde>
				if(args.length==1) {																// regarde si bien tpw + nom
					if (MonPlugin.getInstance().getConfig().isSet("Sauvegarde."+args[0])){								// regarde si la sauvegarde existe 
						Location LocSavePlayer = new Location(											// si oui alors on creer la coordonnee de tp 
							Bukkit.getWorld(MonPlugin.getInstance().getConfig().getString("Sauvegarde."+args[0]+".Monde")),	// recupere le monde de la sauvegarde et le transforme en type WORLD
							MonPlugin.getInstance().getConfig().getDouble("Sauvegarde."+args[0]+".LocX"),				// Recupere le X
							MonPlugin.getInstance().getConfig().getDouble("Sauvegarde."+args[0]+".LocY"),				// puis le Y
							MonPlugin.getInstance().getConfig().getDouble("Sauvegarde."+args[0]+".LocZ"));				// puis le Z
						player.teleport(LocSavePlayer);													// et enfin teleporte le joueur à la coordonnee
						player.sendMessage("§bTP au lieu de votre sauvegarde...");						// affiche un message dans le chat du joeur
						System.out.println("Le joueur "+player.getDisplayName()+" retourne sur le lieu de la sauvegarde...");	// affiche un message sur la console...
					} else {
						player.sendMessage("§bPas de sauvegarde à ce nom enregistree dernierement...");	// si non alors on ne fait rien, previens le joueur dans son chat...
					}
				} else {
					player.sendMessage("§cUsage: /tpw <NomSauve>");
				}
				return true;				// termine la commande en indiquent que nous l'avons traiter...
			}
			
			
		} else {		// c'est la console, donc on ne fait rien	\033[31m ROUGE	\033[32m VERT	\033[33m JAUNE	\033[34m BLEU	\033[35m VIOLET	\033[36m TURQUOISE 
			System.out.println("\033[31m"+"Cette commande n'est pas utilisable depuis la console..."+"\033[0m");	// /033[0m remet tout a zero
		}
		return true;		// retourne quand même vrai car c'est une de mes commandes mais executé par la console !!! et je ne veux pas qu'elle soit traité...
	}

}