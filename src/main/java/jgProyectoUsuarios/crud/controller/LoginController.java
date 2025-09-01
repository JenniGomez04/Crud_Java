package jgProyectoUsuarios.crud.controller;

// Importaciones necesarias para manejo de sesiones, modelo, rutas, cifrado y repositorio

import jakarta.servlet.http.HttpSession;
import jgProyectoUsuarios.crud.model.Usuario;
import jgProyectoUsuarios.crud.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;


@Controller // Marca esta clase como un controlador web de Spring
public class LoginController {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio; // Inyección del repositorio para consultar usuarios

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyección del codificador para verificar contraseñas cifradas

    // Muestra el formulario de login al acceder a /login
    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "login"; // Devuelve la vista login.html
    }

    // Procesa el formulario al enviar login (POST)
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {

        // Busca el usuario por correo. Si no existe, devuelve null.
        Usuario usuario = usuarioRepositorio.findByCorreo(correo).orElse(null);

        if (usuario != null && password != null && passwordEncoder.matches(password, usuario.getContrasena())) {
            session.setAttribute("usuarioLogueado", usuario); // Guarda Usuario si la contraseña es correcta
            return "redirect:/usuarios"; // Redirige a la lista de usuarios
        }

        return "redirect:/login?error=true"; // Si falló el login, redirige con error
    }
}
