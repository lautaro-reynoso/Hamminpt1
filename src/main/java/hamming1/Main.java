import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===== MENU HAMMING =====");
            System.out.println("1. Cargar archivo .txt");
            System.out.println("2. Proteger archivo (.HA1/.HA2/.HA3)");
            System.out.println("3. Introducir errores (.HEx)");
            System.out.println("4. Desproteger sin corregir (.DEx)");
            System.out.println("5. Desproteger con corrección (.DCx)");
            System.out.println("6. Salir");
            System.out.print("Elija una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre del archivo .txt: ");
                    String archivoTxt = scanner.nextLine();
                    cargarArchivo(archivoTxt);
                    break;
                case 2:
                    System.out.print("Ingrese nombre del archivo .txt a proteger: ");
                    String entrada = scanner.nextLine();
                    System.out.print("Tamaño del bloque (8/256/4096): ");
                    int bloque = scanner.nextInt();
                    scanner.nextLine();
                    protegerArchivo(entrada, bloque);
                    break;
                case 3:
                    System.out.print("Ingrese archivo protegido (.HAx): ");
                    String archivoHa = scanner.nextLine();
                    introducirErrores(archivoHa);
                    break;
                case 4:
                    System.out.print("Ingrese archivo con errores (.HAx o .HEx): ");
                    String archivoConErrores = scanner.nextLine();
                    desprotegerSinCorregir(archivoConErrores);
                    break;
                case 5:
                    System.out.print("Ingrese archivo con errores (.HAx o .HEx): ");
                    String archivoACorregir = scanner.nextLine();
                    desprotegerConCorreccion(archivoACorregir);
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }

        } while (opcion != 6);

        scanner.close();
    }

    public static void cargarArchivo(String nombre) {
        System.out.println("[Cargar archivo] Funcionalidad pendiente");
        // Leer archivo, mostrar por consola si se quiere
    }

    public static void protegerArchivo(String nombre, int bloque) {
        System.out.println("[Proteger archivo] Tamaño bloque: " + bloque);
        // Leer archivo, convertir a bits, aplicar Hamming, guardar .HAx
    }

    public static void introducirErrores(String archivoHa) {
        System.out.println("[Introducir errores] en archivo: " + archivoHa);
        // Leer .HAx, introducir errores aleatorios, guardar .HEx
    }

    public static void desprotegerSinCorregir(String archivo) {
        System.out.println("[Desproteger sin corrección] archivo: " + archivo);
        // Leer .HAx o .HEx, decodificar sin corregir, guardar .DEx
    }

    public static void desprotegerConCorreccion(String archivo) {
        System.out.println("[Desproteger con corrección] archivo: " + archivo);
        // Leer .HAx o .HEx, corregir errores, decodificar, guardar .DCx
    }
}
