package com.yo1000.samlsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2AssertionAuthentication;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2ResponseAssertionAccessor;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SamlSpApplication {
	public static void main(String[] args) {
		SpringApplication.run(SamlSpApplication.class, args);
	}

	@Configuration
	@EnableWebSecurity
	public static class SecurityConfig {
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			http.authorizeHttpRequests(auth -> auth
							.requestMatchers("/", "/error").permitAll()
							.anyRequest().authenticated())
					.saml2Login(Customizer.withDefaults())
					.saml2Logout(Customizer.withDefaults())
					.saml2Metadata(Customizer.withDefaults());
			return http.build();
		}
	}

	@RestController
	public static class HomeController {

		@GetMapping("/")
		public String home() {
			return "<a href=\"/protected\">Protected</a>";
		}

		@GetMapping("/protected")
		public String protectedPage(Saml2AssertionAuthentication authentication) {
			Saml2ResponseAssertionAccessor assertion = authentication.getCredentials();

			return "Hello " + authentication.getName()
					+ "<br>Attributes: " + assertion.getAttributes()
					+ "<br>NameID: " + assertion.getNameId()
					+ "<br><a href=\"/logout\">Logout (SLO)</a>";
		}
	}
}
