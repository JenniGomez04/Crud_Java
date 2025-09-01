package jgProyectoUsuarios.crud.utils;

import jgProyectoUsuarios.crud.model.Usuario;

import java.io.*; // Para escribir archivos
import java.time.LocalDate; // Manejar fechas
import java.time.format.DateTimeFormatter; // Manejar Fechas
import java.util.List; // Procesar Lista de usuarios

// Clase utilitaria sin atributos, con un único método público.
public class GeneradorCSV {

    // Este método genera un archivo CSV con los datos de todos los usuarios que recibe por parámetro.
    public static void generarArchivo(List<Usuario> listaUsuarios) {

        // Generar nombre del archivo con la fecha
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String fecha = LocalDate.now().format(formatter);
            String nombreArchivo = "usuarios_" + fecha + ".csv";

            // Verificar si la carpeta archivos_csv existe, si no, crearla
            File carpeta = new File("archivos_csv");
            if (!carpeta.exists()) carpeta.mkdirs();

            // Crear o abrir el archivo CSV
            File archivo = new File(carpeta, nombreArchivo);
            boolean archivoNuevo = archivo.createNewFile();


            // Escribir en el archivo
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
                if (archivoNuevo) {
                    //  Escribir la cabecera solo si el archivo es nuevo
                    writer.write("ID,Nombre,Correo,Contrasena,FechaRegistro");
                    writer.newLine();
                }

                // Escribe cada usuario
                for (Usuario usuario : listaUsuarios) {
                    writer.write(usuario.getId() + "," +
                            usuario.getRazonSocial() + "," +
                            usuario.getNitCc() + "," +
                            usuario.getNombre() + "," +
                            usuario.getTelefono() + "," +
                            usuario.getCorreo() + "," +
                            usuario.getContrasena() + "," +
                            usuario.getTipoServicio() + "," +
                            usuario.getEstado() + "," +
                            usuario.getFechaRegistro());
                    writer.newLine();
                }
            }

            System.out.println("✅ Archivo CSV guardado exitosamente.");

        } catch (IOException e) {
            System.err.println("❌ Error al generar CSV: " + e.getMessage());
        }
    }
}
