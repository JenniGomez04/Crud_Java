package jgProyectoUsuarios.crud.database;

import jakarta.annotation.PostConstruct;
import jgProyectoUsuarios.crud.model.Usuario;
import jgProyectoUsuarios.crud.repository.UsuarioRepositorio;
import jgProyectoUsuarios.crud.utils.GeneradorCSV;
import org.springframework.data.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.PrintWriter; //

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

@Service // Marca esta clase como un servicio que Spring debe manejar

// Implementa carga de usuarios para login con Spring Security
// Implements es una clase que utiliza una interfaz
public class UsuarioServicio implements UserDetailsService {

    // Dependencias inyectadas
    @Autowired
    private UsuarioRepositorio repositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Al iniciar la aplicación, crear usuario admin si no existe
    @PostConstruct
    public void initAdmin() {
        if (!repositorio.existsByCorreo("admin@correo.com")) {
            Usuario admin = new Usuario();
            admin.setCorreo("admin@correo.com");
            admin.setNombre("Administrador");
            admin.setContrasena(passwordEncoder.encode("admin123"));
            admin.setRazonSocial("Sistema");
            admin.setTelefono("1234567890");
            admin.setNitCc("12345678");
            admin.setTipoServicio("Otros");
            admin.setEstado("Activo");
            admin.setFechaRegistro(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            repositorio.save(admin);
        }
    }

    // Obtener los usuarios
    public List<Usuario> obtenerTodos() {
        return repositorio.findAll();
    }

    // Paginación, obtiene usuarios paginados para la vista
    public Page<Usuario> obtenerUsuariosPaginados(int pagina, int tamano) {
        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by("id").ascending());
        return repositorio.findAll(pageable);
    }

    // Busqueda en el filtro (Nombre, Correo o Nit)
    public Page<Usuario> buscarPorFiltro(String filtro, int pagina, int tamano) {
        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by("id").ascending());
        return repositorio.buscarPorNombreCorreoNit(filtro, pageable);
    }

    // Realiza Busqueda por ID
    public Usuario obtenerPorId(Long id) {
        Optional<Usuario> usuario = repositorio.findById(id);
        return usuario.orElse(null);
    }

    // Busca por correo
    public Optional<Usuario> obtenerPorCorreo(String correo) {
        return repositorio.findByCorreo(correo);
    }

    // Guarda o Actualiza un usuario
    public void guardarUsuario(Usuario usuario) {
        // Si no tiene fecha de registro, se la asigna hoy
        if (usuario.getId() == null || usuario.getFechaRegistro() == null || usuario.getFechaRegistro().isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String fecha = LocalDate.now().format(formatter);
            usuario.setFechaRegistro(fecha);
        }

        Optional<Usuario> existingUserOptional = Optional.empty();
        if (usuario.getId() != null) {
            existingUserOptional = repositorio.findById(usuario.getId());
        }

        // Si tiene contraseña, verifica si es igual a la guardada (para evitar volver a cifrar)
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            if (existingUserOptional.isPresent() &&
                    passwordEncoder.matches(usuario.getContrasena(), existingUserOptional.get().getContrasena())) {
                usuario.setContrasena(existingUserOptional.get().getContrasena()); // Mantiene la original
            } else {
                String contrasenaEncriptada = passwordEncoder.encode(usuario.getContrasena());
                usuario.setContrasena(contrasenaEncriptada); // Recupera la contraseña anterior
            }
        } else if (usuario.getId() != null) {
            existingUserOptional.ifPresent(existingUser -> {
                usuario.setContrasena(existingUser.getContrasena());
            });
        }

        repositorio.save(usuario); // Guarda en BD
        List<Usuario> lista = repositorio.findAll(); // Genera archivo CSV con la lista actualizada
        GeneradorCSV.generarArchivo(lista);
    }

    @Override // Sobreescribiendo un metodo definido de la super clase
    // Carga de usuarios para login
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repositorio.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new org.springframework.security.core.userdetails.User(
                usuario.getCorreo(),
                usuario.getContrasena(),
                Collections.emptyList() // Sin roles
        );
    }


    // Metodo para exportar todos los registros de proveedores como csv
    public void exportarTodosComoCSV(PrintWriter writer) {
        List<Usuario> lista = repositorio.findAll();

        writer.println("ID,Razón Social,NIT/CC,Nombre,Teléfono,Correo,Tipo Servicio,Estado,Fecha Registro");

        for (Usuario u : lista) {
            writer.println(String.join(",",
                    String.valueOf(u.getId()),
                    u.getRazonSocial(),
                    u.getNitCc(),
                    u.getNombre(),
                    u.getTelefono(),
                    u.getCorreo(),
                    u.getTipoServicio(),
                    u.getEstado(),
                    u.getFechaRegistro()
            ));
        }
    }

    // Metodo para exportar los registros de la pagina actual como csv
    public void exportarPaginaComoCSV(List<Usuario> usuarios, PrintWriter writer) {
        writer.println("ID,Razón Social,NIT/CC,Nombre,Teléfono,Correo,Tipo Servicio,Estado,Fecha Registro");

        for (Usuario u : usuarios) {
            writer.println(String.join(",",
                    String.valueOf(u.getId()),
                    u.getRazonSocial(),
                    u.getNitCc(),
                    u.getNombre(),
                    u.getTelefono(),
                    u.getCorreo(),
                    u.getTipoServicio(),
                    u.getEstado(),
                    u.getFechaRegistro()
            ));
        }
    }


    // Elimina los usuarios por id
    public void eliminarPorId(Long id) {
        repositorio.deleteById(id);
        List<Usuario> lista = repositorio.findAll();
        GeneradorCSV.generarArchivo(lista);
    }
}
