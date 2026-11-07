package ma.usmba.portail.modele;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record EntreeJournal(String identifiant, LocalDateTime instant, TypeEvenement type) {

	public enum TypeEvenement {
		CONNEXION("Connexion acceptée", "ok"),
		ECHEC("Identifiants refusés", "echec"),
		DECONNEXION("Déconnexion", "neutre");

		private final String libelle;
		private final String style;

		TypeEvenement(String libelle, String style) {
			this.libelle = libelle;
			this.style = style;
		}

		public String getLibelle() {
			return libelle;
		}

		public String getStyle() {
			return style;
		}
	}

	public String getHeure() {
		return instant.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
	}
}
