package ma.usmba.portail.modele;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DonneesPortail {

	public record DemandeConge(String reference, String collaborateur, String type, LocalDate debut, LocalDate fin,
			String etat) {
	}

	public record DossierPaie(String matricule, String collaborateur, String mois, String statut) {
	}

	public List<DemandeConge> getDemandes() {
		return List.of(
				new DemandeConge("CG-2024-104", "Sara Benali", "Annuel", LocalDate.of(2024, 11, 4),
						LocalDate.of(2024, 11, 8), "Validée"),
				new DemandeConge("CG-2024-112", "Sara Benali", "Récupération", LocalDate.of(2024, 12, 2),
						LocalDate.of(2024, 12, 3), "Validée"),
				new DemandeConge("CG-2024-118", "Yassine Amrani", "Annuel", LocalDate.of(2024, 12, 23),
						LocalDate.of(2024, 12, 31), "En attente"),
				new DemandeConge("CG-2024-121", "Salma Bennis", "Exceptionnel", LocalDate.of(2024, 12, 26),
						LocalDate.of(2024, 12, 27), "Validée"),
				new DemandeConge("CG-2024-124", "Omar Tazi", "Maladie", LocalDate.of(2024, 12, 30),
						LocalDate.of(2025, 1, 3), "En attente"),
				new DemandeConge("CG-2024-127", "Sara Benali", "Annuel", LocalDate.of(2025, 1, 6),
						LocalDate.of(2025, 1, 10), "En attente"));
	}

	public List<DossierPaie> getDossiersPaie() {
		return List.of(
				new DossierPaie("MAT-0142", "Yassine Amrani", "Novembre 2024", "Clôturé"),
				new DossierPaie("MAT-0187", "Salma Bennis", "Novembre 2024", "À valider"),
				new DossierPaie("MAT-0203", "Omar Tazi", "Décembre 2024", "En préparation"));
	}

	public long getEffectifTotal() {
		return 128;
	}

	public long getDemandesEnAttente() {
		return getDemandes().stream().filter(d -> d.etat().equals("En attente")).count();
	}
}
