//// Este codigo maneja las peticiones http relacionada con la entidad usuario y responde
//// con vistas hmtl

package jgProyectoUsuarios.crud.controller;

import jgProyectoUsuarios.crud.model.Usuario;
import org.springframework.data.domain.Page;
import jgProyectoUsuarios.crud.database.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller // Esta clase actúa como controlador en la arquitectura MVC y responde con vistas(HTML)
@RequestMapping("/usuarios") // Todas las rutas comienzan con /usuarios

public class UsuarioController {

    @Autowired // Facilita que se apliquen las dependencias sin necesidad de instanciar.

    private UsuarioServicio usuarioServicio;  // Contiene la lógica para trabajar con usuarios

    // Paginación: Mostrar usuarios por página, filtro de busqueda
    @GetMapping
    public String mostrarUsuarios(@RequestParam(defaultValue = "0") int pagina,
                                  @RequestParam(defaultValue = "7") int tamano,
                                  @RequestParam(required = false) String filtro, // Palabra clave para buscar
                                  Model modelo) {

        Page<Usuario> paginaUsuarios;

        if (filtro != null && !filtro.isBlank()) {
            paginaUsuarios = usuarioServicio.buscarPorFiltro(filtro, pagina, tamano);  // Buscar por nombre o correo
        } else {
            paginaUsuarios = usuarioServicio.obtenerUsuariosPaginados(pagina, tamano); // Obtener sin filtro
        }

        modelo.addAttribute("usuarios", paginaUsuarios.getContent());
        modelo.addAttribute("paginaUsuarios", paginaUsuarios);
        modelo.addAttribute("paginaActual", pagina);
        modelo.addAttribute("totalPaginas", paginaUsuarios.getTotalPages());
        modelo.addAttribute("filtro", filtro);

        return "usuarios/listado";  // Devuelve la vista usuarios/listado.html
    }


    @PostMapping("/usuarios/registrar")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {
        usuarioServicio.guardarUsuario(usuario);  // Guarda en BD
        usuarioServicio.guardarUsuario(usuario); // Este ya llama a GeneradorCSV con la lista completa
        model.addAttribute("mensaje", "✅ Usuario registrado.");
        return "usuarios/formulario";
    }

    // Mostrar formulario para agregar nuevo usuario
     @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model modelo) {
        modelo.addAttribute("usuario", new Usuario());
        return "usuarios/formulario"; // Vista con el formulario vacio
    }

    // Guardar nuevo usuario o actualización
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioServicio.guardarUsuario(usuario);
        return "redirect:/usuarios"; // Redirige a la lista de usuarios
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model modelo) {
        Usuario usuario = usuarioServicio.obtenerPorId(id);
        modelo.addAttribute("usuario", usuario);
        return "usuarios/formulario"; // Muestra el formulario con datos cargados
    }

    // Exportar todos los usuarios de la BD
    @GetMapping("/exportar-todos")
    public void exportarTodos(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios_completos.csv");

        usuarioServicio.exportarTodosComoCSV(response.getWriter()); // Exporta todos los usuarios
    }


    // Exportar solo los proveedores de la pagina actual
    @GetMapping("/exportar-pagina")
    public void exportarPagina(@RequestParam(defaultValue = "0") int pagina,
                               @RequestParam(defaultValue = "8") int tamano,
                               HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios_pagina.csv");

        Page<Usuario> paginaUsuarios = usuarioServicio.obtenerUsuariosPaginados(pagina, tamano);
        // Exporta solo los usuarios de esa página
        usuarioServicio.exportarPaginaComoCSV(paginaUsuarios.getContent(), response.getWriter());
    }


    // Eliminar usuario
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioServicio.eliminarPorId(id);
        return "redirect:/usuarios"; // Actualiza la lista sin el usuario eliminado
    }
}


