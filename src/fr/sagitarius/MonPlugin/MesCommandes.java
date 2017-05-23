package fr.sagitarius.MonPlugin;


import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;


public class MesCommandes implements Listener {

	public MonPlugin pl;

	public MesCommandes(MonPlugin pl) {
		this.pl = pl;
	}

	@EventHandler
	public void onCommandes(PlayerCommandPreprocessEvent e){		// Evenement qui gere les commandes du type "/xxxxx zzzz yyy"
		
		// variable diverses
		String msg = e.getMessage();
		String[] args = msg.split(" ");								// dans args[0]=Nom commande, puis args[x]= les parametres...

		// commande /START
		// qui demare l'affichage de la base
		if(args[0].equalsIgnoreCase("/start")){							// Démarrage pour affichage BASE
			double BleuX = this.pl.getConfig().getDouble("BleuX");		// Récupère les coordonnées de la base bleu
			double BleuZ = this.pl.getConfig().getDouble("BleuZ");
			double RougeX = this.pl.getConfig().getDouble("RougeX");	// Récupère les coordonnées de la base rouge
			double RougeZ = this.pl.getConfig().getDouble("RougeZ");
			double NetherX = this.pl.getConfig().getDouble("NetherX");	// Récupère les coordonnées du Nether
			double NetherZ = this.pl.getConfig().getDouble("NetherZ");
			double TheEndX = this.pl.getConfig().getDouble("TheEndX");	// Récupère les coordonnées de l'End
			double TheEndZ = this.pl.getConfig().getDouble("TheEndZ");
			this.pl.setPartieActive(true);								// commence affichage de la BASE
			this.pl.AfficheDistanceBase(BleuX, BleuZ,RougeX, RougeZ, NetherX, NetherZ, TheEndX, TheEndZ);	// Affichage bousole base qui se terminera quand setPartieActive=false
			e.setCancelled(true);
		}

		// commande /END
		// qui arrete l'affichage de la base
		if(args[0].equalsIgnoreCase("/end")){
			this.pl.setPartieActive(false);								// termine affichage de la fleche BASE
			// ---------------------------------------------------------------------------------------
			//
			// ici boucle pour supprimer tous les TAG des joueurs En Ligne ou Hors Ligne.... si besoins....
			//
			// ---------------------------------------------------------------------------------------
			e.setCancelled(true);			
		}
		
		
		// commande /SAVE
		// qui sauvegarde une coordonnee dans le fichier de config
		if(args[0].equalsIgnoreCase("/save")){
			if(args.length==5) {								// /save MonNom LocX LocY LocZ   de args[0] a args[4]
				e.getPlayer().sendMessage("§bSauvegarde de "+args[1]+" ...");
				this.pl.getConfig().set("Sauvegarde."+args[1]+".Monde", e.getPlayer().getWorld().getName());	// Ajoute le MonNom et Monde dans le fichier de config
				this.pl.getConfig().set("Sauvegarde."+args[1]+".LocX", Integer.parseInt(args[2]));		// Ajoute le MonNom et LocX dans le fichier de config
				this.pl.getConfig().set("Sauvegarde."+args[1]+".LocY", Integer.parseInt(args[3]));		// Ajoute le MonNom et LocY dans le fichier de config
				this.pl.getConfig().set("Sauvegarde."+args[1]+".LocZ", Integer.parseInt(args[4]));		// Ajoute le MonNom et LocZ dans le fichier de config
				this.pl.saveConfig();
			} else {
				if(args.length==2){								// /save MonNom     de args[0] a args[1]
					Player Player = e.getPlayer();
					Player.sendMessage("§bSauvegarde de "+args[1]+" en loc automatique ...");
					this.pl.getConfig().set("Sauvegarde."+args[1]+".Monde", Player.getWorld().getName());		// Ajoute le MonNom et Monde dans le fichier de config
					this.pl.getConfig().set("Sauvegarde."+args[1]+".LocX", Player.getLocation().getBlockX());	// Ajoute le MonNom et LocX dans le fichier de config
					this.pl.getConfig().set("Sauvegarde."+args[1]+".LocY", Player.getLocation().getBlockY());	// Ajoute le MonNom et LocY dans le fichier de config
					this.pl.getConfig().set("Sauvegarde."+args[1]+".LocZ", Player.getLocation().getBlockZ());	// Ajoute le MonNom et LocZ dans le fichier de config
					this.pl.saveConfig();
				} else {
					e.getPlayer().sendMessage("§cUsage: /save <MonNom> <ValeurX> <ValeurY> <ValeurZ>");
					e.getPlayer().sendMessage("§cUsage: /save <MonNom> (sans loc = la loc actuelle du joueur automatiquement)");
				}
			}
			e.setCancelled(true);				// termine la commande en indiquent que nous l'avons traiter...
		}
		
		
		// commande /REMOVE
		// qui supprime une coordonnee dans le fichier de config
		if(args[0].equalsIgnoreCase("/remove")){			// /remove MonNom    de args[0] a args[1]
			if(args.length==2) {
				e.getPlayer().sendMessage("§bSupression de la sauvegarde "+args[1]+" ...");
				this.pl.getConfig().set("Sauvegarde."+args[1], null);		// Suprime le MonNom dans le fichier de config
				this.pl.saveConfig();	
			} else {
				e.getPlayer().sendMessage("§cUsage: /remove MonNom");
			}
			e.setCancelled(true);				// termine la commande en indiquant que nous l'avons traite...
		}
		
		
		// commande /LIST
		// qui affiche les coordonnee du fichier de config
		if(args[0].equalsIgnoreCase("/list")){
			e.getPlayer().sendMessage("§bListe Sauvegarde(s) :");
			Set<String> ListeSauvegarde = this.pl.getConfig().getConfigurationSection("Sauvegarde").getKeys(false);	// pointe sur la structure "sauvegarde"
			for(String NomSauvegarde : ListeSauvegarde) {															// boucle qui recupere chaque "sauvegarde"
				e.getPlayer().sendMessage("§3   - "+NomSauvegarde+													// affiche les divers element si il y en a...
						" "+this.pl.getConfig().getInt("Sauvegarde."+NomSauvegarde.toString()+".LocX")+
						" "+this.pl.getConfig().getInt("Sauvegarde."+NomSauvegarde.toString()+".LocY")+
						" "+this.pl.getConfig().getInt("Sauvegarde."+NomSauvegarde.toString()+".LocZ"));
			}
			e.setCancelled(true);				// termine la commande en indiquent que nous l'avons traiter...
		}
		
		// commande /TPMORT
		// qui TP le joueur aux coordonnees de sa mort qui est sauvegarder dans le fichier de config
		if(args[0].equalsIgnoreCase("/tpmort")){
			Player Player = e.getPlayer();
			if (this.pl.getConfig().isSet("Sauvegarde."+Player.getName()+"_Mort")){	// regarde si deja une mort d'enregistree...
				Location LocMortPlayer = new Location(												// si oui alors on creer la coordonnee de tp 
					Bukkit.getWorld(this.pl.getConfig().getString("Sauvegarde."+Player.getName()+"_Mort.Monde")),	// recupere le monde de notre mort et le transforme en type WORLD
					this.pl.getConfig().getDouble("Sauvegarde."+Player.getName()+"_Mort.LocX"),		// Recupere le X
					this.pl.getConfig().getDouble("Sauvegarde."+Player.getName()+"_Mort.LocY"),		// puis le Y
					this.pl.getConfig().getDouble("Sauvegarde."+Player.getName()+"_Mort.LocZ"));	// puis le Z
				Player.teleport(LocMortPlayer);														// et enfin teleporte le joueur à la coordonnee
				Player.sendMessage("§bTP au lieu de votre dernière mort...");						// affiche un message dans le chat du joeur
				System.out.println("Le joueur "+Player.getDisplayName()+" retourne sur le lieu de sa mort...");	// affiche un message sur la console...
			} else {
				Player.sendMessage("§bPas de mort enregistree dernierement...");	// si non alors on ne fait rien, previens le joueur dans son chat...
			}
			e.setCancelled(true);				// termine la commande en indiquent que nous l'avons traiter...
		}
		
		
		// commande /TPW
		// qui TP le joueur aux coordonnees du <NomSauvegarde> qui est sauvegarder dans le fichier de config
		if(args[0].equalsIgnoreCase("/tpw")){														// TPW <NomSauvegarde>
			if(args.length==2) {																	// regarde si bien tpw + nom
				Player Player = e.getPlayer();
				if (this.pl.getConfig().isSet("Sauvegarde."+args[1])){								// regarde si la sauvegarde existe 
					Location LocSavePlayer = new Location(											// si oui alors on creer la coordonnee de tp 
						Bukkit.getWorld(this.pl.getConfig().getString("Sauvegarde."+args[1]+".Monde")),	// recupere le monde de la sauvegarde et le transforme en type WORLD
						this.pl.getConfig().getDouble("Sauvegarde."+args[1]+".LocX"),				// Recupere le X
						this.pl.getConfig().getDouble("Sauvegarde."+args[1]+".LocY"),				// puis le Y
						this.pl.getConfig().getDouble("Sauvegarde."+args[1]+".LocZ"));				// puis le Z
					Player.teleport(LocSavePlayer);													// et enfin teleporte le joueur à la coordonnee
					Player.sendMessage("§bTP au lieu de votre sauvegarde...");						// affiche un message dans le chat du joeur
					System.out.println("Le joueur "+Player.getDisplayName()+" retourne sur le lieu de la sauvegarde...");	// affiche un message sur la console...
				} else {
					Player.sendMessage("§bPas de sauvegarde à ce nom enregistree dernierement...");	// si non alors on ne fait rien, previens le joueur dans son chat...
				}
			}
			e.setCancelled(true);				// termine la commande en indiquent que nous l'avons traiter...
		}
		
	}
	
	
	
