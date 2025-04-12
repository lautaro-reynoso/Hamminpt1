package hamming1;

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
        try {
            File inputFile = new File(nombre);
            FileInputStream fis = new FileInputStream(inputFile);
            byte[] buffer = fis.readAllBytes();
            fis.close();

            String outputName = nombre.replace(".txt", ".HA1");
            FileOutputStream fos = new FileOutputStream(outputName);

            for (byte b : buffer) {
                int[] dataBits = byteToBits(b);
                int[] hammingBits = hammingEncode74(dataBits);
                fos.write(bitsToByte(hammingBits));
            }

            fos.close();
            System.out.println("Archivo protegido guardado como: " + outputName);

        } catch (IOException e) {
            System.out.println("Error al proteger archivo: " + e.getMessage());
        }
    }

    public static int[] byteToBits(byte b) {
        int[] bits = new int[4];
        for (int i = 0; i < 4; i++) {
            bits[i] = (b >> (7 - i)) & 1;
        }
        return bits;
    }

    public static int[] hammingEncode74(int[] data) {
        int[] hamming = new int[7];
        hamming[2] = data[0];
        hamming[4] = data[1];
        hamming[5] = data[2];
        hamming[6] = data[3];

        hamming[0] = hamming[2] ^ hamming[4] ^ hamming[6];
        hamming[1] = hamming[2] ^ hamming[5] ^ hamming[6];
        hamming[3] = hamming[4] ^ hamming[5] ^ hamming[6];

        return hamming;
    }

    public static byte bitsToByte(int[] bits) {
        byte result = 0;
        for (int i = 0; i < bits.length; i++) {
            result |= (bits[i] << (7 - i));
        }
        return result;
    }


    public static void introducirErrores(String archivoHa) {
        try {
            File inputFile = new File(archivoHa);
            FileInputStream fis = new FileInputStream(inputFile);
            byte[] buffer = fis.readAllBytes();
            fis.close();

            Random random = new Random();

            for (int i = 0; i < buffer.length; i++) {
                // Introduce un error en 1 bit aleatorio del byte (bit 0 a 6)
                int bitAInvertir = random.nextInt(7); // solo 7 bits relevantes
                buffer[i] ^= (1 << (7 - bitAInvertir));
            }

            String outputName = archivoHa.replace(".HA", ".HE");
            FileOutputStream fos = new FileOutputStream(outputName);
            fos.write(buffer);
            fos.close();

            System.out.println("Errores introducidos. Archivo guardado como: " + outputName);

        } catch (IOException e) {
            System.out.println("Error al introducir errores: " + e.getMessage());
        }
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
