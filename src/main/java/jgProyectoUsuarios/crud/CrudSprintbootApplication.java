package jgProyectoUsuarios.crud;

import jgProyectoUsuarios.crud.model.Usuario;
import jgProyectoUsuarios.crud.repository.UsuarioRepositorio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//  Indica que esta es la clase principal del sistema se habilita de forma automatica
@SpringBootApplication
public class CrudSprintbootApplication {

	// Este es el punto de arranque del sistema. Ejecuta el proyecto completo de Spring Boot.
	public static void main(String[] args) {
		SpringApplication.run(CrudSprintbootApplication.class, args);
	}

	// Para encriptar contraseña, Declara un PasswordEncoder usando BCrypt
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
}
	// Se ejecuta automaticamente y sirve para crear un usuario automaticamente si no existe aún
//	@Bean
//	public CommandLineRunner demoData(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder) {
//		return args -> {
//			if (usuarioRepositorio.findByCorreo("admin@correo.com").isEmpty()) {
//				Usuario admin = new Usuario();
//				admin.setRazonSocial("Admin Corp");
//				admin.setNitCc("123456789");
//				admin.setNombre("Administrador");
//				admin.setTelefono("5551234");
//				admin.setCorreo("admin@correo.com");
//				admin.setContrasena(passwordEncoder.encode("admin123"));
//				admin.setTipoServicio("Otros");
//				admin.setEstado("Activo");
//				admin.setFechaRegistro(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//				usuarioRepositorio.save(admin);
//				System.out.println("✅ Usuario administrador creado: admin@correo.com / admin123");
//			}
//		};
//	}

