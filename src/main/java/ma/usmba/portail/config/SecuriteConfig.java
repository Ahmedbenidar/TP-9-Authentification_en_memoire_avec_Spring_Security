package ma.usmba.portail.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import ma.usmba.portail.modele.EntreeJournal.TypeEvenement;
import ma.usmba.portail.modele.JournalConnexions;

@Configuration
@EnableWebSecurity
public class SecuriteConfig {

	@Bean
	public SecurityFilterChain chaineDeFiltres(HttpSecurity http,
			JournalConnexions journal,
			AuthenticationSuccessHandler succesHandler,
			AuthenticationFailureHandler echecHandler) throws Exception {

		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/connexion", "/acces-refuse", "/css/**", "/error").permitAll()
						.requestMatchers("/espace/administration/**", "/journal/**").hasRole("ADMIN")
						.requestMatchers("/espace/rh/**").hasRole("RH")
						.requestMatchers("/espace/manager/**").hasRole("MANAGER")
						.requestMatchers("/espace/employe/**").hasRole("EMPLOYE")
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/connexion")
						.loginProcessingUrl("/connexion")
						.usernameParameter("identifiant")
						.passwordParameter("motDePasse")
						.successHandler(succesHandler)
						.failureHandler(echecHandler)
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/deconnexion")
						.logoutSuccessHandler((requete, reponse, authentification) -> {
							if (authentification != null) {
								journal.enregistrer(authentification.getName(), TypeEvenement.DECONNEXION);
							}
							reponse.sendRedirect(requete.getContextPath() + "/?deconnecte");
						})
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.permitAll())
				.exceptionHandling(ex -> ex.accessDeniedPage("/acces-refuse"))
				.sessionManagement(session -> session
						.maximumSessions(2)
						.expiredUrl("/connexion?expiree"));

		return http.build();
	}

	@Bean
	public AuthenticationSuccessHandler succesHandler(JournalConnexions journal) {
		SavedRequestAwareAuthenticationSuccessHandler delegate = new SavedRequestAwareAuthenticationSuccessHandler();
		delegate.setDefaultTargetUrl("/mon-espace");

		return new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest requete, HttpServletResponse reponse,
					Authentication authentification) throws IOException, ServletException {
				journal.enregistrer(authentification.getName(), TypeEvenement.CONNEXION);
				delegate.onAuthenticationSuccess(requete, reponse, authentification);
			}
		};
	}

	@Bean
	public AuthenticationFailureHandler echecHandler(JournalConnexions journal) {
		SimpleUrlAuthenticationFailureHandler delegate = new SimpleUrlAuthenticationFailureHandler("/connexion?erreur");

		return new AuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest requete, HttpServletResponse reponse,
					org.springframework.security.core.AuthenticationException exception)
					throws IOException, ServletException {
				String saisi = requete.getParameter("identifiant");
				journal.enregistrer(saisi == null || saisi.isBlank() ? "(vide)" : saisi, TypeEvenement.ECHEC);
				delegate.onAuthenticationFailure(requete, reponse, exception);
			}
		};
	}
}
