package fr.sagitarius.MonPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

public class MonPlugin extends JavaPlugin implements Listener { /* PROCEDURE PRINCIPALE LANCER AU DEPART */

	private boolean PartieActive = false;
	
	public static MonPlugin instance;
	
	public static MonPlugin getInstance() {			// fonction qui retourne l'instance du programme principal, celui-ci...
		return instance;
	}

	@Override
	public void onEnable() { /* à l'activation du plugin */
		super.onEnable();
		
		instance = this;
		
		System.out.println("MonPlugin > active !");
		
		getCommand("start").setExecutor(new Commandes());		// commande 'start'		lance affichage de la direction
		getCommand("end").setExecutor(new Commandes());			// commande 'end'		arrete affichage de la direction
		getCommand("list").setExecutor(new Commandes());		// commande 'list'		liste les positions sauvegarder
		getCommand("remove").setExecutor(new Commandes());		// commande 'remove'	supprime une position sauvegarder
		getCommand("save").setExecutor(new Commandes());		// commande 'save'		sauve une position
		getCommand("tpmort").setExecutor(new Commandes());		// commande 'tpmort'	teleporte au dernier lieu de mort
		getCommand("tpw").setExecutor(new Commandes());			// commande 'tpw'		teleporte a une position sauvegarder
		
		getServer().getPluginManager().registerEvents(new MonPluginListeners(), this);	// déclare mon Listener

		getConfig().options().copyDefaults(true); 			/* met le fichier config par defaut si pas déja creer */
		saveConfig();										/* sauvegarde le fichier de config. */

		setPartieActive(false);								/* par defaut l'affichage de la direction de la base ne s'affiche pas */

	}
	
	@Override
	public void onDisable() { /* à la cloture du plugin */
		super.onDisable();
		System.out.println("MonPlugin > desactive !");
	}

	public boolean isPartieActive() {
		return PartieActive;
	}

	public void setPartieActive(boolean partieActive) {
		PartieActive = partieActive;
	}

	
	// ------------------------------------------------------------------------------------------
	// 
	// TACHE DE FOND ACTIVE TOUTES LES SECONDES QUI AFFICHE LES DONNEES SI ACTIVE...
	// 
	// ------------------------------------------------------------------------------------------
	
	
	
	public int task2;

