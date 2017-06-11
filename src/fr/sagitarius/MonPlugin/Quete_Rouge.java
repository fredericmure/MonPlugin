package fr.sagitarius.MonPlugin;

import org.bukkit.Material;

public enum Quete_Rouge {
	APPLE(Material.APPLE, "Obtenir au moins 10 Pommes",10,0,false),
	DIAMOND_HELMET(Material.DIAMOND_HELMET, "Obtenir un casque en diamants",1,0,false),
	ENCHANTMENT_TABLE(Material.ENCHANTMENT_TABLE, "Obtenir une Table d'enchant",1,0,false),
	LEATHER_LEGGINGS(Material.LEATHER_LEGGINGS, "Obtenir un Pantalon en cuir",1,0,false),
	LEATHER_CHESTPLATE(Material.LEATHER_CHESTPLATE, "Obtenir une Veste en cuir",1,0,false),
	RAW_FISH(Material.RAW_FISH, "Obtenir 5 poissons cru",5,0,false),
	SOUL_SAND(Material.SOUL_SAND, "Obtenir 64 Soul Sand",64,0,false),
	EMERALD(Material.EMERALD, "Obtenir 3 Emeraudes",3,0,false);
	
	private Material material;
	private String message="";
	private int mini=0;
	private int actuel=0;
	private boolean termine;
	
	private Quete_Rouge(Material material, String message, int mini, int actuel, boolean termine) {
		this.setMaterial(material);
		this.setMessage(message);
		this.setMini(mini);
		this.setActuel(actuel);
		this.setTermine(termine);
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getMini() {
		return mini;
	}

	public void setMini(int mini) {
		this.mini = mini;
	}

	public int getActuel() {
		return actuel;
	}

	public void setActuel(int actuel) {
		this.actuel = actuel;
	}

	public boolean isTermine() {
		return termine;
	}

	public void setTermine(boolean termine) {
		this.termine = termine;
	}
}
