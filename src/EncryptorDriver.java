import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class serves as the driver for the encryption tool.
 * It allows users to encode and decode text using multiple schemes.
 * Supports input validation and reusing the previous input if no new text is provided.
 *
 * @author Sonya Pathania
 * @version 1.0
 */
public class EncryptorDriver {
    /**
     * Main method driving the encryption/decryption tool.
     *
     * @param args an array of command-line arguments
     */
    public static void main(String[] args) {
        String previous = "";
        String input = "";
        System.out.println("Get Started with the Encryption Tool!");
        System.out.println("Available Commands:");
        System.out.println("encode <scheme> <parameter> [target text]");
        System.out.println("decode <scheme> <parameter> [target text]");
        System.out.println("stats");
        System.out.println("exit");

        Scanner scan = new Scanner(System.in);

        while (!input.equals("exit")) {
            System.out.print("> ");
            input = scan.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            Scanner commandScan = new Scanner(input);
            String command = commandScan.next();

            // Validate command
            if (!command.equals("encode") && !command.equals("decode")
                    && !command.equals("stats") && !command.equals("exit")) {
                System.out.println("Error: Invalid command");
                commandScan.close();
                continue;
            }

            if (command.equals("exit")) {
                System.out.println("Goodbye!");
                commandScan.close();
                break;
            } else if (command.equals("stats")) {
                System.out.println(Encryptor.encryptionTallyString());
                commandScan.close();
                continue;
            }

            // Handle encode/decode commands
            String scheme = "";
            int parameter = 0;
            String text = "";

            try {
                scheme = commandScan.next();
            } catch (Exception e) {
                System.out.println("Error: Missing encryption scheme");
                commandScan.close();
                continue;
            }

            // Validate scheme
            if (!scheme.equals("basen") && !scheme.equals("caesar") && !scheme.equals("rotate")) {
                System.out.println("Error: Invalid scheme");
                commandScan.close();
                continue;
            }

            // Validate parameter
            try {
                parameter = commandScan.nextInt();
                if (parameter < 0) {
                    System.out.println("Error: Parameter must be non-negative");
                    commandScan.close();
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Parameter must be an integer");
                commandScan.close();
                continue;
            } catch (Exception e) {
                System.out.println("Error: Missing parameter");
                commandScan.close();
                continue;
            }

            // Extra validation: basen only works with base 2 or 16
            if (scheme.equals("basen") && parameter != 2 && parameter != 16) {
                System.out.println("Error: Base-n scheme only supports base 2 or base 16");
                commandScan.close();
                continue;
            }

            // Get text input or use previous/default
            if (commandScan.hasNext()) {
                text = commandScan.nextLine().trim();
            } else if (previous.isEmpty()) {
                text = "my encryption string";
            } else {
                text = previous;
            }

            // Encode/decode based on scheme
            switch (scheme) {
                case "basen":
                    Encryptor e = new Encryptor(Scheme.BASE_N);
                    previous = command.equals("encode") ? e.encode(text, parameter) : e.decode(text, parameter);
                    break;
                case "caesar":
                    Encryptor f = new Encryptor(Scheme.CAESAR);
                    previous = command.equals("encode") ? f.encode(text, parameter) : f.decode(text, parameter);
                    break;
                case "rotate":
                    Encryptor g = new Encryptor(Scheme.ROTATE);
                    previous = command.equals("encode") ? g.encode(text, parameter) : g.decode(text, parameter);
                    break;
            }

            System.out.println(previous);
            commandScan.close();
        }

        scan.close();
    }
}