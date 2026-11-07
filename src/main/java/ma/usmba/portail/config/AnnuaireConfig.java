package ma.usmba.portail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import ma.usmba.portail.modele.AnnuaireDemo;
import ma.usmba.portail.modele.CompteDemo;

@Configuration
public class AnnuaireConfig {

	@Bean
	public UserDetailsService userDetailsService(AnnuaireDemo annuaire, PasswordEncoder encodeur) {
		UserDetails[] comptes = annuaire.getComptes().stream()
				.map(compte -> versUserDetails(compte, encodeur))
				.toArray(UserDetails[]::new);
		return new InMemoryUserDetailsManager(comptes);
	}

	private UserDetails versUserDetails(CompteDemo compte, PasswordEncoder encodeur) {
		User.UserBuilder builder = User.withUsername(compte.identifiant())
				.password(encodeur.encode(compte.secretClair()));

		return switch (compte.rolePrincipal()) {
			case "ADMIN" -> builder.roles("ADMIN", "RH", "MANAGER", "EMPLOYE").build();
			case "RH" -> builder.roles("RH", "MANAGER", "EMPLOYE").build();
			case "MANAGER" -> builder.roles("MANAGER", "EMPLOYE").build();
			default -> builder.roles("EMPLOYE").build();
		};
	}
}
