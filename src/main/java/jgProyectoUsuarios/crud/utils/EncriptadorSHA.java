package jgProyectoUsuarios.crud.utils;

import java.security.MessageDigest; // permite aplicar algoritmos de cifrado como SHA-256.
import java.security.NoSuchAlgorithmException; // es una excepción por si el algoritmo no existe.

// clase auxiliar con un método estático (no necesitas crear un objeto para usarlo).
public class EncriptadorSHA {

    // Método para encriptar una cadena con SHA-256
    public static String encriptarSHA256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // Se especifica el algoritmo
            byte[] hash = md.digest(texto.getBytes()); // Aplica SHA-256 y devuelve bytes
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                // Convierte cada byte a hexadecimal
                String hex = Integer.toHexString(0xff & b); // Convierte byte a número hexadecimal
                if (hex.length() == 1) hexString.append('0'); // Asegura que cada byte tenga 2 dígitos
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar con SHA-256", e);
        }
    }
}