	@EventHandler										// Affiche les coordonnees du joueur qui viens de mourir, et uniquement a lui...
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player Player = e.getEntity().getPlayer();		// Recupere le joueur
        System.out.println("Le joueur "+Player.getDisplayName()+" est mort...");	// est affiche sa mort sur la console...
        Location LocPlayer = Player.getLocation();		// coordonnee du joueur mort, et ensuite les affiche sur chat du joueur et les sauvegarder...
        this.pl.getConfig().set("Sauvegarde."+Player.getName()+"_Mort.Monde", LocPlayer.getWorld().getName());		// Ajoute le Monde dans le fichier de config du joueur
        this.pl.getConfig().set("Sauvegarde."+Player.getName()+"_Mort.LocX", LocPlayer.getBlockX());		// Ajoute LocX dans le fichier de config du joueur mort
        this.pl.getConfig().set("Sauvegarde."+Player.getName()+"_Mort.LocY", LocPlayer.getBlockY());		// Ajoute LocY dans le fichier de config du joueur mort
        this.pl.getConfig().set("Sauvegarde."+Player.getName()+"_Mort.LocZ", LocPlayer.getBlockZ());		// Ajoute LocZ dans le fichier de config du joueur mort
		this.pl.saveConfig();
        Player.sendMessage("§b["+Player.getDisplayName()+"] §2Mort en "+LocPlayer.getBlockX()+" , "+LocPlayer.getBlockY()+" , "+LocPlayer.getBlockZ());
    }
	
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {			// PlayerJoinEvent est l'évènement de connexion d'un joueur sur le serveur
	
		Player p = e.getPlayer();							// On récupère le joueur qui vient de se connecter à partir de l'évènement
		p.sendMessage("Bienvenue sur le serveur FK de Clement !");		// On lui envoie un message
		if(!p.hasMetadata("Team")){														// On vérifie si déja TAG pour  Couleur de Base
			String CouleurTeam = this.pl.getConfig().getString(p.getName());			// Pas encore de TAG on vas donc chercher si Joueur existe dans fichier de config
			if (CouleurTeam != null){				
				p.sendMessage("votre couleur de Base est le "+CouleurTeam);				// Si c'est le cas, on lui TAG la couleur défini
				p.setMetadata("Team", new FixedMetadataValue(this.pl, CouleurTeam));
			} else {																	// Sinon il vas falloir définir une Couleur de Base
				CouleurTeam = this.pl.getConfig().getString("CouleurDefault");		// Récupère la couleur par defaut
				this.pl.getConfig().set(p.getName(), CouleurTeam);						// Ajoute le joueur dans la Config
				this.pl.saveConfig();													// sauvegarde le fichier de config.
				p.sendMessage("Vous n'avez pas de Couleur de Team prédéfini... La nouvelle est le "+CouleurTeam);
				p.setMetadata("Team", new FixedMetadataValue(this.pl, CouleurTeam));	// Tage le joueur avec la couleur...
			}
		} else {
			p.sendMessage("DEJA TAG Couleur de Base...");								// Le joueur à déja le TAG, déco/Reco...
		}
	}
	
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){			// PlayerQuitEvent est l'évènement de fin de connexion d'un joueur sur le serveur
		Player p = e.getPlayer();
		p.sendMessage("A Bientôt sur le serveur FK de Clement !");					// On lui envoie un message prive
		System.out.println("Le joueur "+p.getDisplayName()+" a quite le jeux !");	// est aussi sur la console...
	}
	
	
	
}
