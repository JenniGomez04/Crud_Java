// Interfaz que maneja la comunicación con la base de datos para la tabla "usuarios"
package jgProyectoUsuarios.crud.repository; // Esta clase forma parte del paquete conexionBD

import jgProyectoUsuarios.crud.model.Usuario; // importa del package correcto
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // Importar Optional
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository // Se usa para que Spring reconozca esta interfaz como parte de la capa de acceso a datos

// Se define una interfaz que hereda lo que automaticamente da acceso a metodos utiles.
// JPA importa para acceder a la BD
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    // Para evitar errores si un resultado no existe.
    // Busca si un usuario existe por su correo (findByCorreo) o si ya está registrado (existsByCorreo).
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);

    // Busqueda en el filtro el @query personaliza las consultas por diferencias columnas
    @Query("SELECT u FROM Usuario u WHERE " +
            "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(u.correo) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(u.nitCc) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(u.razonSocial) LIKE LOWER(CONCAT('%', :filtro, '%'))")

    // Paginación
    Page<Usuario> buscarPorNombreCorreoNit(@Param("filtro") String filtro, Pageable pageable);

    // - findAll() Obtiene todos los usuarios
    // - findById(id) busca un usuario por su ID
    // - save(usuario) guarda o actualiza un usuario.
    // - deleteById(id) elimina un usuario por su ID
}
