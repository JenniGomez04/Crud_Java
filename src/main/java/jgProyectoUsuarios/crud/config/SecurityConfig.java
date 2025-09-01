package jgProyectoUsuarios.crud.config;

// Importaciones necesarias para configurar la seguridad en la aplicación
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration; // Agrupa las clases de Spring Boot
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Esta clase define una configuración, procesa para arrancar app
@Configuration

// Clase principal de configuración de seguridad,
// Protege rutas que requieren auntenticación inicio, cierre
public class SecurityConfig {

    // Metodo produce Bean, usa parte de la configuración de seguridad
    @Bean


    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // // Desactiva protección CSRF (útil en pruebas o APIs)

                // Configura quién puede acceder a qué recursos
                .authorizeHttpRequests(auth -> auth
                        // Permite acceso sin autenticación a la página de login y recursos estáticos.
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll() // Rutas públicas sin login
                        .anyRequest().authenticated() // Cualquier otra solicitud requerirá que el usuario esté autenticado.
                )

                .formLogin(form -> form
                        .loginPage("/login")   // ruta personalizada
                        .defaultSuccessUrl("/usuarios", true) // redirige luego del login
                        .permitAll() // Permite acceso libre al login

                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Redirige tras cerrar sesión
                        .permitAll() // Logout accesible sin restricciones
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