	public void AfficheDistanceBase(final double BleuX, final double BleuZ, final double RougeX, final double RougeZ, 
									final double NetherX, final double NetherZ, final double TheEndX, final double TheEndZ) { 
									/*
									 * tache lancé toute les secondes (20 ticks)
									 * soit 1 seconde pour afficher la distance et la fleche indiquant notre base...
									 */

		task2 = Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

			private int RegardVers;			/* Fleche de 0 à 7 en fonction des deux angles */
			private String RegardVersTexte;	/* Fleche correspondant en texte */
			private int DistancePtoB;		/* Distence entre Joeur et Base */
			private String CouleurTeam;		/* TAG correspondant à la couleur de la BASE du Joueur dans CONFIG */

			@Override
			public void run() {
				boolean Partie = isPartieActive();

				if (Partie) { /* vrai - Partie en route */
					for (Player OnLinePlayer : Bukkit.getServer().getOnlinePlayers()) { /* si pas de joueur ne fait rien !!! */
						Location LocPlayer = OnLinePlayer.getLocation();
						int OffSetX;	/* Offset sur X entre Joeur et Base */
						int OffSetZ;	/* Offset sur Z entre Joeur et Base */
						int NumARP;		/* Numéro 0 à 7 regard Angle Joueur */
						int NumAngle;	/* Angle entre 0 et 7 entre Joeur et Base */
						CouleurTeam = OnLinePlayer.getMetadata("Team").get(0).asString();
						if (CouleurTeam != null) { /* Normalement tous les joueurs ont un TAG, donc couleurTeam existe */

							boolean MondeNether = OnLinePlayer.getWorld().getName().contains("nether");		/* Pour tester si pas dans NETHER pour changer la loc de la recherche BASE */
							boolean MondeEnd = OnLinePlayer.getWorld().getName().contains("the_end");		/* Pour tester si pas dans l'END pour changer la loc de la recherche BASE */
							if (!MondeNether && !MondeEnd){			/* Si pas le nether ou l'end alors calcul base du monde normal */
								if (CouleurTeam.equals("bleu")) { 						/* calcul pour base bleu... */
									OffSetX = (int) (LocPlayer.getX() - (int) BleuX);
									OffSetZ = (int) (LocPlayer.getZ() - (int) BleuZ);
								} else { 												/* ou pour base rouge... */
									OffSetX = (int) (LocPlayer.getX() - (int) RougeX);
									OffSetZ = (int) (LocPlayer.getZ() - (int) RougeZ);
								}
							} else {
								if (MondeNether){		/* c'est le nether alors prend les coordonnees du nether comme base */
									OffSetX = (int) (LocPlayer.getX() - (int) NetherX);
									OffSetZ = (int) (LocPlayer.getZ() - (int) NetherZ);
								} else {				/* c'est l'end alors prend les coordonnees de l'end comme base */
									OffSetX = (int) (LocPlayer.getX() - (int) TheEndX);
									OffSetZ = (int) (LocPlayer.getZ() - (int) TheEndZ);
								}
								
							}

							DistancePtoB = (int) Math.sqrt(Math.pow(OffSetX, 2) + Math.pow(OffSetZ, 2));
							float AngleRegardP = OnLinePlayer.getEyeLocation().getYaw();

							if (AngleRegardP >= 0) {
								NumARP = (int) AngleRegardP / 45;
							} else {
								NumARP = (int) (360 + AngleRegardP) / 45;
							}
							if (OffSetX >= 0) { /*
												 * joueur partie x de gauche (+)
												 */
								if (OffSetZ >= 0) { /*
													 * joueur partie z du haut
													 * (+)
													 */
									if (Math.abs(OffSetX) > Math.abs(OffSetZ)) {
										NumAngle = 3; /* bas+droite */
									} else {
										NumAngle = 4; /* bas */
									}

								} else { /* joueur partie z du bas (-) */
									if (Math.abs(OffSetX) > Math.abs(OffSetZ)) {
										NumAngle = 2; /* droite */
									} else {
										NumAngle = 1; /* haut+droite */
									}
								}
							} else {
								if (OffSetZ >= 0) { /*
													 * joueur partie z du haut
													 * (+)
													 */
									if (Math.abs(OffSetX) > Math.abs(OffSetZ)) {
										NumAngle = 6; /* bas+droite */
									} else {
										NumAngle = 5; /* bas */
									}
								} else { /* joueur partie z du bas (-) */
									if (Math.abs(OffSetX) > Math.abs(OffSetZ)) {
										NumAngle = 7; /* droite */
									} else {
										NumAngle = 0; /* haut+droite */
									}
								}
							}
							RegardVers = (int) NumAngle - NumARP;
							if (RegardVers < 0) {
								RegardVers = (int) 8 + RegardVers;
							}
							switch (RegardVers) { /* 0 \u2191  1 \u2197  2 \u2192  3 \u2198  4 \u2193  5 \u2199  6 \u2190  7 \u2196 */
							case 1:
								RegardVersTexte = " \u2191";
								break;

							case 2:
								RegardVersTexte = " \u2197";
								break;

							case 3:
								RegardVersTexte = " \u2192";
								break;

							case 4:
								RegardVersTexte = " \u2198";
								break;

							case 5:
								RegardVersTexte = " \u2193";
								break;

							case 6:
								RegardVersTexte = " \u2199";
								break;

							case 7:
								RegardVersTexte = " \u2190";
								break;

							case 0:
								RegardVersTexte = " \u2196";
								break;

							default:
								RegardVersTexte = " o";
								break;
							}
							/*
							 * affiche la bousole dans la barre d'action du
							 * joeur...
							 */
							ActionBarAPI.sendActionBar(OnLinePlayer, "§l§bBASE: §e" + DistancePtoB + RegardVersTexte);
						} else {
							/* 
							 * Normalement jamais, 
							 * mais au cas ou joueur
							 *  sans TAG
							 */
							ActionBarAPI.sendActionBar(OnLinePlayer, "§l§bBASE: §e BUG, Joueur sans TAG !!!");
						}
					}
					AfficheDistanceBase(BleuX, BleuZ, RougeX, RougeZ, NetherX, NetherZ, TheEndX, TheEndZ); /* et relance la tache... */
				} else { /* faux - partie terminer */
					Bukkit.getScheduler().cancelTask(
							task2); /* tue la tache quand partie terminer... */
				}


			}

		}, 10);

	}

}
