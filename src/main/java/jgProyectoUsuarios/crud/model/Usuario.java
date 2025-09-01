package jgProyectoUsuarios.crud.model;

import jakarta.persistence.*;

// Esta clase representa la tabla "usuarios" en la base de datos dentro de un sistema CRUD

@Entity // Indica que esta clase es una entidad JPA (tabla)
@Table(name = "usuarios") // Nombre de la tabla en la BD
public class Usuario {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremento id
    private Long id;

    // Atributos de la tabla
    @Column(nullable = false)
    private String razonSocial;

    @Column(nullable = false, unique = true, length = 20) // No puede tener campos nulos
    private String nitCc;

    @Column(nullable = false) // No puede tener campos nulos
    private String nombre;

    @Column(length = 15)
    private String telefono;

    @Column(nullable = false, unique = true) // Correo debe ser unico
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    @Column(length = 100) // Ajusta la longitud según necesites
    private String tipoServicio;

    @Column(nullable = false, length = 50)
    private String estado; // "Activo", "Pendiente de Aprobación", "Inactivo", "Bloqueado"

    @Column(name = "fecha_registro", nullable = false)
    private String fechaRegistro; // Usamos String para simplificar el formato en el CSV


    // Constructor vacío requerido por JPA
    public Usuario() {
    }

    // Constructor con todos los datos (excepto ID)
    public Usuario(String razonSocial, String nitCc, String nombre, String telefono, String correo, String contrasena,
                   String tipoServicio, String estado)
    {
        this.razonSocial = razonSocial;
        this.nitCc = nitCc;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.contrasena = contrasena;
        this.tipoServicio = tipoServicio;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro; // Este valor no se está pasando como parámetro
    }

    // Getters y setters para acceder y modificar todos los campos privados
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNitCc() {
        return nitCc;
    }

    public void setNitCc(String nitCc) {
        this.nitCc = nitCc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

}
