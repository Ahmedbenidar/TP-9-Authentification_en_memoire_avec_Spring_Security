package ma.usmba.portail.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccueilController {

	@GetMapping("/")
	public String accueil() {
		return "index";
	}

	@GetMapping("/connexion")
	public String connexion() {
		return "connexion";
	}

	@GetMapping("/acces-refuse")
	public String accesRefuse() {
		return "acces_refuse";
	}
}
