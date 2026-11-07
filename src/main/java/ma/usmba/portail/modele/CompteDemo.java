package ma.usmba.portail.modele;

/**
 * Compte de demonstration expose sur la page de connexion.
 * Aucun mot de passe n'est stocke en clair dans la base : ce record ne sert
 * qu'a l'affichage pedagogique de l'annuaire de test.
 */
public record CompteDemo(
		String identifiant,
		String secretClair,
		String nomComplet,
		String fonction,
		String rolePrincipal) {
}
