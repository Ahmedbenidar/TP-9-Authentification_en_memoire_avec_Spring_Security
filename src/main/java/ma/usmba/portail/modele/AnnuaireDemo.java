package ma.usmba.portail.modele;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AnnuaireDemo {

	private final List<CompteDemo> comptes = List.of(
			new CompteDemo("user", "1111", "Sara Benali", "Chargée de dossier - Service Informatique", "EMPLOYE"),
			new CompteDemo("manager", "2222", "Karim El Idrissi", "Chef d'équipe - Pôle Développement", "MANAGER"),
			new CompteDemo("rh", "3333", "Hind Ouazzani", "Gestionnaire - Direction des Ressources Humaines", "RH"),
			new CompteDemo("admin", "1234", "Nadia Cherkaoui", "Directrice RH - Super administratrice", "ADMIN"));

	public List<CompteDemo> getComptes() {
		return comptes;
	}
}
