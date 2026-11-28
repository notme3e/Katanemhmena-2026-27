package gr.hua.dit.petCareAthens.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.time.Instant;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain apiChain(final HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            // Προσωρινά httpBasic για εύκολο έλεγχο 401/403 με curl/Postman μέχρι να μπει JWT
            .httpBasic(basic -> {})
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(apiAuthenticationEntryPoint())
                .accessDeniedHandler(apiAccessDeniedHandler())
            );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain uiChain(final HttpSecurity http) throws Exception {
        http
            .securityMatcher("/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/login",
                    "/register",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {});

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ΠΡΟΣΩΡΙΝΟ: Μέχρι να συνδεθεί το ApplicationUserDetailsService με το PersonRepository
    @Bean
    public UserDetailsService tempUserDetailsService(final PasswordEncoder encoder) {
        UserDetails owner = User.builder()
            .username("owner@petcare.gr")
            .password(encoder.encode("1234"))
            .roles("OWNER") // αντιστοιχεί στο PersonType.OWNER
            .build();

        UserDetails vet = User.builder()
            .username("vet@petcare.gr")
            .password(encoder.encode("1234"))
            .roles("VET") // αντιστοιχεί στο PersonType.VET
            .build();

        return new InMemoryUserDetailsManager(owner, vet);
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint apiAuthenticationEntryPoint() {
        ObjectMapper mapper = new ObjectMapper();
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(mapper.writeValueAsString(Map.of(
                "timestamp", Instant.now().toString(),
                "status", 401,
                "error", "Unauthorized",
                "message", "Authentication required"
            )));
        };
    }

    @Bean
    public AccessDeniedHandler apiAccessDeniedHandler() {
        ObjectMapper mapper = new ObjectMapper();
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(mapper.writeValueAsString(Map.of(
                "timestamp", Instant.now().toString(),
                "status", 403,
                "error", "Forbidden",
                "message", "Access denied"
            )));
        };
    }
}
