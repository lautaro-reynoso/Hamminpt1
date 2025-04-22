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
        File archivo = new File(nombre);
    
        if (!archivo.exists()) {
            System.out.println("El archivo no existe.");
            return;
        }
    
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            System.out.println("\n--- Contenido del archivo ---");
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
            System.out.println("--- Fin del archivo ---");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }
    

    public static void protegerArchivo(String nombre, int bloque) {
        try {
            File inputFile = new File(nombre);
            FileInputStream fis = new FileInputStream(inputFile);
            byte[] buffer = fis.readAllBytes();
            fis.close();
    
            String outputName = nombre.replace(".txt", ".HA" + (bloque == 8 ? "1" : bloque == 256 ? "2" : "3"));
            FileOutputStream fos = new FileOutputStream(outputName);
    
            int bytesPorBloque = bloque / 8;
    
            for (int i = 0; i < buffer.length; i += bytesPorBloque) {
                int fin = Math.min(i + bytesPorBloque, buffer.length); 
                for (int j = i; j < fin; j++) {
                    byte b = buffer[j];
                    int highNibble = (b >> 4) & 0x0F;
                    int lowNibble = b & 0x0F;
            
                    int[] highBits = intTo4Bits(highNibble);
                    int[] lowBits = intTo4Bits(lowNibble);
            
                    int[] hammingHigh = hammingEncode84(highBits);
                    int[] hammingLow = hammingEncode84(lowBits);
            
                    fos.write(bitsToByte(hammingHigh));
                    fos.write(bitsToByte(hammingLow));
                }
            }
            
    
            fos.close();
            System.out.println("Archivo protegido guardado como: " + outputName);
    
        } catch (IOException e) {
            System.out.println("Error al proteger archivo: " + e.getMessage());
        }
    }
    
    

    public static int[] byteToBits8(byte b) {
        int[] bits = new int[8];
        for (int i = 0; i < 8; i++) {
            bits[i] = (b >> (7 - i)) & 1;
        }
        return bits;
    }
    

    public static int[] hammingEncode84(int[] data) {
        int[] hamming = new int[8];
    
        // Bits de datos
        hamming[2] = data[0];
        hamming[4] = data[1];
        hamming[5] = data[2];
        hamming[6] = data[3];
    
        // Bits de paridad
        hamming[0] = hamming[2] ^ hamming[4] ^ hamming[6]; // p1
        hamming[1] = hamming[2] ^ hamming[5] ^ hamming[6]; // p2
        hamming[3] = hamming[4] ^ hamming[5] ^ hamming[6]; // p3
    
        // Bit de paridad general (p0)
        hamming[7] = hamming[0] ^ hamming[1] ^ hamming[2] ^ hamming[3] ^ hamming[4] ^ hamming[5] ^ hamming[6];
    
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
                // 50% de probabilidad de introducir un error en este byte
                if (random.nextBoolean()) {
                    int bitAInvertir = random.nextInt(7); // solo 7 bits relevantes
                    buffer[i] ^= (1 << (7 - bitAInvertir));
                }
            }
    
            String outputName = archivoHa.replace(".HA", ".HE");
            FileOutputStream fos = new FileOutputStream(outputName);
            fos.write(buffer);
            fos.close();
    
            System.out.println("Errores aleatorios introducidos. Archivo guardado como: " + outputName);
    
        } catch (IOException e) {
            System.out.println("Error al introducir errores: " + e.getMessage());
        }
    }

    public static void desprotegerSinCorregir(String archivo) {
        try {
            File inputFile = new File(archivo);
            FileInputStream fis = new FileInputStream(inputFile);
            byte[] buffer = fis.readAllBytes();
            fis.close();
    
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
            for (int i = 0; i < buffer.length; i += 2) {
                int[] bits1 = byteToBits8(buffer[i]);
                int[] bits2 = byteToBits8(buffer[i + 1]);
    
                int[] data1 = extractDataBits(bits1);
                int[] data2 = extractDataBits(bits2);
    
                byte original = (byte) ((bitsToInt(data1) << 4) | bitsToInt(data2));
                baos.write(original);
            }
    
            String outputName = archivo.replace(".HA", ".DE").replace(".HE", ".DE");
            FileOutputStream fos = new FileOutputStream(outputName);
            fos.write(baos.toByteArray());
            fos.close();
    
            System.out.println("Archivo desprotegido sin corrección: " + outputName);
        } catch (IOException e) {
            System.out.println("Error al desproteger archivo: " + e.getMessage());
        }
    }
    
    

    public static void desprotegerConCorreccion(String archivo) {
        try {
            File inputFile = new File(archivo);
            FileInputStream fis = new FileInputStream(inputFile);
            byte[] buffer = fis.readAllBytes();
            fis.close();
    
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
            for (int i = 0; i < buffer.length; i += 2) {
                int[] bits1 = byteToBits8(buffer[i]);
                int[] bits2 = byteToBits8(buffer[i + 1]);
    
                correctHamming84(bits1);
                correctHamming84(bits2);
    
                int[] data1 = extractDataBits(bits1);
                int[] data2 = extractDataBits(bits2);
    
                byte original = (byte) ((bitsToInt(data1) << 4) | bitsToInt(data2));
                baos.write(original);
            }
    
            String outputName = archivo.replace(".HA", ".DC").replace(".HE", ".DC");
            FileOutputStream fos = new FileOutputStream(outputName);
            fos.write(baos.toByteArray());
            fos.close();
    
            System.out.println("Archivo desprotegido con corrección: " + outputName);
        } catch (IOException e) {
            System.out.println("Error al desproteger archivo: " + e.getMessage());
        }
    }
    
    
    public static int[] intTo4Bits(int n) {
        int[] bits = new int[4];
        for (int i = 0; i < 4; i++) {
            bits[i] = (n >> (3 - i)) & 1;
        }
        return bits;
    }
    
    public static int bitsToInt(int[] bits) {
        int result = 0;
        for (int i = 0; i < bits.length; i++) {
            result |= (bits[i] << (bits.length - 1 - i));
        }
        return result;
    }
    
    public static int[] byteToBits7(byte b) {
        int[] bits = new int[7];
        for (int i = 0; i < 7; i++) {
            bits[i] = (b >> (7 - i)) & 1;
        }
        return bits;
    }

    public static int[] extractDataBits(int[] bits) {
        return new int[] { bits[2], bits[4], bits[5], bits[6] };
    }

    public static byte pack4Bits(int[] bits) {
        byte result = 0;
        for (int i = 0; i < 4; i++) {
            result |= (bits[i] << (7 - i));
        }
        return result;
    }

    public static void correctHamming84(int[] bits) {
        int p0 = bits[7]; // paridad general
        int p1 = bits[0];
        int p2 = bits[1];
        int d1 = bits[2];
        int p3 = bits[3];
        int d2 = bits[4];
        int d3 = bits[5];
        int d4 = bits[6];
    
        int c1 = p1 ^ d1 ^ d2 ^ d4;
        int c2 = p2 ^ d1 ^ d3 ^ d4;
        int c3 = p3 ^ d2 ^ d3 ^ d4;
        int c0 = p0 ^ p1 ^ p2 ^ d1 ^ p3 ^ d2 ^ d3 ^ d4;
    
        int errorPos = (c3 << 2) | (c2 << 1) | c1;
    
        if (c0 == 1 && errorPos == 0) {
            // Error en el bit de paridad general
            bits[7] ^= 1;
        } else if (errorPos > 0 && errorPos <= 7) {
            // Error en los otros bits
            bits[errorPos - 1] ^= 1;
        }
    }
    
    
    
    
    
    
}
