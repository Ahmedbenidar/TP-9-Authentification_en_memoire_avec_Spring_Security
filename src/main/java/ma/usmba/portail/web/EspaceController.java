package ma.usmba.portail.web;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ma.usmba.portail.modele.AnnuaireDemo;
import ma.usmba.portail.modele.CompteDemo;
import ma.usmba.portail.modele.DonneesPortail;
import ma.usmba.portail.modele.JournalConnexions;

@Controller
public class EspaceController {

	private final DonneesPortail donnees;
	private final JournalConnexions journal;
	private final AnnuaireDemo annuaire;

	public EspaceController(DonneesPortail donnees, JournalConnexions journal, AnnuaireDemo annuaire) {
		this.donnees = donnees;
		this.journal = journal;
		this.annuaire = annuaire;
	}

	@GetMapping("/mon-espace")
	public String aiguillage(Principal principal) {
		String identifiant = principal.getName();
		CompteDemo compte = annuaire.getComptes().stream()
				.filter(c -> c.identifiant().equals(identifiant))
				.findFirst()
				.orElse(null);

		String role = compte == null ? "EMPLOYE" : compte.rolePrincipal();
		return switch (role) {
			case "ADMIN" -> "redirect:/espace/administration/tableau-de-bord";
			case "RH" -> "redirect:/espace/rh/tableau-de-bord";
			case "MANAGER" -> "redirect:/espace/manager/tableau-de-bord";
			default -> "redirect:/espace/employe/tableau-de-bord";
		};
	}

	@GetMapping("/espace/employe/tableau-de-bord")
	public String espaceEmploye(Model model, Principal principal) {
		String identifiant = principal.getName();
		String nom = annuaire.getComptes().stream()
				.filter(c -> c.identifiant().equals(identifiant))
				.map(CompteDemo::nomComplet)
				.findFirst()
				.orElse(identifiant);

		model.addAttribute("identifiant", identifiant);
		model.addAttribute("nomComplet", nom);
		model.addAttribute("demandes", donnees.getDemandes().stream()
				.filter(d -> d.collaborateur().equals(nom))
				.toList());
		return "espace_employe";
	}

	@GetMapping("/espace/manager/tableau-de-bord")
	public String espaceManager(Model model, Principal principal) {
		model.addAttribute("identifiant", principal.getName());
		model.addAttribute("demandes", donnees.getDemandes());
		model.addAttribute("enAttente", donnees.getDemandesEnAttente());
		return "espace_manager";
	}

	@GetMapping("/espace/rh/tableau-de-bord")
	public String espaceRh(Model model, Principal principal) {
		model.addAttribute("identifiant", principal.getName());
		model.addAttribute("effectif", donnees.getEffectifTotal());
		model.addAttribute("dossiers", donnees.getDossiersPaie());
		return "espace_rh";
	}

	@GetMapping("/espace/administration/tableau-de-bord")
	public String espaceAdministration(Model model, Principal principal) {
		model.addAttribute("identifiant", principal.getName());
		model.addAttribute("comptes", annuaire.getComptes());
		model.addAttribute("effectif", donnees.getEffectifTotal());
		return "espace_administration";
	}

	@GetMapping("/journal/connexions")
	public String journalConnexions(Model model) {
		List<ma.usmba.portail.modele.EntreeJournal> entrees = journal.getEntrees();
		model.addAttribute("entrees", entrees);
		return "journal";
	}
}
