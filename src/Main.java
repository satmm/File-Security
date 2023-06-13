
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import main.compressionnanddecompression;
public class Main {


        private SecretKey key;
        private final int KEY_SIZE = 128;
        private final int T_LEN = 128;
        private Cipher encryptionCipher;

        public void init() throws Exception {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(KEY_SIZE);
            key = generator.generateKey();
        }

        public void encryptFile(String inputFilePath, String outputFilePath) throws Exception {
            byte[] fileBytes = readFile(inputFilePath);
            encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
            encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = encryptionCipher.doFinal(fileBytes);
            writeFile(outputFilePath, encryptedBytes);
            System.out.println("File encrypted successfully.");
        }

        public void decryptFile(String inputFilePath, String outputFilePath) throws Exception {
            byte[] encryptedBytes = readFile(inputFilePath);
            Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(T_LEN, encryptionCipher.getIV());
            decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
            byte[] decryptedBytes = decryptionCipher.doFinal(encryptedBytes);
            writeFile(outputFilePath, decryptedBytes);
            System.out.println("File decrypted successfully.");
        }

        private byte[] readFile(String filePath) throws Exception {
            File file = new File(filePath);
            return Files.readAllBytes(file.toPath());
        }

        private void writeFile(String filePath, byte[] data) throws Exception {
            Path path = Path.of(filePath);
            Files.write(path, data, StandardOpenOption.CREATE);
        }

        public static void main(String[] args) {
            try {
                Main aes = new Main();

                aes.init();

                Scanner scanner = new Scanner(System.in);
                int choose;
                do
                {
                    System.out.println("\n* * * * * * Main Menu * * * * * *\n \n1)Encryption and Decryption of File\n2) Compression and Decompression of File\n3)Exit \nEnter your choice :");
                    choose = scanner.nextInt();
                    scanner.nextLine();
                    switch(choose)
                    {
                        case 1:
                            System.out.print("Enter the input file path: ");
                            String inputFilePath = scanner.nextLine();

                            System.out.print("Enter the output file path for encryption: ");
                            String encryptedFilePath = scanner.nextLine();


                            aes.encryptFile(inputFilePath, encryptedFilePath);

                            System.out.print("Enter the input file path for decryption: ");
                            String decryptedInputFilePath = scanner.nextLine();

                            System.out.print("Enter the output file path for decryption: ");
                            String decryptedFilePath = scanner.nextLine();

                            aes.decryptFile(decryptedInputFilePath, decryptedFilePath);
                            break;

                        case 2:
                            compressionnanddecompression cd = new compressionnanddecompression();
                            System.out.print("Enter the path of the file to compress: ");
                            String sourceFilePath = scanner.nextLine();

                            System.out.print("Enter the path to save the compressed file: ");
                            String compressedFilePath = scanner.nextLine();

                            System.out.print("Enter the path to save the decompressed file: ");
                            String decompressedFilePath = scanner.nextLine();

                            scanner.close();

                            // Compress the file
                            cd.compressFile(sourceFilePath, compressedFilePath);

                            // Decompress the file
                            cd.decompressFile(compressedFilePath, decompressedFilePath);
                            break;
                        case 3:
                            System.out.println("Exiting the program...");
                            scanner.close();
                            System.exit(0);
                            break;

                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;


                    }
                }while(choose<=3&& choose>0 );



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
     }

