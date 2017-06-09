package fr.sagitarius.MonPlugin;

import org.bukkit.Material;

public enum Quete_Bleu {
	PORK(Material.PORK, "Obtenir au moins 10 cotes de porc cru",10,0,false),
	DIAMOND_HELMET(Material.DIAMOND_HELMET, "Obtenir au moins un casque en diamants",1,0,false),
	SUGAR_CANE(Material.SUGAR_CANE, "Obtenir au moins 5 cannes a sucre",5,0,false);
	
	private Material material;
	private String message="";
	private int mini=0;
	private int actuel=0;
	private boolean termine;
	
	private Quete_Bleu(Material material, String message, int mini, int actuel, boolean termine) {
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
